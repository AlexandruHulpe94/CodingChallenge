package com.example.codingchallenge.presentation.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import com.example.codingchallenge.Constants
import com.example.codingchallenge.domain.model.User
import com.example.codingchallenge.presentation.viewmodel.UserViewModel


@Composable
fun UserListScreen(viewModel: UserViewModel, onClick: (User) -> Unit) {
    val viewState by viewModel.state.collectAsState()

    when {
        viewState.userList.isNotEmpty() -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = Constants.USER_LIST_TITLE) }
                    )
                },
                content = { padding ->
                    Column(
                        modifier = Modifier.padding(padding)
                    ) {
                        LazyColumn {
                            items(viewState.userList) { user ->
                                UserItem(user = user, onClick = onClick)
                            }
                        }
                    }
                }
            )
        }
        viewState.error != null -> {
            Text(text = Constants.ERROR_TEXT + viewState.error)
        }
        else -> {
            Text(text = Constants.NO_USERS_FOUND_TEXT)
        }
    }
}

@Composable
fun UserItem(user: User, onClick: (User) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Constants.CARD_PADDING)
            .clickable(onClick = { onClick(user) }),
        elevation = Constants.CARD_PADDING
    ) {
        Row(
            modifier = Modifier.padding(Constants.ROW_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberAsyncImagePainter(model = user.avatar)
            Image(
                painter = painter,
                contentDescription = Constants.USER_AVATAR_DESCRIPTION,
                modifier = Modifier.size(Constants.IMAGE_SIZE)
            )
            Spacer(modifier = Modifier.width(Constants.SPACER_WIDTH))
            Text(text = user.name, fontSize = Constants.TEXT_SIZE)
        }
    }
}



