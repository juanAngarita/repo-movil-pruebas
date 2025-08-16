package com.example.twitterfalso.data.local

import com.example.twitterfalso.R
import com.example.twitterfalso.data.TweetInfo

// Base de datos local de tweets
object LocalTweetsProvider {

    // Lista local de tweets
    val tweets = listOf(
        TweetInfo(
            id = "1",
            profileImage = "https://midu.dev/advantage-twitch.webp",
            name = "Juan Pérez",
            username = "@juanp",
            content = "¡Hoy es un gran día para programar en Kotlin! 🚀",
            time = "2h",
            retweets = 15,
            comments = 8,
            likes = 120,
            userId = "1"
        ),
        TweetInfo(
            id = "2",
            profileImage = "https://midu.dev/advantage-twitch.webp",
            name = "Ana Gómez",
            username = "@anagomez",
            content = "¿Alguien más ama usar Compose para interfaces en Android? 💙",
            time = "3h",
            retweets = 22,
            comments = 10,
            likes = 180,
            userId = "1"
        ),
        TweetInfo(
            id = "3",
            profileImage = "https://midu.dev/advantage-twitch.webp",
            name = "Carlos Rodríguez",
            username = "@carlitos_dev",
            content = "No hay mejor sensación que solucionar un bug después de horas de debugging 😅",
            time = "5h",
            retweets = 30,
            comments = 14,
            likes = 210,
            userId = "1"
        ),
        TweetInfo(
            id = "4",
            profileImage = "https://midu.dev/advantage-twitch.webp",
            name = "María López",
            username = "@marialpz",
            content = "Aprendiendo sobre corrutinas en Kotlin. ¡Qué maravilla! 😍",
            time = "6h",
            retweets = 18,
            comments = 7,
            likes = 95,
            userId = "1"
        ),
        TweetInfo(
            id = "5",
            profileImage = "https://midu.dev/advantage-twitch.webp",
            name = "Juan Pérez",
            username = "@juanp",
            content = "¡Hoy es un gran día para programar en Kotlin! 🚀",
            time = "2h",
            retweets = 15,
            comments = 8,
            likes = 120,
            userId = "1"
        ),
        TweetInfo(
            id = "6",
            profileImage = "https://midu.dev/advantage-twitch.webp",
            name = "Ana Gómez",
            username = "@anagomez",
            content = "¿Alguien más ama usar Compose para interfaces en Android? 💙",
            time = "3h",
            retweets = 22,
            comments = 10,
            likes = 180,
            userId = "1"
        ),
        TweetInfo(
            id = "7",
            profileImage = "https://midu.dev/advantage-twitch.webp",
            name = "Carlos Rodríguez",
            username = "@carlitos_dev",
            content = "No hay mejor sensación que solucionar un bug después de horas de debugging 😅",
            time = "5h",
            retweets = 30,
            comments = 14,
            likes = 210,
            userId = "1"
        ),
        TweetInfo(
            id = "8",
            profileImage = "https://midu.dev/advantage-twitch.webp",
            name = "María López",
            username = "@marialpz",
            content = "Aprendiendo sobre corrutinas en Kotlin. ¡Qué maravilla! 😍",
            time = "6h",
            retweets = 18,
            comments = 7,
            likes = 95,
            userId = "1"
        )
    )
}