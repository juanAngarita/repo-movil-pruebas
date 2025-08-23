package com.example.twitterfalso.ui.Screens.TweetDetail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twitterfalso.data.local.LocalTweetsProvider
import com.example.twitterfalso.ui.utils.Tweet

@Composable
fun TweetDetailScreen(
    tweetId: String,
    tweetDetailViewModel: TweetDetailViewModel,
    onTweetProfileImageClicked: (String) -> Unit,
    onTweetReplyClicked: (String) -> Unit,
    onTweetDetailClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val state by tweetDetailViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        tweetDetailViewModel.getTweetById(tweetId)
        tweetDetailViewModel.getTweetRespose(tweetId)
    }

    if(state.tweet == null){
        Text("Tweet no encontrado")
    } else {
        LazyColumn (
            modifier = modifier
        ) {
            item{
                Tweet(
                    tweetInfo = state.tweet!!,
                    onTweetReplyClicked = {onTweetReplyClicked(tweetId)},
                    onTweetProfileImageClicked = onTweetProfileImageClicked,
                )
                HorizontalDivider(thickness = 1.dp)
                TweetActionBar(
                    onReply = { /*TODO*/ },
                    onRetweet = { /*TODO*/ },
                    onLike = {
                        tweetDetailViewModel.sendOrDeleteTweetLike(tweetId, state.currentUserId)
                    },
                    onSave = { /*TODO*/ },
                    onShare = { /*TODO*/ },
                    isLiked = state.tweet?.liked ?: false
                )
                HorizontalDivider(thickness = 1.dp)
                Text(
                    "Respuestas mas relevantes",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
                )
            }
            items(state.resposeTweets.size) { index ->
                Tweet(
                    tweetInfo = state.resposeTweets[index],
                    modifier = Modifier.padding(top = 6.dp, bottom = 6.dp).clickable(onClick = {
                        onTweetDetailClicked(state.resposeTweets[index].id)
                    }),
                    onTweetReplyClicked = onTweetReplyClicked,
                    onTweetProfileImageClicked = onTweetProfileImageClicked
                )
                HorizontalDivider(thickness = 1.dp)
            }
        }
    }



}

@Preview(showBackground = true)
@Composable
fun TweetDetailScreenPreview() {

    val tweet = LocalTweetsProvider.tweets[0]
    val responseTweets = LocalTweetsProvider.tweets

    TweetDetailScreen(
        tweetId = tweet.id,
        tweetDetailViewModel = viewModel(),
        onTweetReplyClicked = {},
        onTweetProfileImageClicked = {},
        onTweetDetailClicked = {}
    )
}

@Composable
fun TweetActionBar(
    onReply: () -> Unit,
    onRetweet: () -> Unit,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onShare: () -> Unit,
    isLiked: Boolean
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        IconButton(
            onClick = onReply
        ) {
            Icon(
                imageVector = Icons.Outlined.Comment,
                contentDescription = "Reply"
            )
        }
        IconButton(onClick = onRetweet) {
            Icon(imageVector = Icons.Outlined.Repeat, contentDescription = "Retweet")
        }
        IconButton(onClick = onLike) {
            if(isLiked) Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Me gusta", tint = MaterialTheme.colorScheme.primary)
            else Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "Me gusta")
        }
        IconButton(onClick = onSave) {
            Icon(imageVector = Icons.Outlined.Bookmarks, contentDescription = "Guardar")
        }
        IconButton(onClick = onShare) {
            Icon(imageVector = Icons.Outlined.Share, contentDescription = "Compartir")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TweetActionBarPreview() {
    TweetActionBar(
        onReply = {},
        onRetweet = {},
        onLike = {},
        onSave = {},
        onShare = {},
        isLiked = true
    )
}