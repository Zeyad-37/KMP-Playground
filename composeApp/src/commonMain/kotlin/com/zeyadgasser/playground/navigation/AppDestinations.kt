package com.zeyadgasser.playground.navigation

import kotlinx.serialization.Serializable

@Serializable
data object List

@Serializable
data class Detail(val id: String)
