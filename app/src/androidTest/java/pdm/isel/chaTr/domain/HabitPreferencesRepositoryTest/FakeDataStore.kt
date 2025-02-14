package pdm.isel.chaTr.domain.HabitPreferencesRepositoryTest

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile

private var testDataStoreInstance: DataStore<Preferences>? = null

fun createTestDataStore(context: Context): DataStore<Preferences> {
    return testDataStoreInstance ?: synchronized(context) {
        testDataStoreInstance ?: PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("test_prefs")
        }.also { testDataStoreInstance = it }
    }
}
