package pdm.isel.chaTr

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import pdm.isel.chaTr.domain.HabitPreferencesRepository
import pdm.isel.chaTr.domain.HabitRepository

const val APPLICATION_TAG = "CHaTr"

// ✅ Properly define DataStore as an extension of Context
private val Application.dataStore: DataStore<Preferences> by preferencesDataStore(name = "habbitsPreference")

interface DependenciesContainer {
    val preferencesDataStore: DataStore<Preferences>
    val habitsRepository: HabitRepository
}

class CHaTrApplication : Application(), DependenciesContainer {

    override val preferencesDataStore: DataStore<Preferences> by lazy { dataStore }

    override val habitsRepository: HabitRepository by lazy { HabitPreferencesRepository(preferencesDataStore) }
}
