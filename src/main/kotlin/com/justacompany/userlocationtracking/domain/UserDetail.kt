package com.justacompany.userlocationtracking.domain

import java.util.*
import javax.persistence.*

@Entity
data class UserDetail (
       @Id
       @GeneratedValue(strategy = GenerationType.SEQUENCE)
       val userId: String,

       @Column
       val phoneNumber: String? = null,

       @Column
       val password: String? = null,

       @Column
       val dateCreated: Date
)