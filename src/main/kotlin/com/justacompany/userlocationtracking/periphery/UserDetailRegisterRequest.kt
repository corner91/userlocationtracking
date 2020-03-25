package com.justacompany.userlocationtracking.periphery

data class UserDetailRegisterRequest(
        val email: String,
        val password: String,
        val phoneNumber: String? = null
)