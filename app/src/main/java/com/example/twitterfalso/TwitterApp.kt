package com.example.twitterfalso

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.twitterfalso.ui.functions.Utils
import com.example.twitterfalso.ui.navigation.AppNavigation
import com.example.twitterfalso.ui.navigation.ModalNavigation
import com.example.twitterfalso.ui.navigation.NavigationLogic
import com.example.twitterfalso.ui.navigation.Screen
import com.example.twitterfalso.ui.navigation.TwitterBottomNavigationBar
import com.example.twitterfalso.ui.theme.TwitterFalsoTheme
import com.example.twitterfalso.ui.theme.twitterLogoFont
import com.example.twitterfalso.ui.utils.ProfileAsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import java.time.format.TextStyle

@Composable
fun TwitterApp(){

    val context = LocalContext.current
    // Launcher para pedir permiso
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Log.d("FCM", "Permiso de notificaciones concedido ✅")
            } else {
                Log.d("FCM", "Permiso de notificaciones denegado ❌")
            }
        }
    )
    // Al entrar al Composable, pedimos el permiso si hace falta
    LaunchedEffect(Unit) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }


        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Token: ${task.result}")
                } else {
                    Log.e("FCM", "Error obteniendo token", task.exception)
                }
            }
    }

    val navController = rememberNavController()

    val currentRoute = Utils.getCurrentRoute(navController)
    val photoUrl = Utils.getCurrentUserPhoto()
    val userId = Utils.getCurrentUserId()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalNavigation(
                imageProfile = photoUrl,
                onProfileClicked = {
                    navController.navigate(Screen.UserProfile.createRoute(userId, true))
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                if(NavigationLogic.shouldShowTopBar(currentRoute)) {
                    TwitterTopAppBar(
                        photoUrl,
                        profileClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                        currentScreen = currentRoute ?: "")
                }
            },
            bottomBar = {
                if(NavigationLogic.shouldShowBottomBar(currentRoute)) {
                    TwitterBottomNavigationBar(navController = navController)
                }
            }
        ){

            AppNavigation(
                navController = navController,
                modifier = Modifier.padding(it)
            )
        }
    }


}

@Composable
fun TopAppBarTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontFamily = twitterLogoFont,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun TopAppBarSearch(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontFamily = twitterLogoFont,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun CompactSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        modifier = modifier,
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, color = Color.Black),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .height(30.dp) // 👈 aquí sí controlas el alto
                    .fillMaxWidth()
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(8.dp))
                Box {
                    if (query.isEmpty()) {
                        Text("Buscar...", fontSize = 14.sp, color = Color.Gray)
                    }
                    // 👇 centramos el textfield
                    Box(Modifier.align(Alignment.CenterStart)) {
                        innerTextField()
                    }
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwitterTopAppBar(
    imageProfile: String,
    currentScreen: String,
    profileClicked: () -> Unit
){

    var searchBarContent by remember { mutableStateOf("") }

    CenterAlignedTopAppBar(
        title = {
            if(currentScreen != Screen.Search.route) {
                TopAppBarTitle("Twitter")
            } else{
                CompactSearchBar(
                    query = searchBarContent,
                    onQueryChange = {
                        searchBarContent = it
                    },
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

        },
        navigationIcon = {
            IconButton(
                onClick = profileClicked
            ) {
                ProfileAsyncImage(
                    profileImage = imageProfile,
                    size = 40
                )
            }
        }

    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TwitterFalsoTheme {
        TwitterTopAppBar(
            imageProfile = "",
            profileClicked = {},
            currentScreen = ""
        )
    }
}
