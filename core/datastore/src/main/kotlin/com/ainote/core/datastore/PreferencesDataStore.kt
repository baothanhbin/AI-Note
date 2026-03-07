package com.ainote.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.ainote.core.datastore.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class PreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val USE_DARK_MODE = booleanPreferencesKey("use_dark_mode")
        val USE_MARKDOWN_PREVIEW = booleanPreferencesKey("use_markdown_preview")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val useDarkMode = preferences[Keys.USE_DARK_MODE] ?: false
            val useMarkdownPreview = preferences[Keys.USE_MARKDOWN_PREVIEW] ?: true
            UserPreferences(useDarkMode, useMarkdownPreview)
        }

    suspend fun updateDarkMode(useDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.USE_DARK_MODE] = useDarkMode
        }
    }

    suspend fun updateMarkdownPreview(useMarkdownPreview: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.USE_MARKDOWN_PREVIEW] = useMarkdownPreview
        }
    }
}
