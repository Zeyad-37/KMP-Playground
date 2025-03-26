package com.zeyadgasser.playground.user.data

import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import dev.gitlive.firebase.auth.ActionCodeSettings
import dev.gitlive.firebase.auth.AndroidPackageName
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.PhoneAuthProvider

@OpenForMokkery
interface FirebaseAuthDataSource {
    val currentUser: FirebaseUser?
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean
    suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean
    suspend fun signOut()
    suspend fun sendPasswordResetEmail(email: String)
    suspend fun updatePassword(password: String): Unit?
    suspend fun updateProfile(displayName: String?, photoUrl: String?): Unit?
    suspend fun delete(): Unit?
    suspend fun reload(): Unit?
    suspend fun getIdToken(forceRefresh: Boolean): String?
    suspend fun unlink(provider: String): FirebaseUser?
    suspend fun updatePhoneNumber(
        phoneNumber: String,
        verificationId: String,
    ): Unit? // PhoneAuthCredential

    suspend fun verifyBeforeUpdateEmail(
        newEmail: String,
        androidPackageName: String?,
        installApp: Boolean,
        handleCodeInApp: Boolean,
    ): Unit?

    suspend fun reauthenticate(
        idToken: String?,
        accessToken: String?,
    ): Unit? // AuthCredential

    suspend fun reauthenticateAndRetrieveData(
        idToken: String?,
        accessToken: String?,
    ): AuthCredential? // AuthCredential

    suspend fun sendEmailVerification(
        url: String,
        packageName: String,
        installIfNotAvailable: Boolean,
        minimumVersion: String?,
        dynamicLinkDomain: String?,
        canHandleCodeInApp: Boolean,
        iOSBundleId: String?,
    ): Unit?
}

@OpenForMokkery
class FirebaseAuthDataSourceImpl(private val firebaseAuth: FirebaseAuth) : FirebaseAuthDataSource {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean =
        firebaseAuth.createUserWithEmailAndPassword(email, password).user != null

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean =
        firebaseAuth.signInWithEmailAndPassword(email, password).user != null

    override suspend fun signOut() = firebaseAuth.signOut()

    override suspend fun sendPasswordResetEmail(email: String) =
        firebaseAuth.sendPasswordResetEmail(email)

    override suspend fun updatePassword(password: String) =
        firebaseAuth.currentUser?.updatePassword(password) ?: Unit

    override suspend fun updateProfile(displayName: String?, photoUrl: String?) =
        firebaseAuth.currentUser?.updateProfile(displayName, photoUrl)

    override suspend fun delete() = firebaseAuth.currentUser?.delete()

    override suspend fun reload() = firebaseAuth.currentUser?.reload()

    override suspend fun getIdToken(forceRefresh: Boolean): String? =
        firebaseAuth.currentUser?.getIdToken(forceRefresh)

    override suspend fun unlink(provider: String): FirebaseUser? =
        firebaseAuth.currentUser?.unlink(provider)

    override suspend fun updatePhoneNumber(phoneNumber: String, verificationId: String): Unit? =
        PhoneAuthProvider().credential(verificationId, phoneNumber)
            .let { firebaseAuth.currentUser?.updatePhoneNumber(it) }

    override suspend fun verifyBeforeUpdateEmail(
        newEmail: String,
        androidPackageName: String?,
        installApp: Boolean,
        handleCodeInApp: Boolean,
    ) = ActionCodeSettings(
        "https://www.example.com", // You should provide a valid URL. fixme
        AndroidPackageName(androidPackageName!!, installApp),
        canHandleCodeInApp = handleCodeInApp,
    ).let { firebaseAuth.currentUser?.verifyBeforeUpdateEmail(newEmail, it) }

    override suspend fun reauthenticate(idToken: String?, accessToken: String?): Unit? =
        firebaseAuth.currentUser?.reauthenticate(GoogleAuthProvider.credential(idToken, accessToken))

    override suspend fun reauthenticateAndRetrieveData(
        idToken: String?,
        accessToken: String?,
    ): AuthCredential? {
        return firebaseAuth.currentUser?.reauthenticateAndRetrieveData(
            GoogleAuthProvider.credential(idToken, accessToken)
        )?.credential
    }

    override suspend fun sendEmailVerification(
        url: String,
        packageName: String,
        installIfNotAvailable: Boolean,
        minimumVersion: String?,
        dynamicLinkDomain: String?,
        canHandleCodeInApp: Boolean,
        iOSBundleId: String?,
    ) = firebaseAuth.currentUser?.sendEmailVerification(
        ActionCodeSettings(
            url,
            AndroidPackageName(packageName, installIfNotAvailable, minimumVersion),
            dynamicLinkDomain,
            canHandleCodeInApp,
            iOSBundleId
        )
    )
}