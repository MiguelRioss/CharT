package pdm.isel.chatr.screens.habitlist

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pdm.isel.chaTr.domain.Habit
import pdm.isel.chaTr.domain.HabitRepository
import pdm.isel.chaTr.screens.HabitList.ExistingHabitsViewModel
import pdm.isel.cher.rules.ReplaceMainDispatcherRule

@RunWith(AndroidJUnit4::class)  // ✅ Required for JUnit 4 compatibility
class ExistingHabitsViewModelTest {

    @get:Rule
    val dispatcherRule = ReplaceMainDispatcherRule() // ✅ Allows proper coroutine testing

    private val testHabit = Habit(1, "Exercise", "Morning jog", 3)

    private val fakeRepo = object : HabitRepository {
        private val _habits = MutableStateFlow<List<Habit>>(listOf(testHabit)) // ✅ Ensure emission
        override val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

        override suspend fun getHabits(): List<Habit> {
            return _habits.value
        }
        override suspend fun addHabit(habit: Habit) {
            _habits.value = _habits.value + habit
        }
        override suspend fun removeHabit(habitId: Int) {
            _habits.value = _habits.value.filterNot { it.id == habitId }
        }
        override suspend fun clearHabits() {
            _habits.value = emptyList()
        }
        override suspend fun updateHabit(habit: Habit) {
            _habits.value = _habits.value.map { if (it.id == habit.id) habit else it }
        }
    }


    @Test
    fun initialStateContainsTestHabit() = runTest {
        val viewModel = ExistingHabitsViewModel(fakeRepo)
        advanceUntilIdle()  // ✅ Ensures Flow has collected the latest data
        assertEquals(listOf(testHabit), viewModel.habits.value)
    }

    @Test
    fun deleteHabitRemovesFromList() = runTest {
        val viewModel = ExistingHabitsViewModel(fakeRepo)
        viewModel.deleteHabit(testHabit.id)
        advanceUntilIdle()
        assertTrue(viewModel.habits.value.isEmpty()) // ✅ Habit should be removed
    }

    @Test
    fun editHabitUpdatesDetails() = runTest {
        val viewModel = ExistingHabitsViewModel(fakeRepo)
        val updatedHabit = testHabit.copy(name = "Updated Exercise")
        viewModel.onEditHabit(updatedHabit)
        advanceUntilIdle()
        assertEquals("Updated Exercise", viewModel.habits.value.first().name) // ✅ Check updated name
    }

    @Test
    fun listenToHabitsUpdatesViewModel() = runTest {
        val viewModel = ExistingHabitsViewModel(fakeRepo)
        val newHabit = Habit(2, "Reading", "Read a book", 1)
        fakeRepo.addHabit(newHabit)
        advanceUntilIdle()
        assertTrue(viewModel.habits.value.contains(newHabit)) // ✅ New habit should be detected
    }
}
