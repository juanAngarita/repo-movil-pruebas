package com.example.twitterfalso.ui.Screens.SearchUser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.twitterfalso.data.UserProfileInfo
import com.example.twitterfalso.ui.utils.ProfileAsyncImage
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchUserScreen(
    viewModel: SearchUserViewModel,
    searchQuery: String
) {
    val state by viewModel.uistate.collectAsState()

    // Cada vez que cambie el query externo, lo pasamos al VM
    LaunchedEffect(searchQuery) {
        viewModel.observeQuery(flowOf(searchQuery))
    }

    LazyColumn {
        items(state.users) { user ->
            UserItem(
                user = user,
                modifier = Modifier.padding(16.dp)
            )
        }
    }


    Text(searchQuery)
}

@Composable
fun UserItem(
    user: UserProfileInfo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileAsyncImage(
            user.profileImage ?: "",
            size = 40
        )
        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(user.name, fontWeight = FontWeight.Bold)
            Text(user.username, color = Color.Gray)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun UserItemPreview() {
    UserItem(
        user = UserProfileInfo(
            id = "1",
            username = "juansebas",
            name = "Juan Sebastian",
            bio = "Bio",
            location = "",
            website = "",
            profileImage = "",
            birthDate = "",
            followers = 0,
            following = 0,
            backgroundImage = "",
            accountCreationDate = "",
        )
    )



}