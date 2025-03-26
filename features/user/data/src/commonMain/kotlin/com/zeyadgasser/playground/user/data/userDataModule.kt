package com.zeyadgasser.playground.user.data

import com.zeyadgasser.playground.user.domain.UserRepository
import org.koin.dsl.module

val userDataModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
}
