package com.example.twitterfalso.ui.navigation

import ProfileScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.twitterfalso.ui.Screens.Home.HomeScreen
import com.example.twitterfalso.ui.Screens.Home.HomeViewModel
import com.example.twitterfalso.ui.Screens.Splash.SplashScreen
import com.example.twitterfalso.ui.Screens.StartScreen
import com.example.twitterfalso.ui.Screens.TweetDetail.TweetDetailScreen
import com.example.twitterfalso.ui.Screens.TweetDetail.TweetDetailViewModel
import com.example.twitterfalso.ui.Screens.createTweet.CreateTweetScreen
import com.example.twitterfalso.ui.Screens.createTweet.CreateTweetViewModel
import com.example.twitterfalso.ui.Screens.login.LoginScreen
import com.example.twitterfalso.ui.Screens.login.LoginViewModel
import com.example.twitterfalso.ui.Screens.profile.ProfileViewModel
import com.example.twitterfalso.ui.Screens.register.RegisterScreen
import com.example.twitterfalso.ui.Screens.register.RegisterViewModel
import com.example.twitterfalso.ui.Screens.updateProfile.UpdateProfileScreen
import com.example.twitterfalso.ui.Screens.updateProfile.UpdateProfileViewModel
import com.example.twitterfalso.ui.Screens.userProfile.UserProfileScreen

//Sealed class
//2. definir las rutas
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Register : Screen("register")
    object Start : Screen("start")
    object Home : Screen("home")
    object TweetDetail : Screen("tweetDetail") {
        fun createRoute(tweetId: String) = "tweetDetail/$tweetId"
    }
    object Profile : Screen("profile")
    object Search : Screen("search")
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
    object Login: Screen("login")
    object CreateTweet : Screen("createTweet") {
        fun createRouteModify(tweetId: String) = "createTweet/$tweetId"
        fun createRouteReply(tweetId: String) = "createTweet/response/$tweetId"
    }
    object UserProfile : Screen("userProfile") {
        fun createRoute(userId: String, showEdit: Boolean = false) = "userProfile/$userId/$showEdit"
    }
}



@Composable
fun AppNavigation(
    searchQuery: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ){
        composable(route = Screen.Splash.route){
            SplashScreen(
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navigateToStart = {
                    navController.navigate(Screen.Start.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                splashViewModel = hiltViewModel()
            )
        }

        composable(route = Screen.Start.route)  {
            StartScreen(
                loginButtonPressed = {
                    navController.navigate(Screen.Login.route)
                },
                registerButtonPressed = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(route = Screen.Register.route)  {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            val state by registerViewModel.uistate.collectAsState()
            if (state.navigate) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            RegisterScreen(
                registerViewModel = registerViewModel
            )
        }

        composable(route = Screen.Login.route)  {
            val loginViewModel: LoginViewModel = hiltViewModel()
            val state by loginViewModel.uiState.collectAsState()
            if (state.navigate) {
                navController.navigate(Screen.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
            LoginScreen(
                loginViewModel = loginViewModel
            )
        }


        composable(route = Screen.Home.route)  {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                homeViewModel = hiltViewModel(),
                onTweetClicked = { tweetId ->
                    navController.navigate(Screen.TweetDetail.createRoute(tweetId))
                },
                navigateCreateTweet = {
                    navController.navigate(Screen.CreateTweet.route)
                },
                onTweetReplyClicked = { tweetId ->
                    navController.navigate(Screen.CreateTweet.createRouteReply(tweetId))
                },
                onTweetProfileImageClicked = { userId ->
                    navController.navigate(Screen.UserProfile.createRoute(userId, showEdit = false))
                }
            )
        }


        composable(route = Screen.Notifications.route)  {
            val updateProfileViewModel: UpdateProfileViewModel = hiltViewModel()
            UpdateProfileScreen(updateProfileViewModel = updateProfileViewModel)
        }

        composable(route = Screen.Search.route)  {
            Text(searchQuery)
        }

        composable(route = Screen.Settings.route)  {
            Text("Falta esta pantalla")
        }

        composable(route = Screen.Profile.route)  {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                viewModel = profileViewModel,
                logoutButtonPressed = {
                    navController.navigate(Screen.Start.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.CreateTweet.route,
        ) {
            val createTweetViewModel: CreateTweetViewModel = hiltViewModel()
           CreateTweetScreen(
                viewModel = createTweetViewModel,
                onBack = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        //tweetDetail
        composable(
            route = "tweetDetail/{tweetId}",
            arguments = listOf(navArgument("tweetId") { type = NavType.StringType })
        ){

            //Obtener los paramatros
            val tweetId = it.arguments?.getString("tweetId") ?: ""

            val tweetDetailViewModel: TweetDetailViewModel = hiltViewModel()

            //if(tweet != null){
                TweetDetailScreen(
                    tweetId = tweetId,
                    tweetDetailViewModel = tweetDetailViewModel,
                    modifier = Modifier.padding(12.dp),
                    onTweetReplyClicked = { tweetId ->
                        navController.navigate(Screen.CreateTweet.createRouteReply(tweetId))
                    },
                    onTweetProfileImageClicked = { userId ->
                        navController.navigate(Screen.UserProfile.createRoute(userId, showEdit = false))
                    },
                    onTweetDetailClicked = { tweetId ->
                        navController.navigate(Screen.TweetDetail.createRoute(tweetId))
                    }
                )
        }

        composable(
            route = "userProfile/{userId}/{showEdit}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType }, navArgument("showEdit") { type = NavType.BoolType })
        ) {

            // Obtener los argumentos
            val userId = it.arguments?.getString("userId") ?: "1"
            val showEdit = it.arguments?.getBoolean("showEdit") ?: false

            // Activar la pantalla
            UserProfileScreen(
                userId = userId,
                showEdit = showEdit,
                userProfileViewModel = hiltViewModel(),
                onTweetEditClicked = { tweetId ->
                    navController.navigate(Screen.CreateTweet.createRouteModify(tweetId))
                },
                onTweetProfileImageClicked = { userId ->
                    navController.navigate(Screen.UserProfile.createRoute(userId, showEdit = false))
                }
            )
        }

        composable(
            route = "createTweet/response/{tweetId}",
            arguments = listOf(navArgument("tweetId") { type = NavType.StringType })
        ) {
            // Obtener los argumentos
            val tweetId = it.arguments?.getString("tweetId") ?: ""

            val createTweetViewModel: CreateTweetViewModel = hiltViewModel()
            CreateTweetScreen(
                responseTweetId = tweetId,
                viewModel = createTweetViewModel,
                onBack = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "createTweet/{tweetId}",
            arguments = listOf(navArgument("tweetId") { type = NavType.StringType })
        ) {
            // Obtener los argumentos
            val tweetId = it.arguments?.getString("tweetId") ?: ""

            val createTweetViewModel: CreateTweetViewModel = hiltViewModel()
            CreateTweetScreen(
                tweetId = tweetId,
                viewModel = createTweetViewModel,
                onBack = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
