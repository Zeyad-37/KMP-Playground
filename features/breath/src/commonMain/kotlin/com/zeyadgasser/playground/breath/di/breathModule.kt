package com.zeyadgasser.playground.breath.di

import com.zeyadgasser.playground.breath.viewmodel.BreathingViewModel
import org.koin.dsl.module

val breathModule = module {
    factory { BreathingViewModel() }
}
