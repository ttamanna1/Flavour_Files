package com.example.flavourfiles

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UiState(
    val favoriteRecipes: Set<Int> = setOf(),
    val instructionsClicked: Boolean = false,
    val methodClicked: Boolean = false
)

class FlavourFilesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun toggleFavorite(recipe: Int) {
        _uiState.update { currentState ->
            val newFavorites = if (currentState.favoriteRecipes.contains(recipe)) {
                currentState.favoriteRecipes - recipe
            } else {
                currentState.favoriteRecipes + recipe
            }
            currentState.copy(favoriteRecipes = newFavorites)
        }
    }

    fun toggleInstructions() {
        _uiState.update { it.copy( instructionsClicked = !it.instructionsClicked)
        }
    }
    fun toggleMethods() {
        _uiState.update { it.copy( methodClicked = !it.methodClicked)
        }
    }
}