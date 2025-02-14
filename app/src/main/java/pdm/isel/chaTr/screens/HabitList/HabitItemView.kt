package pdm.isel.chaTr.screens.HabitList

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pdm.isel.chaTr.domain.Habit

const val VIEW_MODEL_TAG = "HabitItem"

@Composable
fun HabitItem(
    habit: Habit,
    updateHabit: (Habit) -> Unit,
    onDelete: () -> Unit,
    onReset: () -> Unit // ✅ Reset action added
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = habit.name, style = MaterialTheme.typography.titleMedium)
            Text(text = habit.description, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Frequency: ${habit.frequencyOnDays} per day", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Centered Counter UI
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    enabled = habit.completed > 0,
                    onClick = {
                        if (habit.completed > 0) {
                            updateHabit(habit.copy(completed = habit.completed - 1))
                        }
                    }
                ) {
                    Text("-")
                }

                Text("Completed: ${habit.completed}", style = MaterialTheme.typography.bodyLarge)

                Button(
                    enabled = habit.completed < habit.frequencyOnDays,
                    onClick = {
                        if (habit.completed < habit.frequencyOnDays) {
                            updateHabit(habit.copy(completed = habit.completed + 1))
                        }
                    }
                ) {
                    Text("+")
                }
            }

            // ✅ New Reset Button
            Button(
                onClick = onReset, // Calls reset function
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Reset")
            }

            // ✅ Delete Button
            Button(
                onClick = onDelete,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Delete")
            }
        }
    }
}
