package com.justacompany.userlocationtracking.periphery

data class UserLocationRequest(
        val userId: String,
        val userCoordinates: List<UserCoordinate>
)