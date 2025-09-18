package com.example.twitterfalso.ui.Screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twitterfalso.R
import com.example.twitterfalso.ui.theme.TwitterFalsoTheme
import com.example.twitterfalso.ui.utils.AppButton
import com.example.twitterfalso.ui.utils.BackgroundImage
import com.example.twitterfalso.ui.utils.LogoApp

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
){
    
    val state by loginViewModel.uiState.collectAsState()

    var icono = if(!state.mostrarContrasena) R.drawable.hide else R.drawable.see

    Box(
        modifier = modifier.testTag("loginScreen")
    ){

        BackgroundImage()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            LogoApp()
            FormularioRegistro(
                email = state.email,
                password = state.password,
                mostrarPassword = state.mostrarContrasena,
                icono = icono,
                onEmailChange = { loginViewModel.updateEmail(it) },
                onPasswordChange = { loginViewModel.updatePassword(it) },
                onMostrarPasswordChange = { loginViewModel.mostrarEsconderPassword() },
            )
            //1. Si la contraseña es menor a 6 dígitos
            //2. Si el correo es admin@admin.com aparece correo ya registrado
            if(state.mostrarMensaje){
                Text(state.errorMessage)
            }
            AppButton(
                "Registrar",
                onClick = {loginViewModel.registerButtonPressed()},
            )
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview(){
    TwitterFalsoTheme {
        LoginScreen(
            loginViewModel = viewModel(),
        )
    }
}

//state hoisting
@Composable
fun FormularioRegistro(
    email: String ,
    password: String ,
    mostrarPassword: Boolean,
    icono: Int,
    onEmailChange: (String) -> Unit ,
    onPasswordChange: (String) -> Unit ,
    onMostrarPasswordChange: () -> Unit,
    modifier: Modifier = Modifier
){

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(text = stringResource(R.string.email)) },
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(text = stringResource(R.string.contrase_a)) },
            visualTransformation = if(mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = onMostrarPasswordChange) {
                    Icon(
                        painter = painterResource(icono),
                        contentDescription = stringResource(R.string.mostrar_password)
                    )
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun FormularioRegistroPreview(){
    FormularioRegistro(
        email = "",
        password = "",
        onEmailChange = {},
        onPasswordChange = {},
        onMostrarPasswordChange = {},
        mostrarPassword = false,
        icono = R.drawable.hide
    )
}
