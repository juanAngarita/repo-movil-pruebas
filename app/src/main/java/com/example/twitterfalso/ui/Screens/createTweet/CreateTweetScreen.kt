package com.example.twitterfalso.ui.Screens.createTweet

import PickImageButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twitterfalso.ui.utils.ProfileAsyncImage


@Composable
fun CreateTweetScreen(
    viewModel: CreateTweetViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    responseTweetId: String? = null,
    tweetId: String? = null
) {

    val state by viewModel.uistate.collectAsState()

    LaunchedEffect(Unit) {
        if(tweetId != null){
            viewModel.getTweetById(tweetId ?: "")
        }
    }

    LaunchedEffect(state.navigateBack) {
        if(state.navigateBack){
            onBack()
        }
    }

    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CreateTweetUpperBar(
            onBack = onBack,
            onPublish = {
                viewModel.createTweet(responseTweetId, tweetId)
            }
        )
        TweetContent(
            tweetText = state.content,
            onTweetTextChange = { viewModel.onTweetChange(it) },
            profileImageURL = state.profileImage?:"",
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text("ResponseTweetId id: $responseTweetId")
        Text("tweetId: $tweetId")
        PickImageButton {
            /*Todo*/
        }

    }
}

@Composable
fun TweetContent(
    tweetText: String,
    onTweetTextChange: (String) -> Unit,
    profileImageURL: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically // Centrado vertical
    ) {
        ProfileAsyncImage(
            profileImage = profileImageURL,
            size = 50,
            modifier = Modifier.align(Alignment.Top)
        )
        BasicTextField(
            value = tweetText,
            onValueChange = onTweetTextChange,
            maxLines = 10,
            modifier = Modifier
                .padding(8.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            decorationBox = { innerTextField ->
                if (tweetText.isEmpty()) {
                    Text(text = "¿Que esta pasando?", fontSize = 20.sp)
                }
                innerTextField()
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TweetContentPreview() {
    TweetContent(
        tweetText = "",
        onTweetTextChange = {},
        profileImageURL = "",
    )
}

@Composable
fun CreateTweetUpperBar(
    onBack: () -> Unit,
    onPublish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onBack() },
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Cancel"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onPublish() },
            modifier = Modifier.height(35.dp)
        ) {
            Text("Publicar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpperBarPreview() {
    CreateTweetUpperBar(onBack = {}, onPublish = {})
}