package com.example.twitterfalso.data.local

import com.example.twitterfalso.data.UserProfileInfo

object LocalUsersProvider {

    val users = listOf(
        UserProfileInfo(
            backgroundImage = "https://picsum.photos/800/300?random=1",
            profileImage = "https://picsum.photos/200?random=1",
            name = "Ana Torres",
            username = "anatorres",
            bio = "Amante de la fotografía 📸 y los viajes ✈️.",
            followers = 1200,
            following = 350,
            location = "Bogotá, Colombia",
            website = "https://anatorres.dev",
            birthDate = "1995-03-12",
            accountCreationDate = "2018-06-01",
            id = "1",
            followed = true
        ),
        UserProfileInfo(
            backgroundImage = "https://picsum.photos/800/300?random=2",
            profileImage = "https://picsum.photos/200?random=2",
            name = "Carlos Mendoza",
            username = "carlitos",
            bio = "Desarrollador Android y ciclista 🚴.",
            followers = 890,
            following = 420,
            location = "Medellín, Colombia",
            website = "https://carlosmendoza.me",
            birthDate = "1990-11-05",
            accountCreationDate = "2017-09-15",
            id = "2",
            followed = false
        ),
        UserProfileInfo(
            backgroundImage = "https://picsum.photos/800/300?random=3",
            profileImage = "https://picsum.photos/200?random=3",
            name = "Laura Gómez",
            username = "laurag",
            bio = "Diseñadora UX/UI 🎨 | Minimalismo lover.",
            followers = 1500,
            following = 600,
            location = "Cali, Colombia",
            website = "https://laurag.design",
            birthDate = "1993-07-21",
            accountCreationDate = "2019-01-20",
            id = "3",
            followed = true
        ),
        UserProfileInfo(
            backgroundImage = "https://picsum.photos/800/300?random=4",
            profileImage = "https://picsum.photos/200?random=4",
            name = "David Rodríguez",
            username = "davidro",
            bio = "Músico 🎸 y programador full-stack.",
            followers = 2100,
            following = 980,
            location = "Barranquilla, Colombia",
            website = "https://davidro.com",
            birthDate = "1988-02-18",
            accountCreationDate = "2015-03-10",
            id = "4",
            followed = false
        ),
        UserProfileInfo(
            backgroundImage = "https://picsum.photos/800/300?random=5",
            profileImage = "https://picsum.photos/200?random=5",
            name = "Mariana López",
            username = "marianal",
            bio = "Amante del café ☕, la lectura 📚 y la IA 🤖.",
            followers = 5000,
            following = 1200,
            location = "Cartagena, Colombia",
            website = "https://marianalopez.co",
            birthDate = "1998-09-09",
            accountCreationDate = "2020-12-05",
            id = "5",
            followed = true
        )
    )
}