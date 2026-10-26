package com.coredan.knowledgebaseapp.ui.placeholder

import app.cash.turbine.test
import com.coredan.knowledgebaseapp.core.network.StubKnowledgeBaseApiClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlaceholderViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads stub demo session into ui state`() =
        runTest(dispatcher) {
            val viewModel = PlaceholderViewModel(StubKnowledgeBaseApiClient())

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.isLoading).isFalse()
                assertThat(state.sessions).hasSize(1)
                assertThat(state.sessions.first().title)
                    .isEqualTo(StubKnowledgeBaseApiClient.DEMO_SESSION_TITLE)
                assertThat(state.errorMessage).isNull()
            }
        }
}
