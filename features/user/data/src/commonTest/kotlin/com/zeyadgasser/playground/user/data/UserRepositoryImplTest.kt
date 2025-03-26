package com.zeyadgasser.playground.user.data

import com.zeyadgasser.playground.user.domain.UserDomain
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {

    private val firebaseAuthDataSource: FirebaseAuthDataSource = mock()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var userRepository: UserRepositoryImpl

    @BeforeTest
    fun setup() {
        userRepository = UserRepositoryImpl(firebaseAuthDataSource, testDispatcher)
    }

    @Test
    fun `getUser returns null when currentUser is null`() {
        every { firebaseAuthDataSource.currentUser } returns null

        val result = userRepository.getUser()

        assertNull(result)
    }

    @Test
    fun `signOut calls signOut`() = runTest {
        val userDomain = UserDomain()
        everySuspend { firebaseAuthDataSource.signOut() } returns Unit

        userRepository.signOut(userDomain)

        verifySuspend { firebaseAuthDataSource.signOut() }
    }

    @Test
    fun `updatePassword calls updatePassword on currentUser`() = runTest {
        val password = "newPassword"
        everySuspend { firebaseAuthDataSource.updatePassword(password) } returns Unit

        userRepository.updatePassword(password)

        verifySuspend { firebaseAuthDataSource.updatePassword(password) }
    }

    @Test
    fun `updateProfile calls updateProfile on currentUser`() = runTest {
        val displayName = "New Name"
        val photoUrl = "http://example.com/newPhoto.jpg"
        everySuspend { firebaseAuthDataSource.updateProfile(displayName, photoUrl) } returns Unit

        userRepository.updateProfile(displayName, photoUrl)

        verifySuspend { firebaseAuthDataSource.updateProfile(displayName, photoUrl) }
    }

    @Test
    fun `delete calls delete on currentUser`() = runTest {
        everySuspend { firebaseAuthDataSource.delete() } returns Unit

        userRepository.delete()

        verifySuspend { firebaseAuthDataSource.delete() }
    }

    @Test
    fun `reload calls reload on currentUser`() = runTest {
        everySuspend { firebaseAuthDataSource.reload() } returns Unit

        userRepository.reload()

        verifySuspend { firebaseAuthDataSource.reload() }
    }

    @Test
    fun `getIdToken calls getIdToken on currentUser`() = runTest {
        val forceRefresh = true
        val token = "testToken"
        everySuspend { firebaseAuthDataSource.getIdToken(forceRefresh) } returns token

        val result = userRepository.getIdToken(forceRefresh)

        assertEquals(token, result)
        verifySuspend { firebaseAuthDataSource.getIdToken(forceRefresh) }
    }

    @Test
    fun `forgotPassword calls sendPasswordResetEmail`() = runTest {
        val email = "test@example.com"
        everySuspend { firebaseAuthDataSource.sendPasswordResetEmail(email) } returns Unit

        userRepository.forgotPassword(email)

        verifySuspend { firebaseAuthDataSource.sendPasswordResetEmail(email) }
    }

    @Test
    fun `verifyBeforeUpdateEmail calls verifyBeforeUpdateEmail on dataSource`() = runTest {
        val newEmail = "new@example.com"
        val androidPackageName = "com.example"
        val installApp = true
        val handleCodeInApp = true

        everySuspend {
            firebaseAuthDataSource.verifyBeforeUpdateEmail(
                newEmail, androidPackageName, installApp, handleCodeInApp
            )
        } returns Unit

        userRepository.verifyBeforeUpdateEmail(newEmail, androidPackageName, installApp, handleCodeInApp)

        verifySuspend {
            firebaseAuthDataSource.verifyBeforeUpdateEmail(
                newEmail, androidPackageName, installApp, handleCodeInApp
            )
        }
    }

    @Test
    fun `reauthenticate throws exception on failure`() = runTest {
        val idToken = "testIdToken"
        val accessToken = "testAccessToken"
        val exception = Exception("Reauthentication Failed")
        everySuspend { firebaseAuthDataSource.reauthenticate(idToken, accessToken) } throws exception

        val error = assertFailsWith<Exception> {
            userRepository.reauthenticate(idToken, accessToken)
        }

        assertEquals(exception.message, error.message)
        verifySuspend { firebaseAuthDataSource.reauthenticate(idToken, accessToken) }
    }

    @Test
    fun `reauthenticateAndRetrieveData throws exception on failure`() = runTest {
        val idToken = "testIdToken"
        val accessToken = "testAccessToken"
        val exception = Exception("Failed to reauthenticate and retrieve data")
        everySuspend {
            firebaseAuthDataSource.reauthenticateAndRetrieveData(idToken, accessToken)
        } throws exception

        val error = assertFailsWith<Exception> {
            userRepository.reauthenticateAndRetrieveData(idToken, accessToken)
        }

        assertEquals(exception.message, error.message)
        verifySuspend { firebaseAuthDataSource.reauthenticateAndRetrieveData(idToken, accessToken) }
    }
}