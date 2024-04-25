package com.example.codingchallenge

import com.example.codingchallenge.data.remote.DomainResult
import com.example.codingchallenge.data.remote.DomainResultFailureReason
import com.example.codingchallenge.data.repository.UserRepository
import com.example.codingchallenge.domain.model.User
import com.example.codingchallenge.domain.usecase.GetUserDetailUseCase
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
class GetUserDetailUseCaseTest {

    private lateinit var useCase: GetUserDetailUseCase
    private val repository: UserRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetUserDetailUseCase(repository)
    }

    @Test
    fun `when getUserDetail is successful, it returns a user`() = runBlocking {
        val user = User(id = 1, name = "Test", avatar = "avatar")
        coEvery { repository.getUserDetail(1) } returns flowOf(DomainResult.Success(user))

        val result = useCase(1).first()

        assertTrue(result is DomainResult.Success)
        assertEquals(user, (result as DomainResult.Success).value)
    }

    @Test
    fun `when getUserDetail fails, it returns a failure internal error`() = runBlocking {
        coEvery { repository.getUserDetail(1) } returns flowOf(
            DomainResult.Failure(
                DomainResultFailureReason.InternalError("Internal error")
            )
        )

        val result = useCase(1).first()

        assertTrue(result is DomainResult.Failure)
        assertEquals(
            DomainResultFailureReason.InternalError("Internal error"),
            (result as DomainResult.Failure).failureReason
        )
    }

    @Test
    fun `when getUserDetail fails, it returns a failure unavailable`() = runBlocking {
        coEvery { repository.getUserDetail(1) } returns flowOf(
            DomainResult.Failure(
                DomainResultFailureReason.Unavailable("Unavailable")
            )
        )

        val result = useCase(1).first()

        assertTrue(result is DomainResult.Failure)
        assertEquals(
            DomainResultFailureReason.Unavailable("Unavailable"),
            (result as DomainResult.Failure).failureReason
        )
    }

    @Test
    fun `when getUserDetail fails, it returns a failure network error`() = runBlocking {
        coEvery { repository.getUserDetail(1) } returns flowOf(
            DomainResult.Failure(
                DomainResultFailureReason.NetworkError("Network error")
            )
        )

        val result = useCase(1).first()

        assertTrue(result is DomainResult.Failure)
        assertEquals(
            DomainResultFailureReason.NetworkError("Network error"),
            (result as DomainResult.Failure).failureReason
        )
    }
}
