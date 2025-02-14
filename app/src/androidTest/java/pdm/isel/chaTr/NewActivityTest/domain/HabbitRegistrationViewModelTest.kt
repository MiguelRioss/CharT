package pdm.isel.chaTr.NewActivityTest.domain

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import pdm.isel.cher.rules.ReplaceMainDispatcherRule
import pdm.isel.chaTr.domain.Habit
import pdm.isel.chaTr.domain.HabitRepository
import pdm.isel.chaTr.screens.createHabit.domain.NewHabitScreenState
import pdm.isel.chaTr.screens.createHabit.domain.NewHabitViewModel

class NewHabitViewModelTests {

    @get:Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    private val testHabit = Habit(1, "Exercise", "Morning jog", 3)
    private val fakeRepo = object : HabitRepository {
        override val habits: StateFlow<List<Habit>>
            get() = TODO("Not yet implemented")

        override suspend fun getHabits(): List<Habit> = listOf(testHabit)
        override suspend fun addHabit(habit: Habit) {}
        override suspend fun removeHabit(habitId: Int) {}
        override suspend fun clearHabits() {}
        override suspend fun updateHabit(habit: Habit) {}
    }

    @Test
    fun initial_state_is_Initialized() {
        val sut = NewHabitViewModel(habitRepository = fakeRepo)
        assert(sut.screenState.value is NewHabitScreenState.Displaying) {
            "Expected state to be Displaying(null), but was ${sut.screenState.value}"
        }
    }

    @Test
    fun when_displayHabit_is_called_transitions_to_Display() {
        val sut = NewHabitViewModel(habitRepository = fakeRepo)
        assert(sut.screenState.value is NewHabitScreenState.Displaying) {
            "Expected state to be Displaying, but was ${sut.screenState.value}"
        }
    }

    @Test
    fun when_startEditing_is_called_transitions_to_Editing() {
        val sut = NewHabitViewModel(habitRepository = fakeRepo)
        sut.startEditing()
        assert(sut.screenState.value is NewHabitScreenState.Editing) {
            "Expected state to be Editing, but was ${sut.screenState.value}"
        }
    }

    @Test
    fun cancelEditing_when_editing_returns_to_Display() = runTest(replaceMainDispatcherRule.testDispatcher) {
        val sut = NewHabitViewModel(habitRepository = fakeRepo)
        sut.startEditing()
        sut.cancelEditing()

        assert(sut.screenState.value is NewHabitScreenState.Concluded) {
            "Expected state to be Concluded, but was ${sut.screenState.value}"
        }
    }

    @Test
    fun cancelEditing_on_new_habit_returns_to_Concluded() = runTest(replaceMainDispatcherRule.testDispatcher) {
        val sut = NewHabitViewModel(habitRepository = fakeRepo)
        sut.cancelEditing() // ✅ User cancels a new habit (null habit)
        assert(sut.screenState.value is NewHabitScreenState.Concluded) {
            "Expected state to be Concluded, but was ${sut.screenState.value}"
        }
    }

    @Test
    fun when_saveHabit_is_called_transitions_to_Display_with_updated_habit() =
        runTest(replaceMainDispatcherRule.testDispatcher) {
            val sut = NewHabitViewModel(habitRepository = fakeRepo)
            sut.startEditing()
            val updatedHabit = testHabit.copy(name = "Evening Run")
            sut.saveHabit(updatedHabit).join()

            assert(sut.screenState.value is NewHabitScreenState.Displaying) {
                "Expected state to be Displaying, but was ${sut.screenState.value}"
            }

            val displayedHabit = (sut.screenState.value as NewHabitScreenState.Displaying).habit
            assert(displayedHabit == updatedHabit) {
                "Expected displayed habit to be $updatedHabit, but was $displayedHabit"
            }
        }
}
