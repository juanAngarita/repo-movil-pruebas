package com.example.twitterfalso.data.dtos

import com.example.twitterfalso.data.UserProfileInfo
abstract class UserDtoGeneric{
    abstract fun toUserProfileInfo(): UserProfileInfo
}


data class UserProfileFirestoreDto(
    val id: String,
    val username: String,
    val email: String,
    val name: String,
    val bio: String?,
    val pais: String?,
    val website: String?,
    val profileImage: String?,
    val coverImage: String?,
    val birthDate: String, // o LocalDate si lo vas a deserializar como fecha
    val verified: Boolean,
    val followersCount: Int,
    val followingCount: Int,
    val createdAt: String, // o Instant / LocalDateTime si parseas fechas
    val updatedAt: String,  // igual que createdAt
    var followed: Boolean = false
) : UserDtoGeneric(){
    constructor(): this("", "", "", "", "", "", "", null, "", "", false, 0, 0, "", "")

    override fun toUserProfileInfo(): UserProfileInfo {
        return UserProfileInfo(
            id = id,
            username = username,
            name = name,
            bio = bio ?: "No hay biografia",
            location = pais ?: "No hay ubicación",
            website = website ?: "",
            profileImage = profileImage,
            birthDate = birthDate,
            followers = followersCount,
            following = followingCount,
            backgroundImage = coverImage,
            accountCreationDate = createdAt,
            followed = followed
        )
    }
}

data class UserProfileRetrofitDto(
    val id: Int,
    val username: String,
    val email: String,
    val name: String,
    val bio: String?,
    val location: String?,
    val website: String,
    val profileImage: String,
    val coverImage: String,
    val birthDate: String, // o LocalDate si lo vas a deserializar como fecha
    val verified: Boolean,
    val followersCount: Int,
    val followingCount: Int,
    val createdAt: String, // o Instant / LocalDateTime si parseas fechas
    val updatedAt: String  // igual que createdAt
) : UserDtoGeneric(){
    constructor(): this(0, "", "", "", "", "", "", "", "", "", false, 0, 0, "", "")

    override fun toUserProfileInfo(): UserProfileInfo {
        return UserProfileInfo(
            id = id.toString(),
            username = username,
            name = name,
            bio = bio ?: "No hay biografia",
            location = location ?: "No hay ubicación",
            website = website,
            profileImage = profileImage,
            birthDate = birthDate,
            followers = followersCount,
            following = followingCount,
            backgroundImage = coverImage,
            accountCreationDate = createdAt,
        )
    }
}


