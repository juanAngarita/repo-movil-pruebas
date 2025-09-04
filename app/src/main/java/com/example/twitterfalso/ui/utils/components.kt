package com.example.twitterfalso.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.twitterfalso.R

@Composable
fun LogoApp(
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(R.drawable.logo_twitter),
        contentDescription = stringResource(R.string.logo_twitter),
        modifier = modifier
    )
}

@Composable
fun AppButton(
    textoBoton: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.blue_twitter)
        ),

        modifier = modifier
    ) {
        Text(textoBoton, fontSize = 20.sp)
    }
}

@Composable
fun ProfileAsyncImage(
    profileImage: String,
    size: Int,
    modifier: Modifier = Modifier
){

    AsyncImage(
        contentDescription = "User Image",
        model = ImageRequest.Builder(LocalContext.current)
            .data(profileImage)
            .crossfade(true)
            .build(),
        error = painterResource(id = R.drawable.user_image_icon),
        placeholder = painterResource(id = R.drawable.loading_img),
        contentScale = ContentScale.Crop,
        modifier = modifier.size(size.dp).clip(CircleShape)
    )
}