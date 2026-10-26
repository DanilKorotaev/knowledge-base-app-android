package com.coredan.knowledgebaseapp.di

import com.coredan.knowledgebaseapp.core.network.KnowledgeBaseApiClient
import com.coredan.knowledgebaseapp.core.network.StubKnowledgeBaseApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideKnowledgeBaseApiClient(): KnowledgeBaseApiClient = StubKnowledgeBaseApiClient()
}
