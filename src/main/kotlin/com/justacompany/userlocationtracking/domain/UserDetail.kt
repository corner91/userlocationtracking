package com.justacompany.userlocationtracking.domain

import java.util.*
import javax.persistence.*

@Entity
data class UserDetail (
       @Id
       @GeneratedValue(strategy = GenerationType.SEQUENCE)
       val id: Long? = null,

       @Column
       val email: String,

       @Column
       val password: String,

       @Column
       val phoneNumber: String? = null,

       @Column
       val dateCreated: Date
)