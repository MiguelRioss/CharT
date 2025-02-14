package pdm.isel.chaTr.screens.createHabit.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import pdm.isel.chaTr.domain.Habit
import pdm.isel.chaTr.domain.createHabit
import pdm.isel.chaTr.util.TestTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitEditFields(
    initialHabit: Habit? = null,  // ✅ Accepts an existing habit (if editing)
    onSaved: (Habit) -> Unit,
    onCancelled: () -> Unit
) {
    var name by remember { mutableStateOf(initialHabit?.name ?: "") }
    var description by remember { mutableStateOf(initialHabit?.description ?: "") }
    var frequency by remember { mutableStateOf(initialHabit?.frequencyOnDays ?: 0) }
    var expanded by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = if (isEditing) "Edit Habit" else "Habit Details",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.testTag(TestTags.HABIT_TITLE)
        )

        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; isEditing = true }, //
            label = { Text("Habit Name") },
            modifier = Modifier.fillMaxWidth().testTag(TestTags.HABIT_NAME_INPUT)
        )

        // Description Input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it; isEditing = true },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp).testTag(TestTags.HABIT_DESCRIPTION_INPUT)
        )

        // Frequency Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = frequency.toString(),
                onValueChange = {},
                label = { Text("Frequency (Times On Day)") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.HABIT_FREQUENCY_INPUT)
                    .menuAnchor()
                    .clickable { isEditing = true } // ✅ Detects user editing
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                (0..20).forEach { number ->
                    DropdownMenuItem(
                        text = { Text("$number times") },
                        onClick = {
                            frequency = number
                            expanded = false
                            isEditing = true // ✅ Detects changes
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Buttons: Save or Cancel
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    onSaved(createHabit(name, description, frequency)) // ✅ Saves habit
                },
                modifier = Modifier.weight(1f).testTag(TestTags.SAVE_HABIT_BUTTON)
            ) {
                Text("Save Habit")
            }

            Button(
                onClick = { onCancelled() }, // ✅ Cancels without saving
                modifier = Modifier.weight(1f).testTag(TestTags.CANCEL_HABIT_BUTTON)
            ) {
                Text("Cancel")
            }
        }
    }
}

