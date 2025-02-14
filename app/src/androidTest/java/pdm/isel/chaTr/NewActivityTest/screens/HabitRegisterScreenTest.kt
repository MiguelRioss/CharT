package pdm.isel.chaTr.NewActivityTest.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pdm.isel.chaTr.domain.Habit
import pdm.isel.chaTr.domain.HabitRepository
import pdm.isel.chaTr.screens.createHabit.NewHabitScreen
import pdm.isel.chaTr.screens.createHabit.domain.NewHabitScreenState
import pdm.isel.chaTr.screens.createHabit.domain.NewHabitViewModel
import pdm.isel.chaTr.util.TestTags // ✅ Import test tags

@RunWith(AndroidJUnit4::class)
class NewHabitScreenStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun createFakeViewModel(screenState: NewHabitScreenState) =
        NewHabitViewModel(
            initialScreenState = screenState,
            habitRepository = object : HabitRepository {
                override val habits: StateFlow<List<Habit>>
                    get() = TODO("Not yet implemented")

                override suspend fun getHabits(): List<Habit> {
                    TODO("Not yet implemented")
                }

                override suspend fun addHabit(habit: Habit) {
                    TODO("Not yet implemented")
                }

                override suspend fun removeHabit(habitId: Int) {
                    TODO("Not yet implemented")
                }

                override suspend fun clearHabits() {
                    TODO("Not yet implemented")
                }

                override suspend fun updateHabit(habit: Habit) {
                    TODO("Not yet implemented")
                }
            }

        )

    val fakeViewModel = createFakeViewModel(screenState = NewHabitScreenState.Initialized)
    @Test
    fun testHabitRegistrationScreenUIElementsExist() {
        // Launch the Composable
        composeTestRule.setContent {
            NewHabitScreen(viewModel = fakeViewModel, onBackIntent = {})}

        composeTestRule.onNodeWithTag(TestTags.HABIT_TITLE).assertExists()

        composeTestRule.onNodeWithTag(TestTags.HABIT_NAME_INPUT).assertExists()

        composeTestRule.onNodeWithTag(TestTags.HABIT_DESCRIPTION_INPUT).assertExists()

        composeTestRule.onNodeWithTag(TestTags.HABIT_FREQUENCY_INPUT).assertExists()
    }
}

