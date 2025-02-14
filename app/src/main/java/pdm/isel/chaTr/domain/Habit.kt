package pdm.isel.chaTr.domain

import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val frequencyOnDays: Int,
    val completed: Int = 0
) {
    init {
        require(name.isNotBlank()) { "Habit name cannot be empty" }
        require(frequencyOnDays > 0) { "Frequency on days must be greater than 0" }
        require(completed <= frequencyOnDays) { "Completed count cannot exceed frequency on days" }
    }
}

fun createHabit(name: String, description: String, frequencyOnDays: Int): Habit {
    return Habit(generateId(), name, description, frequencyOnDays)
}

fun generateId(): Int {
    return (0..Int.MAX_VALUE).random()
}
