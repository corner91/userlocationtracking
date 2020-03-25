package com.justacompany.userlocationtracking.periphery

import com.justacompany.userlocationtracking.properties.IssueType
import javax.persistence.Entity
import javax.persistence.Id

data class UserStatusRequest (
        val id: Long? = null,
        val userId: String,
        val raiseAlarm: Boolean = false
)