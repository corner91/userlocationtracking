package com.justacompany.userlocationtracking.repository

import com.justacompany.userlocationtracking.domain.UserStatus
import com.justacompany.userlocationtracking.properties.IssueType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserStatusRepository: JpaRepository<UserStatus, Long> {
    fun findAllUserIdByRaiseAlarm(raiseAlarm: Boolean): List<UserStatus>

    fun findAllByUserIdAndRaiseAlarm(userId: String, raiseAlarm: Boolean): List<UserStatus>
}