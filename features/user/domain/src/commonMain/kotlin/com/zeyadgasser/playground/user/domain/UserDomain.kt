package com.zeyadgasser.playground.user.domain

data class UserDomain(
    val displayName: String? = null,
    val email: String? = null,
//    val address: String? = null,
    val phoneNumber: String? = null,
    val photoURL: String? = null,
    val providerId: String = "",
    val uid: String = "",
    val creationTime: Double? = null,
    val lastSignInTime: Double? = null,
)
