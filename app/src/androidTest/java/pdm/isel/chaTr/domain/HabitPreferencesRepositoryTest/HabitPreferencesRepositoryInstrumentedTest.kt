package pdm.isel.chaTr.domain.HabitPreferencesRepositoryTest

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pdm.isel.chaTr.domain.Habit
import pdm.isel.chaTr.domain.HabitPreferencesRepository

@RunWith(AndroidJUnit4::class)
class HabitPreferencesRepositoryInstrumentedTest {

    private lateinit var repository: HabitPreferencesRepository
    private lateinit var testDataStore: DataStore<Preferences>
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        testDataStore = createTestDataStore(context)
        repository = HabitPreferencesRepository(testDataStore)
    }
    @After
    fun afterTest() = runTest {
        repository.clearHabits()
    }

    @Test
    fun testAddHabitAndRetrieveIt() = runTest {
        val habit = Habit(id = 1, name = "Exercise", description = "Daily workout", frequencyOnDays = 5)

        repository.addHabit(habit)

        val storedHabits = repository.getHabits()
        assertEquals(1, storedHabits.size)
        assertEquals(habit, storedHabits[0])
    }

    @Test
    fun testRemoveHabitAndRetrieveIt() = runTest {
        val habit = Habit(id = 1, name = "Exercise", description = "Daily workout", frequencyOnDays = 5)
        repository.addHabit(habit)

        repository.removeHabit(1)

        val storedHabits = repository.getHabits()
        assertEquals(0, storedHabits.size)
    }

    @Test
    fun testMultipleHabits() = runTest {
        val habit1 = Habit(1, "Exercise", "Workout", 5)
        val habit2 = Habit(2, "Reading", "Read books", 3)

        repository.addHabit(habit1)
        repository.addHabit(habit2)

        val storedHabits = repository.getHabits()
        assertEquals(2, storedHabits.size)
        assertEquals(listOf(habit1, habit2), storedHabits)
    }
    @Test
    fun testUpdateHabitName() = runTest {
        val habit = Habit(id = 1, name = "Exercise", description = "Daily workout", frequencyOnDays = 5)
        repository.addHabit(habit)

        val updatedHabit = habit.copy(name = "Morning Jog")
        repository.updateHabit(updatedHabit)

        val storedHabits = repository.getHabits()
        assertEquals("Morning Jog", storedHabits[0].name)
    }
}
