package com.example.codingchallenge

import com.example.codingchallenge.data.remote.DomainResult
import com.example.codingchallenge.data.remote.DomainResultFailureReason
import com.example.codingchallenge.data.repository.UserRepository
import com.example.codingchallenge.domain.model.User
import com.example.codingchallenge.domain.usecase.GetUserListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetUserListUseCaseTest {

    private lateinit var useCase: GetUserListUseCase
    private val repository: UserRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetUserListUseCase(repository)
    }

    @Test
    fun `when getUsers is successful, it returns a list of users`() = runBlocking {
        val users = listOf(User(id = 1, name = "Test", avatar = "avatar"))
        coEvery { repository.getUsers() } returns flowOf(DomainResult.Success(users))

        val result = useCase().first()

        assertTrue(result is DomainResult.Success)
        assertEquals(users, (result as DomainResult.Success).value)
    }

    @Test
    fun `when getUsers fails, it returns a failure internal error`() = runBlocking {
        coEvery { repository.getUsers() } returns flowOf(
            DomainResult.Failure(
                DomainResultFailureReason.InternalError("Internal error")
            )
        )

        val result = useCase().first()

        assertTrue(result is DomainResult.Failure)
        assertEquals(
            DomainResultFailureReason.InternalError("Internal error"),
            (result as DomainResult.Failure).failureReason
        )
    }

    @Test
    fun `when getUsers fails, it returns a failure`() = runBlocking {
        coEvery { repository.getUsers() } returns flowOf(
            DomainResult.Failure(
                DomainResultFailureReason.Unavailable("Unavailable")
            )
        )

        val result = useCase().first()

        assertTrue(result is DomainResult.Failure)
        assertEquals(
            DomainResultFailureReason.Unavailable("Unavailable"),
            (result as DomainResult.Failure).failureReason
        )
    }

    @Test
    fun `when getUsers fails, it returns a failure network error`() = runBlocking {
        coEvery { repository.getUsers() } returns flowOf(
            DomainResult.Failure(
                DomainResultFailureReason.NetworkError("Network error")
            )
        )

        val result = useCase().first()

        assertTrue(result is DomainResult.Failure)
        assertEquals(
            DomainResultFailureReason.NetworkError("Network error"),
            (result as DomainResult.Failure).failureReason
        )
    }
}