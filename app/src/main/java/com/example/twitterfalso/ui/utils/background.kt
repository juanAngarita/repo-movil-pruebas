package com.example.twitterfalso.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.twitterfalso.R

@Composable
fun BackgroundImage(
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(R.drawable.fondo_pantalla),
        contentDescription = stringResource(R.string.fondo_twitter),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}