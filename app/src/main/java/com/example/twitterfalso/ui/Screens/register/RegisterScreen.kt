package com.example.twitterfalso.ui.Screens.register

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
import java.time.LocalDate

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    modifier: Modifier = Modifier
){

    val state by registerViewModel.uistate.collectAsState()

    var icono = if(!state.mostrarPassword) R.drawable.hide else R.drawable.see

    Box(
        modifier = modifier
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
                name = state.name,
                userName = state.userName,
                pais = state.pais,
                bio = state.bio,
                fechaNacimiento = state.fechaNacimiento,
                password = state.password,
                mostrarPassword = state.mostrarPassword,
                icono = icono,
                onEmailChange = { registerViewModel.updateEmail(it) },
                onPasswordChange = { registerViewModel.updatePassword(it) },
                onMostrarPasswordChange = { registerViewModel.mostrarEsconderPassword() },
                onNameChange = { registerViewModel.updateName(it) },
                onUserNameChange = { registerViewModel.updateUserName(it) },
                onPaisChange = { registerViewModel.updatePais(it) },
                onBioChange = { registerViewModel.updateBio(it) },
                onFechaNacimientoChange = { registerViewModel.updateFechaNacimiento(it) },
            )
            //1. Si la contraseña es menor a 6 dígitos
            //2. Si el correo es admin@admin.com aparece correo ya registrado
            if(state.mostrarMensaje)
                Text(state.errorMessage)
            AppButton(
                "Registrar",
                onClick = {registerViewModel.registerButtonPressed()},
            )
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview(){
    TwitterFalsoTheme {
        RegisterScreen(
            registerViewModel = viewModel(),
        )
    }
}

//state hoisting
@Composable
fun FormularioRegistro(
    email: String = "",
    name: String = "",
    password: String = "",
    userName: String = "",
    pais: String? = null,
    bio: String? = null,
    fechaNacimiento: LocalDate? = null,
    mostrarPassword: Boolean,
    icono: Int,
    onEmailChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit,
    onPaisChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onFechaNacimientoChange: (LocalDate) -> Unit,
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
            value = name,
            onValueChange = onNameChange,
            label = {
                Text(text = "Nombre")
            },
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedTextField(
            value = userName,
            onValueChange = onUserNameChange,
            label = { Text(text = "Nombre de usuario") },
            modifier = Modifier.padding(top = 8.dp)
        )

        OutlinedTextField(
            value = pais ?: "",
            onValueChange = onPaisChange,
            label = { Text(text = "País") },
            modifier = Modifier.padding(top = 8.dp)
        )

        OutlinedTextField(
            value = bio ?: "",
            onValueChange = onBioChange,
            label = { Text(text = "Biografía") },
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
        onNameChange = {},
        onUserNameChange = {},
        onPaisChange = {},
        onBioChange = {},
        onFechaNacimientoChange = {},
        mostrarPassword = false,
        icono = R.drawable.hide
    )
}

