package pdm.isel.chaTr.screens.createHabit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import pdm.isel.chaTr.screens.createHabit.domain.NewHabitScreenState
import pdm.isel.chaTr.screens.createHabit.domain.NewHabitViewModel
import pdm.isel.chaTr.screens.createHabit.views.HabitEditFields

@Composable
fun NewHabitScreen(viewModel: NewHabitViewModel , onBackIntent: () -> Unit) {
    val screenState by viewModel.screenState.collectAsState()

    when (screenState) {
        is NewHabitScreenState.Displaying -> {
            HabitEditFields(
                initialHabit = (screenState as NewHabitScreenState.Displaying).habit,
                onSaved = { habit -> viewModel.saveHabit(habit); onBackIntent() },
                onCancelled = { onBackIntent() }
            )
        }
        is NewHabitScreenState.Editing -> {
            HabitEditFields(
                initialHabit = (screenState as NewHabitScreenState.Editing).habit,
                onSaved = { habit -> viewModel.saveHabit(habit) },
                onCancelled = { viewModel.cancelEditing() }
            )
        }
        is NewHabitScreenState.Concluded -> {
            Text("Habit saved or cancelled!")
        }
        else -> {
            Text("Initializing...")
        }
    }
}


