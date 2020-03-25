package com.justacompany.userlocationtracking.periphery

import com.justacompany.userlocationtracking.properties.AlarmSeverity

data class NotificationResponse (
        val raiseAlarm: Boolean,
        val alarmSeverity: AlarmSeverity
)