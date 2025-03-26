package com.zeyadgasser.playground.user.data

import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import com.zeyadgasser.playground.user.domain.UserDomain
import com.zeyadgasser.playground.user.domain.UserRepository
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher

@OpenForMokkery
class UserRepositoryImpl(
    private val authApi: FirebaseAuthDataSource,
    override val ioDispatcher: CoroutineDispatcher,
) : UserRepository {

    override fun getUser(): UserDomain? = authApi.currentUser?.toDomain()

    override suspend fun signUp(user: UserDomain, password: String): Boolean =
        authApi.createUserWithEmailAndPassword(user.email!!, password)

    override suspend fun signIn(email: String, password: String): Boolean =
        authApi.signInWithEmailAndPassword(email, password)

    override suspend fun signOut(user: UserDomain) = authApi.signOut()

    override suspend fun updatePassword(password: String): Boolean = authApi.updatePassword(password) == Unit

    override suspend fun updateProfile(displayName: String?, photoUrl: String?) =
        authApi.updateProfile(displayName, photoUrl) == Unit

    override suspend fun delete() = authApi.delete() == Unit

    override suspend fun reload() = authApi.reload() == Unit

    override suspend fun getIdToken(forceRefresh: Boolean): String? = authApi.getIdToken(forceRefresh)

    override suspend fun unlink(provider: String): UserDomain? = authApi.unlink(provider)?.toDomain()

    override suspend fun forgotPassword(email: String) = authApi.sendPasswordResetEmail(email) == Unit

    override suspend fun updatePhoneNumber(phoneNumber: String, verificationId: String): Boolean =
        authApi.updatePhoneNumber(phoneNumber, verificationId) == Unit

    override suspend fun verifyBeforeUpdateEmail(
        newEmail: String,
        androidPackageName: String?,
        installApp: Boolean,
        handleCodeInApp: Boolean,
    ) = authApi.verifyBeforeUpdateEmail(newEmail, androidPackageName, installApp, handleCodeInApp) == Unit


    override suspend fun reauthenticate(idToken: String?, accessToken: String?): Boolean =
        authApi.reauthenticate(idToken, accessToken) == Unit

    override suspend fun reauthenticateAndRetrieveData(idToken: String?, accessToken: String?): Boolean =
        authApi.reauthenticateAndRetrieveData(idToken, accessToken) == Unit

    override suspend fun sendEmailVerification(
        url: String,
        packageName: String,
        installIfNotAvailable: Boolean,
        minimumVersion: String?,
        dynamicLinkDomain: String?,
        canHandleCodeInApp: Boolean,
        iOSBundleId: String?,
    ) = authApi.sendEmailVerification(
        url,
        packageName,
        installIfNotAvailable,
        minimumVersion,
        dynamicLinkDomain,
        canHandleCodeInApp,
        iOSBundleId
    ) == Unit

    private fun FirebaseUser.toDomain(): UserDomain = UserDomain(
        displayName = this.displayName,
        email = this.email,
        phoneNumber = this.phoneNumber,
        photoURL = this.photoURL,
        providerId = this.providerId,
        uid = this.uid,
        creationTime = this.metaData?.creationTime,
        lastSignInTime = this.metaData?.lastSignInTime
    )
}