package com.justacompany.userlocationtracking.service

import com.justacompany.userlocationtracking.domain.UserDetail
import com.justacompany.userlocationtracking.periphery.UserDetailRequest
import com.justacompany.userlocationtracking.repository.UserDetailRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDetailService(
    private val userDetailRepository: UserDetailRepository
) {

    fun postUserDetail(userDetailRequest: UserDetailRequest) {
        userDetailRepository.save(makeUserDetailFromUserDetailRequest(userDetailRequest))
    }

    fun generateUserId(): String {
        return UUID.randomUUID().toString()
    }

    private fun makeUserDetailFromUserDetailRequest(userDetailRequest: UserDetailRequest): UserDetail {
        return UserDetail(
                userId = userDetailRequest.userId,
                dateCreated = Date()
        )
    }

}