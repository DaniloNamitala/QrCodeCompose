package com.nepreconsultintg.edigital.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val Document: String,
    val Password: String
)

@Serializable
data class LoginResponse(
    val user : User?,
    val token : String
)

@Serializable
data class User(
    val id : Int?,
    val username : String?,
    val email : String?,
    val name : String?,
    val document: String?,
    val role : List<Int>?
)