package com.zeyadgasser.playground.architecture.data

interface MapsTo<Domain> {
    fun mapsTo(): Domain
}
