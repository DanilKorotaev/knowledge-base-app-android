package com.coredan.knowledgebaseapp.core.network

import com.coredan.knowledgebaseapp.core.model.KbSession

/**
 * In-memory client for UI tests and offline-demo until a base URL + token exist.
 */
class StubKnowledgeBaseApiClient : KnowledgeBaseApiClient {
    override suspend fun listSessions(): List<KbSession> =
        listOf(
            KbSession(
                id = DEMO_SESSION_ID,
                title = DEMO_SESSION_TITLE,
            ),
        )

    companion object {
        const val DEMO_SESSION_ID = "demo"
        const val DEMO_SESSION_TITLE = "Demo session"
    }
}
