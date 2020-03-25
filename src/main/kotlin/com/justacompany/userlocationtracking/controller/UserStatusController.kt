package com.justacompany.userlocationtracking.controller

import com.justacompany.userlocationtracking.periphery.UserAffectedStatusResponse
import com.justacompany.userlocationtracking.periphery.UserStatusRequest
import com.justacompany.userlocationtracking.service.UserStatusService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user-statuses")
class UserStatusController(
        private val userStatusService: UserStatusService
) {

    @PostMapping
    fun postUserStatus(@RequestBody userStatusRequest: UserStatusRequest) {
        userStatusService.postUserStatus(userStatusRequest)
    }

    @GetMapping("/{userId}")
    fun checkIfUserAffected(@PathVariable userId: String): UserAffectedStatusResponse {
        return UserAffectedStatusResponse(userStatusService.checkIfUserIsAffected(userId))
    }
}