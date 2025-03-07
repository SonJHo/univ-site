package com.example.securitytest

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository :JpaRepository<Users, Long> {

    fun findByName ( name :String) : List<Users>
    fun findByUsername ( username :String) : Users?

}