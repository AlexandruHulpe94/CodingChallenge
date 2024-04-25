package com.example.codingchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.codingchallenge.presentation.state.UserIntent
import com.example.codingchallenge.presentation.ui.detail.UserDetailScreen
import com.example.codingchallenge.presentation.ui.list.UserListScreen
import com.example.codingchallenge.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "user_list") {
                composable("user_list") {
                    viewModel.processIntent(UserIntent.FetchUsers)
                    UserListScreen(viewModel) { user ->
                        navController.navigate("user_detail/${user.id}")
                    }
                }
                composable("user_detail/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")
                    UserDetailScreen(
                        userId = userId,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }

    }
}