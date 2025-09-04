package com.example.twitterfalso.ui.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BorderColor
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twitterfalso.R
import com.example.twitterfalso.data.TweetInfo

@Composable
fun  Tweet(
    tweetInfo: TweetInfo,
    onTweetReplyClicked: (String) -> Unit,
    onTweetProfileImageClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    showEditButton: Boolean = false,
    onEditClicked: (String) -> Unit = {},
    onDeleteClicked: () -> Unit = {},
){
    Column(
        modifier = modifier.padding(12.dp)
    ) {
        TweetCardHeader(
            name = tweetInfo.name,
            username = tweetInfo.username,
            profileImage = tweetInfo.profileImage,
            modifier = Modifier.padding(bottom = 12.dp),
            showEditButton = showEditButton,
            onEditClicked = { onEditClicked(tweetInfo.id) },
            onDeleteClicked =  onDeleteClicked,
            onTweetReplyClicked = { onTweetReplyClicked(tweetInfo.id) },
            onTweetProfileImageClicked = { onTweetProfileImageClicked(tweetInfo.userId) }
        )
        TweetCardBody(
            content = tweetInfo.content,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        TweetCardFooter(
            likes = tweetInfo.likes,
            comments = tweetInfo.comments,
            retweets = tweetInfo.retweets,
            modifier = Modifier.padding()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TweetPreview() {
    Tweet(
        tweetInfo = TweetInfo(
            profileImage = "",
            name = "Juan Pérez",
            username = "@juanp",
            content = "¡Hoy es un gran día para programar en Kotlin! 🚀",
            time = "2h",
            retweets = 15,
            comments = 8,
            likes = 120,
            id = "1",
            userId = "1"
        ),
        onTweetReplyClicked = {},
        onTweetProfileImageClicked = {}
    )
}


@Composable
fun TweetCardHeader(
    name: String,
    username: String,
    profileImage: String,
    onTweetReplyClicked: () -> Unit,
    onTweetProfileImageClicked: () -> Unit,
    showEditButton: Boolean = false,
    onEditClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
    ) {
        ProfileAsyncImage(
            profileImage = profileImage,
            size = 40,
            modifier = Modifier.padding(end = 8.dp).clickable { onTweetProfileImageClicked() },
        )
        Column {
            Text(
                name,
                fontWeight = FontWeight.Bold,
            )
            Text(
                username,
                color = Color.Gray,
                fontSize = 12.sp,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (showEditButton) {
            IconButton(
                onClick = { onEditClicked() },
                modifier = Modifier.padding(horizontal = 2.dp).background(
                    color = MaterialTheme.colorScheme.primary, // Azul estilo Twitter o cambia al color que prefieras
                    shape = CircleShape
                ).size(25.dp),
            ) {
                Icon(
                    Icons.Outlined.Code,
                    contentDescription = "Reply",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(20.dp)

                )
            }
            IconButton(
                onClick = { onDeleteClicked() },
                modifier = Modifier.padding(horizontal = 2.dp).background(
                    color = MaterialTheme.colorScheme.primary, // Azul estilo Twitter o cambia al color que prefieras
                    shape = CircleShape
                ).size(25.dp),
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(20.dp)

                )
            }
        }

        IconButton(
            onClick = { onTweetReplyClicked() },
            modifier = Modifier.padding(horizontal = 2.dp).background(
                color = MaterialTheme.colorScheme.primary, // Azul estilo Twitter o cambia al color que prefieras
                shape = CircleShape
            ).size(25.dp),
        ) {
            Icon(
                Icons.Outlined.Create,
                contentDescription = "Reply",
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(20.dp)

            )
        }


    }
}

@Preview(showBackground = true)
@Composable
fun TweetCardHeaderPreview() {

    TweetCardHeader(
        name = "Juan Pérez",
        username = "@juanp",
        profileImage = "",
        onTweetReplyClicked = {},
        onTweetProfileImageClicked = {}
    )
}

@Composable
fun TweetCardBody(
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            content
        )
        Text(
            "8:26 PM * Dec 1, 2022",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TweetCardBodyPreview() {
    TweetCardBody(
        "Lorem Ipsum Dolor Sit Amet"
    )
}

@Composable
fun TweetCardFooterItem(
    cantidad: Int,
    @StringRes label: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(end = 8.dp)
    ) {
        Text(
            cantidad.toString(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(stringResource(label))
    }
}

@Preview(showBackground = true)
@Composable
fun TweetCardFooterItemPreview() {
    TweetCardFooterItem(
        cantidad = 1000,
        label = R.string.retweets
    )
}

@Composable
fun TweetCardFooter(
    retweets: Int,
    likes: Int,
    comments: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        TweetCardFooterItem(
            retweets,
            R.string.retweets
        )
        TweetCardFooterItem(
            likes,
            R.string.likes
        )
        TweetCardFooterItem(
            comments,
            R.string.comments
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TweetCardFooterPreview() {
    TweetCardFooter(
        retweets = 1000,
        likes = 1000,
        comments = 1000
    )
}
