package com.example.twitterfalso.data.dtos

data class RegisterUserDto(
    val username: String,
    val name: String,
    val pais: String? = null,
    val bio: String? = null,
    val FCMToken: String
)
