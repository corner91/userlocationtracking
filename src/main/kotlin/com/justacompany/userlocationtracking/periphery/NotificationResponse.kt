package com.justacompany.userlocationtracking.periphery

data class NotificationResponse (
        val raiseAlarm: Boolean,
        val alreadyAffected: Boolean,
        val severityLevel: Int
)