package com.justacompany.userlocationtracking.service

import com.justacompany.userlocationtracking.domain.UserStatus
import com.justacompany.userlocationtracking.periphery.UserStatusRequest
import com.justacompany.userlocationtracking.properties.IssueType
import com.justacompany.userlocationtracking.repository.UserStatusRepository
import org.springframework.stereotype.Service

@Service
class UserStatusService(
    private val userStatusRepository: UserStatusRepository
) {

    fun postUserStatus(userStatusRequest: UserStatusRequest) {
        userStatusRepository.save(makeUserStatusFromUserStatusRequest(userStatusRequest))
    }

    fun checkIfUserIsAffected(userId: String): Boolean {
        val affectedUsers = userStatusRepository.findAllByUserIdAndRaiseAlarm(
                userId = userId,
                raiseAlarm = true
        )

        if (affectedUsers.isNotEmpty()) {
            return true
        }

        return false
    }

    private fun makeUserStatusFromUserStatusRequest(userStatusRequest: UserStatusRequest): UserStatus {
        return UserStatus(
                userId = userStatusRequest.userId,
                raiseAlarm = userStatusRequest.raiseAlarm
        )
    }

}