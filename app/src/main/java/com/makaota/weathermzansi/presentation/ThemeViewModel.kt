// ThemeViewModel.kt - Manages the theme state across the app
package com.makaota.weathermzansi.presentation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makaota.weathermzansi.data.theme_datastore.DataStoreManager
import kotlinx.coroutines.launch

class ThemeViewModel(private val dataStoreManager: DataStoreManager,
                     private val isSystemDark: Boolean ) : ViewModel() {

    private val _isDarkTheme = MutableLiveData<Boolean>()
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme


    init {
        viewModelScope.launch {
            try {
                dataStoreManager.getThemeSetting().collect { savedTheme ->
                    _isDarkTheme.postValue(savedTheme ?: isSystemDark)
                }
            } catch (e: Exception) {
                _isDarkTheme.postValue(isSystemDark)
            }
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setThemeSetting(isDark)
        }
    }
}


class ThemeViewModelFactory(private val dataStoreManager: DataStoreManager,
                            private val isSystemDark: Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ThemeViewModel(dataStoreManager, isSystemDark) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



