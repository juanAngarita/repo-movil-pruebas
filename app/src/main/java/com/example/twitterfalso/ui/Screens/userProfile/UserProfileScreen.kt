package com.example.twitterfalso.ui.Screens.userProfile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.twitterfalso.R
import com.example.twitterfalso.data.UserProfileInfo
import com.example.twitterfalso.data.local.LocalUsersProvider
import com.example.twitterfalso.ui.theme.TwitterFalsoTheme
import com.example.twitterfalso.ui.utils.ProfileAsyncImage
import com.example.twitterfalso.ui.utils.Tweet

@Composable
fun UserProfileScreen(
    userId: String,
    userProfileViewModel: UserProfileViewModel,
    onTweetProfileImageClicked: (String) -> Unit,
    onTweetEditClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    showEdit: Boolean = false
){

    val state by userProfileViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        userProfileViewModel.getUserProfile(userId)
        userProfileViewModel.getUserTweets(userId)
    }

    Column(
        modifier = modifier
    ) {
        LazyColumn (
            modifier = modifier
        ) {
            item{
                UserProfile(
                    userProfileInfo = state.user,
                    onFollowClicked = {
                        userProfileViewModel.followOrUnfollowUser(userId)
                        Log.d("UserProfileScreen", "El usuario ${state.currentUserId} esta tratando de seguir a $userId")
                    },
                    followed = state.user.followed
                )
                HorizontalDivider(thickness = 1.dp)
            }
            items(state.tweets.size) { index ->
                Tweet(
                    tweetInfo = state.tweets[index],
                    modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
                    onTweetReplyClicked = { /*TODO*/ },
                    showEditButton = showEdit,
                    onDeleteClicked = { userProfileViewModel.deleteTweet(state.tweets[index].id) },
                    onEditClicked = onTweetEditClicked,
                    onTweetProfileImageClicked = onTweetProfileImageClicked
                )
                HorizontalDivider(thickness = 1.dp)
            }
        }

    }
}

@Composable
fun UserProfile(
    userProfileInfo: UserProfileInfo,
    onFollowClicked: () -> Unit,
    followed: Boolean,
    modifier: Modifier = Modifier
){

    val isDarkTheme = isSystemInDarkTheme()

    val placeholderRes = if (isDarkTheme) {
        R.drawable.camera_white // tu drawable para dark
    } else {
        R.drawable.camera_black // tu drawable para light
    }

    Box(
        modifier = modifier,
    ){
        Column {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userProfileInfo.backgroundImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "User Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp),
                contentScale = ContentScale.Crop,
                loading = {
                    Image(
                        painter = painterResource(R.drawable.loading_img),
                        contentDescription = null,
                    )
                },
                error = {
                    Image(
                        painter = painterResource(placeholderRes),
                        contentDescription = null,
                    )
                }
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
                    .fillMaxWidth()
            ){
                Button(
                    onClick = onFollowClicked,
                    modifier = Modifier.align(Alignment.End).height(30.dp).padding(end = 8.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp)
                ) {
                    if(followed){
                        Text(text = "Dejar de seguir")
                    } else{
                        Text(text = "Seguir")
                    }

                }
                Text(
                    text = userProfileInfo.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                Text(
                    text = userProfileInfo.username,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = userProfileInfo.bio,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                //ubicacion
                IconData(
                    icon = Icons.Outlined.LocationOn,
                    text = userProfileInfo.location
                )
                //fecha de nacimiento
                IconData(
                    icon = Icons.Outlined.AutoAwesome,
                    text = "Fecha de nacimiento: "+ userProfileInfo.birthDate
                )
                //account creation date
                IconData(
                    icon = Icons.Outlined.CalendarMonth,
                    text = "Se unio en: "+ userProfileInfo.accountCreationDate
                )
                Row(
                    modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
                ) {
                    Follow(
                        number = userProfileInfo.followers,
                        text = "Seguidores"
                    )
                    Follow(
                        number = userProfileInfo.following,
                        text = "Siguiendo"
                    )
                }
            }


        }
        ProfileAsyncImage(
            profileImage = userProfileInfo.profileImage ?: "",
            size = 80,
            modifier = Modifier.padding(top = 85.dp, start = 15.dp).clip(CircleShape) // Primero se recorta en forma circular
                .border(2.dp, MaterialTheme.colorScheme.background, CircleShape) // Luego el borde se aplica en círculo
        )

    }


}

@Composable
fun Follow(
    number: Int,
    text: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
    ) {
        Text(
            text = number.toString(),
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = text,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun UserProfilePreview() {
    val user = LocalUsersProvider.users[0]
    TwitterFalsoTheme {
        UserProfile(userProfileInfo = user, onFollowClicked = {}, followed = false)
    }
}

@Composable
@Preview(showBackground = true)
fun UserProfilePreviewDark() {
    val user = LocalUsersProvider.users[0]
    TwitterFalsoTheme(
        darkTheme = true
    ) {
        UserProfile(userProfileInfo = user, onFollowClicked = {}, followed = false)
    }
}

@Composable
fun IconData(
    icon:ImageVector,
    text:String,
){
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 4.dp).size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
