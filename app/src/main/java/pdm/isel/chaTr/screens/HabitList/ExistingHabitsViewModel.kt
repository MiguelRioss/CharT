package pdm.isel.chaTr.screens.HabitList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pdm.isel.chaTr.domain.Habit
import pdm.isel.chaTr.domain.HabitRepository

class ExistingHabitsViewModel(private val habitRepository: HabitRepository) : ViewModel() {

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()


    fun listenToHabits() {
        viewModelScope.launch {
            habitRepository.habits.collect { newHabits ->
                _habits.value = newHabits
            }
        }

    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            habitRepository.removeHabit(habitId)
        }
    }

    fun onEditHabit(habit: Habit) {
        viewModelScope.launch {
            habitRepository.updateHabit(habit)
            _habits.value = _habits.value.map { if (it.id == habit.id) habit else it }
        }
    }
    fun resetHabits() {
        viewModelScope.launch {
            habitRepository.resetHabits()
        }
    }
    fun resetHabit(habitId: Int) {
        viewModelScope.launch {
            habitRepository.resetHabit(habitId)
        }
    }

}

@Suppress("UNCHECKED_CAST")
class ExistingHabitsViewModelFactory(private val habitRepository: HabitRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExistingHabitsViewModel(habitRepository) as T
    }
}
