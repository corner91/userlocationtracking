package com.justacompany.userlocationtracking.repository

import com.justacompany.userlocationtracking.domain.UserLocation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserLocationRepository: JpaRepository<UserLocation, Long> {
    fun findAllByUserId(userId: String): List<UserLocation>

    fun findAllByUserIdIn(userIds: List<String>): List<UserLocation>
}