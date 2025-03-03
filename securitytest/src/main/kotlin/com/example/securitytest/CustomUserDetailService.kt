package com.example.securitytest

import com.example.securitytest.dto.CustomUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class CustomUserDetailService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val userData = username?.let { userRepository.findByUsername(it) }
            ?: throw UsernameNotFoundException("User not found with username: $username")
        return CustomUserDetails(userData)
    }
}