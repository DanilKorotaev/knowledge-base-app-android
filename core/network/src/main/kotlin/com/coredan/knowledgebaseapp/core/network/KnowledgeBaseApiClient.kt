package com.coredan.knowledgebaseapp.core.network

import com.coredan.knowledgebaseapp.core.model.KbSession

/**
 * Abstraction over KB App API HTTP surface.
 * Production Retrofit implementation lands with the contract-sync / auth tasks.
 */
interface KnowledgeBaseApiClient {
    suspend fun listSessions(): List<KbSession>
}
