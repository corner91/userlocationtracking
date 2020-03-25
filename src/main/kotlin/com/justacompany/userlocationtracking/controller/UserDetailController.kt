package com.justacompany.userlocationtracking.controller

import com.justacompany.userlocationtracking.periphery.LoginResponse
import com.justacompany.userlocationtracking.periphery.UserDetailLoginRequest
import com.justacompany.userlocationtracking.periphery.UserDetailRegisterRequest
import com.justacompany.userlocationtracking.service.UserDetailService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/user-details")
class UserDetailController(
        private val userDetailService: UserDetailService
) {

    @PostMapping
    fun register(@RequestBody userDetailRegisterRequest: UserDetailRegisterRequest) {
        userDetailService.register(userDetailRegisterRequest)
    }

    @PostMapping("/login")
    fun login(@RequestBody userDetailLoginRequest: UserDetailLoginRequest): LoginResponse {
        return LoginResponse(userDetailService.login(userDetailLoginRequest))
    }

    /*@GetMapping("/password/{email}")
    fun rememberPassword(@PathVariable email: String) {
        userDetailService.sendRememberPasswordEmail(email = email)
    }*/
}