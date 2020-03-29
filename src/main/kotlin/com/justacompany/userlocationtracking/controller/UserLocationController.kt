package com.justacompany.userlocationtracking.controller

import com.justacompany.userlocationtracking.periphery.NotificationResponse
import com.justacompany.userlocationtracking.periphery.UserLocationRequest
import com.justacompany.userlocationtracking.service.UserLocationService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user-locations")
class UserLocationController(
        private val userLocationService: UserLocationService
) {

    @PostMapping
    fun postUserLocation(@RequestBody userLocationRequest: UserLocationRequest) {
        userLocationService.postUserLocation(userLocationRequest)
    }

    @GetMapping("/notifications/{userId}")
    fun getNotificationForUser(@PathVariable userId: String): NotificationResponse {
        return userLocationService.getNotificationForUser(userId)
    }
}