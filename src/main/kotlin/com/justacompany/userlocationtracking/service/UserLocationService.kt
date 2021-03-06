package com.justacompany.userlocationtracking.service

import com.justacompany.userlocationtracking.datatransferobject.NormalizedCoordinates
import com.justacompany.userlocationtracking.domain.UserLocation
import com.justacompany.userlocationtracking.periphery.NotificationResponse
import com.justacompany.userlocationtracking.periphery.UserLocationRequest
import com.justacompany.userlocationtracking.repository.UserLocationRepository
import com.justacompany.userlocationtracking.repository.UserStatusRepository
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.geom.GeometryFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserLocationService(
        private val userLocationRepository: UserLocationRepository,
        private val userStatusRepository: UserStatusRepository,
        private val userStatusService: UserStatusService
) {

    companion object{
        private const val CHUNK_SIZE = 4
        private const val BUFFER_AREA = 0.0005
    }
    fun postUserLocation(userLocationRequest: UserLocationRequest): NotificationResponse {
        userLocationRepository.saveAll(makeUserLocationsFromUserLocationRequest(userLocationRequest))
        return getNotificationForUser(userId = userLocationRequest.userId)
    }

    fun getNotificationForUser(userId: String): NotificationResponse {

        if (userStatusService.checkIfUserIsAffected(userId)) {
            return NotificationResponse(
                    raiseAlarm = true,
                    alreadyAffected = true,
                    severityLevel = 3
            )
        }

        val geometryFactory = GeometryFactory()
        val listOfAffectedPolygons = mutableListOf<Geometry>()
        val listOfAffectedPoints = mutableListOf<Geometry>()

        val listOfUserPolygons = mutableListOf<Geometry>()
        val listOfUserPoints = mutableListOf<Geometry>()

        // find affected user polygons
        val infectedUserIds = userStatusRepository.findAllUserIdByRaiseAlarm(
                raiseAlarm = true
        ).map { it.userId }

        val sortedDescendingLocationsOfAllAffectedUsers = userLocationRepository.findAllByUserIdIn(infectedUserIds)
                .sortedByDescending { userLocation -> userLocation.dateCreated }

        if (sortedDescendingLocationsOfAllAffectedUsers.isNullOrEmpty()) {
            return NotificationResponse(
                    raiseAlarm = false,
                    alreadyAffected = false,
                    severityLevel = 1
            )
        }

        if (sortedDescendingLocationsOfAllAffectedUsers.size < CHUNK_SIZE) {
            val nonChunkedAffectedUserPoints = makeCoordinates(sortedDescendingLocationsOfAllAffectedUsers)
            val nonChunkedAffectedCoordinates = NormalizedCoordinates(
                    normalizedChunkedCoordinates = emptyList(),
                    residualCoordinates = nonChunkedAffectedUserPoints
            )

            listOfAffectedPoints.addAll(nonChunkedAffectedCoordinates.residualCoordinates
                    .map { coordinate -> geometryFactory.createPoint(coordinate).buffer(BUFFER_AREA) }
            )

        } else {
            val chunkedAffectedUserLocations = sortedDescendingLocationsOfAllAffectedUsers.chunked(CHUNK_SIZE)
            val chunkedAffectedCoordinates = chunkedAffectedUserLocations.map { listOfAffectedUserLocations -> makeCoordinates(listOfAffectedUserLocations) }
            val normalizedChunksAffectedCoordinates = normalizeChunks(chunkedAffectedCoordinates)

            listOfAffectedPolygons.addAll(normalizedChunksAffectedCoordinates.normalizedChunkedCoordinates
                    .map { coordinates -> geometryFactory.createPolygon(coordinates.toTypedArray()).buffer(BUFFER_AREA) }
            )

            listOfAffectedPoints.addAll(normalizedChunksAffectedCoordinates.residualCoordinates
                    .map { coordinate -> geometryFactory.createPoint(coordinate).buffer(BUFFER_AREA) }
            )
        }

        // find user polygons
        val sortedDescendingLocationsOfUser = userLocationRepository.findAllByUserId(userId)
                .sortedByDescending { userLocation -> userLocation.dateCreated }

        if (sortedDescendingLocationsOfUser.isNullOrEmpty()) {
            return NotificationResponse(
                    raiseAlarm = false,
                    alreadyAffected = false,
                    severityLevel = 1
            )
        }

        if (sortedDescendingLocationsOfUser.size < CHUNK_SIZE) {
            val nonChunkedUserPoints = makeCoordinates(sortedDescendingLocationsOfUser)
            val nonChunkedUserCoordinates = NormalizedCoordinates(
                    normalizedChunkedCoordinates = emptyList(),
                    residualCoordinates = nonChunkedUserPoints
            )

            listOfUserPoints.addAll(nonChunkedUserCoordinates.residualCoordinates
                    .map { coordinate -> geometryFactory.createPoint(coordinate).buffer(BUFFER_AREA) }
            )
        } else {
            val chunkedUserLocations = sortedDescendingLocationsOfUser.chunked(CHUNK_SIZE)
            val chunkedUserCoordinates = chunkedUserLocations.map { listOfUserLocations -> makeCoordinates(listOfUserLocations) }
            val normalizedChunksUserCoordinates = normalizeChunks(chunkedUserCoordinates)
            listOfUserPolygons.addAll(normalizedChunksUserCoordinates.normalizedChunkedCoordinates
                    .map { coordinates -> geometryFactory.createPolygon(coordinates.toTypedArray()).buffer(BUFFER_AREA) }
            )
            listOfUserPoints.addAll(normalizedChunksUserCoordinates.residualCoordinates
                    .map { coordinate -> geometryFactory.createPoint(coordinate).buffer(BUFFER_AREA) }
            )
        }


        // Check intersection with the user polygon
        for (affectedPoint in listOfAffectedPoints) {
            for (userPoint in listOfUserPoints) {
                if (affectedPoint.intersects(userPoint)) {
                    return NotificationResponse(
                            raiseAlarm = true,
                            alreadyAffected = false,
                            severityLevel = 3
                    )
                }
            }
        }
        for (affectedPolygon in listOfAffectedPolygons) {
            for (userPolygon in listOfUserPolygons) {
                if (affectedPolygon.intersects(userPolygon)) {
                    return NotificationResponse(
                            raiseAlarm = true,
                            alreadyAffected = false,
                            severityLevel = 2
                    )
                }
            }
        }

        return NotificationResponse(
                raiseAlarm = false,
                alreadyAffected = false,
                severityLevel = 1
        )
    }

    private fun normalizeChunks(chunkedCoordinates: List<List<Coordinate>>): NormalizedCoordinates {
        val normalizedChunkedCoordinates = mutableListOf<List<Coordinate>>()
        val residualCoordinates = mutableListOf<Coordinate>()
        for (coordinates in chunkedCoordinates){
            if (coordinates.size != CHUNK_SIZE) {
                residualCoordinates.addAll(coordinates)
            } else{
                normalizedChunkedCoordinates.add(coordinates)
            }
        }

        return NormalizedCoordinates(
                normalizedChunkedCoordinates = normalizedChunkedCoordinates,
                residualCoordinates = residualCoordinates
        )
    }

    private fun makeCoordinates(userLocations: List<UserLocation>): List<Coordinate> {
        return userLocations.map {
            Coordinate().apply {
                x = it.longitude
                y = it.latitude
            }
        }.plus(
                Coordinate().apply {
                    x = userLocations.first().longitude
                    y = userLocations.first().latitude
                })
    }

    private fun makeUserLocationsFromUserLocationRequest(userLocationRequest: UserLocationRequest): List<UserLocation> {
        return userLocationRequest.userCoordinates.map {
            UserLocation(
                    userId = userLocationRequest.userId,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    dateCreated = Date()
            )
        }
    }

}