package pdm.isel.chaTr.screens.HabitList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import pdm.isel.chaTr.DependenciesContainer
import pdm.isel.chaTr.screens.createHabit.NewHabitActivity
import pdm.isel.chaTr.screens.theme.MyApplicationTheme
const val ACTIVITY_TAG = "ExistingHabitsActivity"
class ExistingHabitsActivity : ComponentActivity() {

    private val habitRepository by lazy {
        (application as? DependenciesContainer)?.habitsRepository
            ?: throw IllegalStateException("Application must implement DependenciesContainer")
    }

    private val viewModel by viewModels<ExistingHabitsViewModel> {
        ExistingHabitsViewModelFactory(habitRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewModel.listenToHabits()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.habits.collectLatest {
                    Log.v(ACTIVITY_TAG, "Habits updated")
                }
            }
        }
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Text("Your Habits", style = MaterialTheme.typography.headlineMedium)

                    Spacer(modifier = Modifier.height(16.dp))



                    Spacer(modifier = Modifier.height(16.dp))

                    HabitListScreen(
                        viewModel = viewModel,
                        onDelete = { viewModel.deleteHabit(it) },
                        onEdit = { viewModel.onEditHabit(it) }
                    )
                    Button(
                        onClick = { navigateToNewHabit() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create New Habit")
                    }
                }
            }
        }
    }


    private fun navigateToNewHabit() {
        val intent = Intent(this, NewHabitActivity::class.java)
        startActivity(intent)
    }
}
