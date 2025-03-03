package com.example.securitytest

import com.example.securitytest.dto.CustomUserDetails
import com.example.securitytest.jwt.JWTUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/user/manage")
class UserManageController (
    private val authenticationManager: AuthenticationManager,
    private val userManageService: UserManageService,
    private val jwtUtil: JWTUtil
){

    @PostMapping("/signup")
    fun signup(@RequestBody userSignupRequest: UserSignupRequest): ResponseEntity<String> {
        println("===회원가입 호출===")
        userManageService.signup(userSignupRequest)
        return ResponseEntity.ok("회원가입 성공")
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        println("===로그인 호출===")
        return try {
            // 입력받은 자격증명을 이용해 인증 객체 생성
            val authToken = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
            // 인증 매니저를 통해 인증 진행
            val authentication = authenticationManager.authenticate(authToken)
            // SecurityContext에 인증 객체 저장
            SecurityContextHolder.getContext().authentication = authentication

            // 인증 성공 시, 사용자 정보를 가져와 JWT 생성 (여기선 첫 번째 권한을 role로 사용)
            val userDetails = authentication.principal as CustomUserDetails
            // 예시로 만료시간 1시간(3600000ms)로 설정
            val jwt = jwtUtil.createJwt(userDetails.username!!, userDetails.authorities.first().authority, 3600000)
            ResponseEntity.ok(mapOf("token" to jwt))
        } catch (e: Exception) {
            // 인증 실패 시 401 상태 반환
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password")
        }
    }
}