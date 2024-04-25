package com.example.codingchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.codingchallenge.data.remote.DomainResult
import com.example.codingchallenge.domain.model.User
import com.example.codingchallenge.domain.usecase.GetUserDetailUseCase
import com.example.codingchallenge.domain.usecase.GetUserListUseCase
import com.example.codingchallenge.presentation.state.UserIntent
import com.example.codingchallenge.presentation.viewmodel.UserViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class UserViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UserViewModel
    private val getUserListUseCase: GetUserListUseCase = mockk(relaxed = true)
    private val getUserDetailUseCase: GetUserDetailUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = UserViewModel(getUserListUseCase, getUserDetailUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when FetchUsers intent is processed, it triggers getUserListUseCase`(): Unit =
        runBlockingTest {
            val users = listOf(User(id = 1, name = "Test", avatar = "avatar"))
            coEvery { getUserListUseCase() } returns flowOf(DomainResult.Success(users))

            viewModel.processIntent(UserIntent.FetchUsers)

            delay(100)  // Allow time for the flow collection to happen

            assertEquals(users, viewModel.state.value.userList)
            assertNull(viewModel.state.value.error)
        }

    @Test
    fun `when FetchUserDetail intent is processed, it triggers getUserDetailUseCase`() =
        runBlockingTest {
            val user = User(id = 1, name = "Test", avatar = "avatar")
            coEvery { getUserDetailUseCase(1) } returns flowOf(DomainResult.Success(user))

            viewModel.processIntent(UserIntent.FetchUserDetail(1))

            delay(100)  // Allow time for the flow collection to happen

            assertEquals(user, viewModel.state.value.userDetail)
            assertNull(viewModel.state.value.error)
        }
}
