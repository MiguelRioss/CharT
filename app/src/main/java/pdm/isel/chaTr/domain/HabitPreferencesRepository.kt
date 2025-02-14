package pdm.isel.chaTr.domain

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pdm.isel.chaTr.screens.HabitList.VIEW_MODEL_TAG

const val PREFERENCES_TAG = "HabitPreferencesRepository"
interface HabitRepository {
    val habits: StateFlow<List<Habit>>
    suspend fun getHabits(): List<Habit>
    suspend fun addHabit(habit: Habit)
    suspend fun removeHabit(habitId: Int)
    suspend fun clearHabits()
    suspend fun updateHabit(habit: Habit)
    suspend fun resetHabits()
    suspend fun resetHabit(habitId: Int)

}


class HabitPreferencesRepository(private val store: DataStore<Preferences>) : HabitRepository {

    private val HABITS_KEY = stringPreferencesKey("habits")

    private val _habitsFlow = MutableStateFlow<List<Habit>>(emptyList())
    override val habits: StateFlow<List<Habit>> = _habitsFlow.asStateFlow()

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        repositoryScope.launch {
            store.data.map { preferences ->
                preferences[HABITS_KEY]?.let { json ->
                    Json.decodeFromString<List<Habit>>(json)
                } ?: emptyList()
            }.collect { habitList ->
                _habitsFlow.value = habitList
            }
        }
    }

    override suspend fun getHabits(): List<Habit> {
        return _habitsFlow.value
    }

    override suspend fun addHabit(habit: Habit) {
        updateDataStore { habits -> habits + habit }
    }

    override suspend fun removeHabit(habitId: Int) {
        updateDataStore { habits -> habits.filterNot { it.id == habitId } }
    }

    override suspend fun clearHabits() {
        updateDataStore { emptyList() }
    }

    override suspend fun updateHabit(habit: Habit) {
        updateDataStore { habits ->
            val updatedHabits = habits.toMutableList()
            val index = updatedHabits.indexOfFirst { it.id == habit.id }

            if (index != -1) {
                Log.v(VIEW_MODEL_TAG, "Updating habit: ${habit.name}, New Completed: ${habit.completed}")
                updatedHabits[index] = habit.copy(completed = habit.completed)
            } else {
                Log.e(VIEW_MODEL_TAG, " Habit not found in repository!")
            }

            updatedHabits // Return modified list
        }
    }


    override suspend fun resetHabits() {
        updateDataStore { habits ->
            habits.map { it.copy(completed = 0) }
        }
    }

    override suspend fun resetHabit(habitId: Int) {
        updateDataStore { habits ->
            habits.map { habit ->
                if (habit.id == habitId) habit.copy(completed = 0)
                else habit // Keep others unchanged
            }
        }
    }



    private suspend fun updateDataStore(update: (List<Habit>) -> List<Habit>) {
        store.edit { preferences ->
            val currentHabits = _habitsFlow.value
            val updatedHabits = update(currentHabits)
            preferences[HABITS_KEY] = Json.encodeToString(updatedHabits)
        }
    }
}
