package com.example.twitterfalso.ui.Screens.updateProfile

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.twitterfalso.R
import com.example.twitterfalso.data.local.LocalUsersProvider
import com.example.twitterfalso.ui.theme.TwitterFalsoTheme
import com.example.twitterfalso.ui.utils.ProfileAsyncImage

@Composable
fun UpdateProfileScreen(
    updateProfileViewModel: UpdateProfileViewModel,
    modifier: Modifier = Modifier.testTag("updateProfileScreen")
){


    val state by updateProfileViewModel.uistate.collectAsState()

    val isDarkTheme = isSystemInDarkTheme()

    val placeholderRes = if (isDarkTheme) {
        R.drawable.camera_white // tu drawable para dark
    } else {
        R.drawable.camera_black // tu drawable para light
    }

    Box(
        modifier = modifier,
    ){
        Column(
        ) {
            val lancherBackgroundImage = selectImage {
                updateProfileViewModel.uploadProfileBackgroundToFirebase(it)
            }
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.backgroundImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "User Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .clickable(
                        onClick = {
                            lancherBackgroundImage.launch("image/*")
                        }
                    ),
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
            OutlinedTextField(
                value = state.name,
                onValueChange = {
                    updateProfileViewModel.updateName(it)
                },
                label = { Text(text = "Nombre") },
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .testTag("nameField")
            )
            OutlinedTextField(
                value = state.bio,
                onValueChange = {
                    updateProfileViewModel.updateBio(it)
                },
                label = { Text(text = "Biografia") },
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            )
            OutlinedTextField(
                value = state.location,
                onValueChange = {
                    updateProfileViewModel.updateLocation(it)
                },
                label = { Text(text = "Ubicacion") },
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            )
            Button(
                onClick = {
                    updateProfileViewModel.updateProfile()
                },
                modifier = Modifier.padding(16.dp).fillMaxWidth().testTag("btnUpdateProfile")
            ) {
                Text("Guardar cambios")
            }
            
        }
        val lancherProfileImage = selectImage {
            updateProfileViewModel.uploadProfileImageToFirebase(it)
        }
        ProfileAsyncImage(
            profileImage = state.profileImage,
            size = 80,
            modifier = Modifier.padding(top = 85.dp, start = 15.dp).clip(CircleShape) // Primero se recorta en forma circular
                .border(2.dp, MaterialTheme.colorScheme.background, CircleShape).clickable(
                    onClick = {
                        lancherProfileImage.launch("image/*")
                    }
                ) // Luego el borde se aplica en círculo
        )

    }
}

@Composable
private fun selectImage(
    action: (uri:Uri) -> Unit,
): ManagedActivityResultLauncher<String, Uri?>{
    val launcher = rememberLauncherForActivityResult(
        contract =
            ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            action(uri)
        }
    }
    return launcher
}

@Preview(showBackground = true)
@Composable
fun UpdateProfileScreenPreview() {
    val user = LocalUsersProvider.users[0]
    TwitterFalsoTheme {
        //UpdateProfileScreen(userProfileInfo = user, updateProfileViewModel = hiltViewModel())


    }
}