package com.coredan.knowledgebaseapp.ui.placeholder

import com.coredan.knowledgebaseapp.core.model.KbSession

data class PlaceholderUiState(
    val isLoading: Boolean = true,
    val sessions: List<KbSession> = emptyList(),
    val errorMessage: String? = null,
)
