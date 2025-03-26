package com.zeyadgasser.playground.user.domain

import com.zeyadgasser.playground.architecture.domain.Repository

interface UserRepository : Repository {
    fun getUser(): UserDomain?
    suspend fun signUp(user: UserDomain, password: String): Boolean
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signOut(user: UserDomain): Unit
    suspend fun forgotPassword(email: String): Boolean
    suspend fun updatePassword(password: String): Boolean
    suspend fun updatePhoneNumber(phoneNumber: String, verificationId: String): Boolean
    suspend fun updateProfile(displayName: String?, photoUrl: String?): Boolean
    suspend fun verifyBeforeUpdateEmail(
        newEmail: String,
        androidPackageName: String?,
        installApp: Boolean,
        handleCodeInApp: Boolean,
    ): Boolean

    suspend fun delete(): Boolean
    suspend fun reload(): Boolean
    suspend fun getIdToken(forceRefresh: Boolean): String?
    suspend fun reauthenticate(idToken: String?, accessToken: String?): Boolean
    suspend fun reauthenticateAndRetrieveData(idToken: String?, accessToken: String?): Boolean
    suspend fun sendEmailVerification(
        url: String,
        packageName: String,
        installIfNotAvailable: Boolean,
        minimumVersion: String?,
        dynamicLinkDomain: String?,
        canHandleCodeInApp: Boolean,
        iOSBundleId: String?,
    ): Boolean

    suspend fun unlink(provider: String): UserDomain?
}