package com.justacompany.userlocationtracking.domain

import com.justacompany.userlocationtracking.properties.IssueType
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class UserStatus (
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long? = null,

        @Column(nullable = false)
        val userId: String,

        @Column(nullable = false)
        val raiseAlarm: Boolean = false
)