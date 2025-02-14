package pdm.isel.chaTr.screens.createHabit.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pdm.isel.chaTr.domain.Habit
import pdm.isel.chaTr.domain.HabitRepository

class NewHabitViewModel(
    private val habitRepository: HabitRepository,
    initialScreenState: NewHabitScreenState = NewHabitScreenState.Initialized
) : ViewModel() {

    private val _screenState = MutableStateFlow<NewHabitScreenState>(NewHabitScreenState.Initialized)
    val screenState: StateFlow<NewHabitScreenState> = _screenState.asStateFlow()

    init {
        _screenState.value = NewHabitScreenState.Displaying(null)
    }

    fun startEditing() {
        val currentState = _screenState.value
        if (currentState is NewHabitScreenState.Displaying) {
            _screenState.value = NewHabitScreenState.Editing(currentState.habit)
        }
    }

    fun cancelEditing() {
        _screenState.value = NewHabitScreenState.Concluded
    }




    fun saveHabit(updatedHabit: Habit): Job {
        return viewModelScope.launch {
            habitRepository.addHabit(updatedHabit)
            _screenState.value = NewHabitScreenState.Displaying(updatedHabit)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class NewHabitViewModelFactory(private val habitRepository: HabitRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewHabitViewModel(habitRepository) as T
    }
}
