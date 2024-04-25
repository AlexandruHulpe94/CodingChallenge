package com.example.codingchallenge.presentation.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.codingchallenge.Constants
import com.example.codingchallenge.presentation.state.UserIntent
import com.example.codingchallenge.presentation.viewmodel.UserViewModel


@Composable
fun UserDetailScreen(userId: String?, viewModel: UserViewModel, navController: NavController) {
    val id = try { userId?.toInt() } catch (e: NumberFormatException) { null }

    if (id != null) {
        LaunchedEffect(id) {
            viewModel.processIntent(UserIntent.FetchUserDetail(id))
        }
    }

    val viewState by viewModel.state.collectAsState()

    when {
        viewState.userDetail != null -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = Constants.USER_DETAIL_TITLE) },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = null)
                            }
                        }
                    )
                },
                content = { padding ->
                    Column(
                        modifier = Modifier.padding(padding)
                    ) {
                        Text(text = "ID: ${viewState.userDetail!!.id}")
                        Text(text = "Name: ${viewState.userDetail!!.name}")
                        Image(
                            painter = rememberAsyncImagePainter(model = viewState.userDetail!!.avatar),
                            contentDescription = Constants.USER_AVATAR_DESCRIPTION,
                            modifier = Modifier.size(Constants.IMAGE_SIZE)
                        )
                    }
                }
            )
        }
        viewState.error != null -> {
            Text(text = Constants.USER_DETAIL_ERROR_TEXT + viewState.error)
        }
        else -> {
            Text(text = Constants.NO_USER_DETAIL_FOUND_TEXT)
        }
    }
}



