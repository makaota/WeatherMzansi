package com.makaota.weathermzansi.data.theme_datastore
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun setThemeSetting(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDark
            preferences[HAS_SET_THEME] = true
        }
    }

    fun getThemeSetting(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            if (preferences[HAS_SET_THEME] == true) {
                preferences[IS_DARK_THEME]
            } else {
                null // means system default
            }
        }
    }

    companion object {
        private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        private val HAS_SET_THEME = booleanPreferencesKey("has_set_theme")
    }
}



