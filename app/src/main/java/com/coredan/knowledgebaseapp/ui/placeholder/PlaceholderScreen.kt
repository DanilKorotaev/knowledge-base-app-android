package com.coredan.knowledgebaseapp.ui.placeholder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

object PlaceholderTestTags {
    const val ROOT = "placeholder.root"
    const val TITLE = "placeholder.title"
    const val SESSION_ROW_PREFIX = "placeholder.session."
}

@Composable
fun PlaceholderRoute(
    viewModel: PlaceholderViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    PlaceholderScreen(state = state)
}

@Composable
fun PlaceholderScreen(
    state: PlaceholderUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag(PlaceholderTestTags.ROOT),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Knowledge Base",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.testTag(PlaceholderTestTags.TITLE),
        )
        when {
            state.isLoading -> CircularProgressIndicator()
            state.errorMessage != null ->
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                )
            else ->
                state.sessions.forEach { session ->
                    Text(
                        text = session.title,
                        modifier = Modifier.testTag(PlaceholderTestTags.SESSION_ROW_PREFIX + session.id),
                    )
                }
        }
    }
}
