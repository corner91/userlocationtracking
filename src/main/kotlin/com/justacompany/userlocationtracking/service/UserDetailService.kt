package com.justacompany.userlocationtracking.service

import com.justacompany.userlocationtracking.domain.UserDetail
import com.justacompany.userlocationtracking.exception.UserAlreadyRegisteredException
import com.justacompany.userlocationtracking.periphery.SignInResponse
import com.justacompany.userlocationtracking.periphery.UserDetailLoginRequest
import com.justacompany.userlocationtracking.periphery.UserDetailRegisterRequest
import com.justacompany.userlocationtracking.repository.UserDetailRepository
import com.sun.mail.smtp.SMTPMessage
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service
import java.util.*
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.mail.Message
import com.sun.org.apache.xml.internal.serializer.utils.Utils.messages




@Service
class UserDetailService(
    private val userDetailRepository: UserDetailRepository
) {

    companion object {
        private const val salt = "bbnerfgaFEFvtr@454FRFRs!!dwrfwFWEf34@f42"
    }

    fun register(userDetailRegisterRequest: UserDetailRegisterRequest): SignInResponse {
        if (!checkIfEmailAlreadyPresent(userDetailRegisterRequest.email))
            return SignInResponse(isSignedIn = false)
        userDetailRepository.save(makeUserDetailFromUserDetailRequest(userDetailRegisterRequest))
        return SignInResponse(isSignedIn = true)
    }

    fun login(userDetailLoginRequest: UserDetailLoginRequest): Boolean {
        if (userDetailRepository.findByEmailAndPassword(
                        email = userDetailLoginRequest.email.toLowerCase(),
                        password = Base64.getEncoder().encodeToString(userDetailLoginRequest.password.toByteArray()) +
                                Base64.getEncoder().encodeToString(salt.toByteArray())
                ) != null) {
            return true
        }
        return false
    }

    fun checkIfEmailAlreadyPresent(email: String): Boolean {
        if (userDetailRepository.findByEmail(email.toLowerCase()) != null) {
            return false
        }

        return true
    }

    private fun makeUserDetailFromUserDetailRequest(userDetailRegisterRequest: UserDetailRegisterRequest): UserDetail {
        return UserDetail(
                email = userDetailRegisterRequest.email.toLowerCase(),
                password = Base64.getEncoder().encodeToString(userDetailRegisterRequest.password.toByteArray()) +
                        Base64.getEncoder().encodeToString(salt.toByteArray()),
                phoneNumber = userDetailRegisterRequest.phoneNumber,
                dateCreated = Date()
        )
    }

}