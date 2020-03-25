package com.justacompany.userlocationtracking.controller

import com.justacompany.userlocationtracking.periphery.UserDetailRequest
import com.justacompany.userlocationtracking.periphery.UserStatusRequest
import com.justacompany.userlocationtracking.service.UserDetailService
import com.justacompany.userlocationtracking.service.UserStatusService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user-details")
class UserDetailController(
        private val userDetailService: UserDetailService
) {

    @PostMapping
    fun postUserDetail(@RequestBody userDetailRequest: UserDetailRequest) {
        userDetailService.postUserDetail(userDetailRequest)
    }

    @GetMapping("/userId")
    fun generateUserId(): String {
        return userDetailService.generateUserId()
    }
}