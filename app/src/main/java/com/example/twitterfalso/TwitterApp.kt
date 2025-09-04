package com.example.twitterfalso

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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




@Composable
fun TwitterApp(){

    FirebaseMessaging.getInstance().token.addOnCompleteListener{ task->
        if(task.isSuccessful){
            Log.d("Token", task.result)
        } else {
            Log.d("Token", "Error getting token")
        }
    }

    val navController = rememberNavController()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val currentUser = FirebaseAuth.getInstance().currentUser
    val photoUrl: String = currentUser?.photoUrl?.toString() ?: ""

    val userId: String = currentUser?.uid ?: ""

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Log.d("NotificationPermission", "Permission granted")
            } else {
                Log.d("NotificationPermission", "Permission denied")
            }
        }
    )

    LaunchedEffect(Unit) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
          if(ContextCompat.checkSelfPermission(
                  context,
                  Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
              notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
          }
        }
    }



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
                    TwitterTopAppBar(photoUrl, profileClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    })
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwitterTopAppBar(
    imageProfile: String,
    profileClicked: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Twitter",
                fontWeight = FontWeight.Bold,
                fontFamily = twitterLogoFont,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
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
            profileClicked = {}
        )
    }
}
