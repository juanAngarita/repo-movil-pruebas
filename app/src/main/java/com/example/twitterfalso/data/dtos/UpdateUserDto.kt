package com.example.twitterfalso.data.dtos

data class UpdateUserDto(
    val name: String,
    val pais: String? = null,
    val bio: String? = null,
)
