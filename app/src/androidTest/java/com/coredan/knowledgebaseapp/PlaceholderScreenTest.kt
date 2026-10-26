package com.coredan.knowledgebaseapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.coredan.knowledgebaseapp.core.model.KbSession
import com.coredan.knowledgebaseapp.core.network.StubKnowledgeBaseApiClient
import com.coredan.knowledgebaseapp.ui.placeholder.PlaceholderScreen
import com.coredan.knowledgebaseapp.ui.placeholder.PlaceholderTestTags
import com.coredan.knowledgebaseapp.ui.placeholder.PlaceholderUiState
import com.coredan.knowledgebaseapp.ui.theme.KnowledgeBaseTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlaceholderScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsTitleAndDemoSession() {
        composeRule.setContent {
            KnowledgeBaseTheme {
                PlaceholderScreen(
                    state =
                        PlaceholderUiState(
                            isLoading = false,
                            sessions =
                                listOf(
                                    KbSession(
                                        id = StubKnowledgeBaseApiClient.DEMO_SESSION_ID,
                                        title = StubKnowledgeBaseApiClient.DEMO_SESSION_TITLE,
                                    ),
                                ),
                        ),
                )
            }
        }

        composeRule.onNodeWithTag(PlaceholderTestTags.ROOT).assertIsDisplayed()
        composeRule.onNodeWithTag(PlaceholderTestTags.TITLE).assertIsDisplayed()
        composeRule
            .onNodeWithTag(
                PlaceholderTestTags.SESSION_ROW_PREFIX + StubKnowledgeBaseApiClient.DEMO_SESSION_ID,
            ).assertIsDisplayed()
    }
}
