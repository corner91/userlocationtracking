package com.justacompany.userlocationtracking.repository

import com.justacompany.userlocationtracking.domain.UserDetail
import com.justacompany.userlocationtracking.domain.UserStatus
import com.justacompany.userlocationtracking.properties.IssueType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDetailRepository: JpaRepository<UserDetail, String>