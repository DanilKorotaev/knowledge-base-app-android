package com.coredan.knowledgebaseapp.core.model

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class KbSessionTest {
    @Test
    fun `session holds id and title`() {
        val session = KbSession(id = "demo", title = "Demo session")

        assertThat(session.id).isEqualTo("demo")
        assertThat(session.title).isEqualTo("Demo session")
    }
}
