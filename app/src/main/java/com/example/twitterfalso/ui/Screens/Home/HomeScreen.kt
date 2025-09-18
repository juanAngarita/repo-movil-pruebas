package com.example.twitterfalso.ui.Screens.Home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.twitterfalso.data.TweetInfo
import com.example.twitterfalso.data.local.LocalTweetsProvider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twitterfalso.ui.utils.Tweet

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateCreateTweet: () -> Unit,
    onTweetProfileImageClicked: (String) -> Unit,
    onTweetClicked: (String) -> Unit,
    onTweetReplyClicked: (String) -> Unit,
    modifier: Modifier = Modifier.testTag("homeScreen")
){

    val state by homeViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.getAllTweets()
    }



    when{
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        
        state.errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.errorMessage ?: "Error desconocido")
            }
        }

        else -> {
            Box(
                modifier = modifier
            ){
                LazyColumn(
                    modifier = Modifier
                ) {
                    items(state.tweets.size) { index ->
                        TweetCard(
                            tweetInfo = state.tweets[index],
                            onTweetClicked = onTweetClicked,
                            onTweetReplyClicked =  onTweetReplyClicked,
                            onTweetProfileImageClicked = onTweetProfileImageClicked
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                FloatingButtonAddTwitter(
                    onClick = {
                        homeViewModel.onExpandedChange()
                    },
                    expanded = state.expanded,
                    modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd),
                    navigateCreateTweet = navigateCreateTweet
                )
            }
        }
    }




}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    HomeScreen(
        homeViewModel = viewModel(),
        onTweetClicked = {
            println("Click")
        },
        navigateCreateTweet = {
            println("Click")
        },
        onTweetReplyClicked = {
            println("Click")
        },
        onTweetProfileImageClicked = {
            println("Click")
        }
    )
}


@Composable
fun TweetCard(
    onTweetClicked: (String) -> Unit,
    onTweetProfileImageClicked: (String) -> Unit,
    onTweetReplyClicked: (String) -> Unit,
    tweetInfo: TweetInfo,
    modifier: Modifier = Modifier
){
    OutlinedCard(
        border = BorderStroke(1.dp, Color.Gray),
        onClick = {onTweetClicked(tweetInfo.id)},
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)

    ){
        Tweet(
            tweetInfo = tweetInfo,
            onTweetReplyClicked = onTweetReplyClicked,
            onTweetProfileImageClicked = onTweetProfileImageClicked
        )
    }
}

@Preview
@Composable
fun TweetCardPreview() {
    val example = LocalTweetsProvider.tweets[0]
    TweetCard(
        tweetInfo = example,
        onTweetClicked = {
            println("Click")
        },
        onTweetReplyClicked = {
            println("Click")
        },
        onTweetProfileImageClicked = {
            println("Click")
        }

    )
}

@Composable
fun FloatingButtonAddTwitter(
    expanded: Boolean,
    onClick: () -> Unit,
    navigateCreateTweet: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
    ) {

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(
                onClick = navigateCreateTweet,
                modifier = Modifier.size(32.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape= CircleShape
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "Add",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(
                onClick = onClick,
                modifier = Modifier.size(32.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape= CircleShape
            ) {
                Text("A2")
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(
                onClick = onClick,
                modifier = Modifier.size(32.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape= CircleShape
            ) {
                Text("A3")
            }
        }


        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(48.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape= CircleShape
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Agregar Tweet"
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun FloatingButtonAddTwitterPreview() {
    FloatingButtonAddTwitter(
        onClick = {
            println("Click")
        },
        expanded = true,
        navigateCreateTweet = {
            println("Click")
        }
    )
}

