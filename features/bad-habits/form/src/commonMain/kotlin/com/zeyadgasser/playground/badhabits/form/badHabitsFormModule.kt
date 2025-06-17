package com.zeyadgasser.playground.badhabits.form

import com.zeyadgasser.playground.architecture.di.IO
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormState
import com.zeyadgasser.playground.badhabits.form.viewmodel.BadHabitFormViewModel
import com.zeyadgasser.playground.badhabits.form.viewmodel.SubmitBadHabitInputHandler
import com.zeyadgasser.playground.badhabits.form.viewmodel.ValidateFormInputHandler
import org.koin.core.qualifier.named
import org.koin.dsl.module

val badHabitsFormModule = module {
    single<BadHabitFormState> { BadHabitFormState.InitialState }
    single { SubmitBadHabitInputHandler(get(), get()) }
    single { ValidateFormInputHandler }
    factory { BadHabitFormViewModel(get(), get(), get(), get(), get(named(IO))) }
}
