package pdm.isel.chaTr.screens.HabitList

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pdm.isel.chaTr.domain.Habit

@Composable
fun HabitListScreen(viewModel: ExistingHabitsViewModel, onDelete: (Int) -> Unit, onEdit: (Habit) -> Unit) {

    val habits by viewModel.habits.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { viewModel.resetHabits() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Reset All Habits")
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(habits) { habit ->
                HabitItem(
                    habit = habit,
                    onDelete = { onDelete(habit.id) },
                    onReset = { viewModel.resetHabit(habit.id) },
                    updateHabit = { onEdit(it) }
                )
            }
        }
    }
}

