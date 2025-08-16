import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.twitterfalso.R
import com.example.twitterfalso.ui.Screens.profile.ProfileViewModel
import com.example.twitterfalso.ui.utils.ProfileAsyncImage

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    logoutButtonPressed: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Email: ${state.email}")

        Spacer(modifier = Modifier.weight(1F))
        ProfileAsyncImage(
            profileImage = state.profileImageUrl ?: "",
            size = 200
        )
        PickImageButton(
            action = {
                viewModel.uploadImageToFirebase(it)
            }
        )

        //Contenido centrado
        Spacer(modifier = Modifier.weight(1F))
        Button(
            onClick = {
                viewModel.logout()
                logoutButtonPressed()
            }
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun PickImageButton(
    action: (uri:Uri) -> Unit,
){
    //
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            action(uri)
        }
    }

    Button(
        onClick = {
            launcher.launch("image/*")
        }
    ) {
        Text(text = "Pick Image")
    }
}