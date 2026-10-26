package com.coredan.knowledgebaseapp.core.network

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class StubKnowledgeBaseApiClientTest {
    private val client = StubKnowledgeBaseApiClient()

    @Test
    fun `listSessions returns seeded demo session`() =
        runTest {
            val sessions = client.listSessions()

            assertThat(sessions).hasSize(1)
            assertThat(sessions.first().id).isEqualTo(StubKnowledgeBaseApiClient.DEMO_SESSION_ID)
            assertThat(sessions.first().title).isEqualTo(StubKnowledgeBaseApiClient.DEMO_SESSION_TITLE)
        }
}
