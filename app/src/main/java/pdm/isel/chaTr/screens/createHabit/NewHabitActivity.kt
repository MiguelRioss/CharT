package pdm.isel.chaTr.screens.createHabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pdm.isel.chaTr.DependenciesContainer
import pdm.isel.chaTr.screens.createHabit.domain.NewHabitViewModel
import pdm.isel.chaTr.screens.createHabit.domain.NewHabitViewModelFactory
import pdm.isel.chaTr.screens.theme.MyApplicationTheme

class NewHabitActivity : ComponentActivity() {

    private val habitRepository by lazy {
        (application as? DependenciesContainer)?.habitsRepository
            ?: throw IllegalStateException("Application must implement DependenciesContainer")
    }

    private val viewModel by viewModels<NewHabitViewModel> {
        NewHabitViewModelFactory(habitRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                NewHabitScreen(viewModel = viewModel , onBackIntent = { finish() })
            }
        }
    }
}
