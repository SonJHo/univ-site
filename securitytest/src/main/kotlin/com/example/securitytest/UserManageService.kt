package com.example.securitytest

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class UserManageService (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
){

    fun signup(request: UserSignupRequest){
        if(userRepository.findByUsername(request.username) != null){
            throw Exception("중복 회원")
        }

        val users = Users(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            roles = ("MEMBER")
        )
        userRepository.save(users)
    }
}