package com.justacompany.userlocationtracking.domain

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class UserLocation (
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long? = null,

        @Column(nullable = false)
        val userId: String,

        @Column(nullable = false)
        val latitude: Double,

        @Column(nullable = false)
        val longitude: Double,

        @Column(nullable = false)
        val dateCreated: Date
)