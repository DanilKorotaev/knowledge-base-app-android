package com.coredan.knowledgebaseapp.ui.placeholder

import app.cash.turbine.test
import com.coredan.knowledgebaseapp.core.model.KbSession
import com.coredan.knowledgebaseapp.core.network.KnowledgeBaseApiClient
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
class PlaceholderViewModelErrorTest {
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
    fun `exposes error message when api fails`() =
        runTest(dispatcher) {
            val viewModel =
                PlaceholderViewModel(
                    object : KnowledgeBaseApiClient {
                        override suspend fun listSessions(): List<KbSession> {
                            error("boom")
                        }
                    },
                )

            viewModel.uiState.test {
                val state = awaitItem()
                assertThat(state.isLoading).isFalse()
                assertThat(state.errorMessage).isEqualTo("boom")
                assertThat(state.sessions).isEmpty()
            }
        }
}
