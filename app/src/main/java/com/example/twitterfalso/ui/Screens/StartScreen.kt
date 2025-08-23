package com.example.twitterfalso.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twitterfalso.R
import com.example.twitterfalso.ui.theme.TwitterFalsoTheme
import com.example.twitterfalso.ui.utils.AppButton
import com.example.twitterfalso.ui.utils.BackgroundImage
import com.example.twitterfalso.ui.utils.LogoApp

@Composable
fun MensajeBienvenida(
    nombre: String,
    modifier: Modifier = Modifier
) {
    Text(
        stringResource(R.string.bienvenido_a) + " $nombre",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.blue_twitter),
        modifier = modifier
    )
}

@Composable
fun ExternalLogo(
    idImage: Int,
    description: String,
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(idImage),
        contentDescription = description,
        modifier = modifier
            .padding(8.dp)
            .height(40.dp)
            .width(40.dp)
    )
}

@Composable
@Preview(showBackground = true)
fun ExternalLogoPreview() {
    ExternalLogo(R.drawable.facebook_logo, stringResource(R.string.logo_facebook))
}

// row, column, box
@Composable
fun BodyHomeScreen(
    loginButtonPressed: () -> Unit,
    registerButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        LogoApp()
        MensajeBienvenida(
            stringResource(R.string.app_name),
            modifier = Modifier.padding(16.dp)
        )
        AppButton(
            stringResource(R.string.iniciar_sesion),
            modifier = Modifier.padding(bottom = 4.dp),
            onClick = loginButtonPressed

        )
        AppButton(
            stringResource(R.string.crear_cuenta),
            modifier = Modifier.padding(bottom = 16.dp),
            onClick = registerButtonPressed

        )
        Row {
            ExternalLogo(R.drawable.facebook_logo, stringResource(R.string.logo_facebook))
            ExternalLogo(R.drawable.instagram_logo, stringResource(R.string.logo_instagram))
            ExternalLogo(R.drawable.github_logo, stringResource(R.string.logo_github))
            ExternalLogo(R.drawable.google_logo, stringResource(R.string.logo_google))
        }

    }
}

@Composable
@Preview(showBackground = true)
fun BodyHomeScreenPreview() {
    BodyHomeScreen(
        loginButtonPressed = {},
        registerButtonPressed = {}
    )
}

@Composable
fun StartScreen(
    loginButtonPressed: () -> Unit,
    registerButtonPressed: () -> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
    ){
        BackgroundImage()
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()

        ) {
            Spacer(modifier = Modifier.weight(1F))
            BodyHomeScreen(
                loginButtonPressed = loginButtonPressed,
                registerButtonPressed = registerButtonPressed
            )
            Spacer(modifier = Modifier.weight(1F))
            Text(
                stringResource(R.string.twitter_all_right_reserved)
            )
        }

    }

}


@Composable
@Preview(showBackground = true)
fun MensajeBienvenidaPreview() {
    MensajeBienvenida(stringResource(R.string.app_name))
}

@Composable
@Preview(showBackground = true)
fun LogoAppPreview() {
    LogoApp()
}


@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    TwitterFalsoTheme {
        StartScreen(
            loginButtonPressed = {},
            registerButtonPressed = {}
        )
    }
}