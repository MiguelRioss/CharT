package pdm.isel.chaTr.domain

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HabitTest {
    @Test
    fun createHabit() {
        val habit = Habit(1, "Habit 1", "Description 1", 1)
        assertEquals(1, habit.id)
        assertEquals("Habit 1", habit.name)
        assertEquals("Description 1", habit.description)
        assertEquals(1, habit.frequencyOnDays)
    }
    @Test
    fun habit_name_not_empty() {
        assertThrows(IllegalArgumentException::class.java) {
            Habit(1, "", "Description 1", 1)
        }
    }
    @Test
    fun habit_frequency_on_days_greater_than_zero() {
        assertThrows(IllegalArgumentException::class.java) {
            Habit(1, "Habit 1", "Description 1", 0)
        }
    }
}