package com.coredan.knowledgebaseapp.ui.placeholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coredan.knowledgebaseapp.core.network.KnowledgeBaseApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlaceholderViewModel
    @Inject
    constructor(
        private val apiClient: KnowledgeBaseApiClient,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PlaceholderUiState())
        val uiState: StateFlow<PlaceholderUiState> = _uiState.asStateFlow()

        init {
            refresh()
        }

        fun refresh() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                runCatching { apiClient.listSessions() }
                    .onSuccess { sessions ->
                        _uiState.update {
                            it.copy(isLoading = false, sessions = sessions, errorMessage = null)
                        }
                    }.onFailure { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "Failed to load sessions",
                            )
                        }
                    }
            }
        }
    }
