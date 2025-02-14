package pdm.isel.chaTr.screens.createHabit.domain

import pdm.isel.chaTr.domain.Habit

sealed interface NewHabitScreenState {
    data object Initialized : NewHabitScreenState
    data class Displaying(val habit: Habit?) : NewHabitScreenState
    data class Editing(val habit: Habit?) : NewHabitScreenState
    data object Concluded : NewHabitScreenState
}
