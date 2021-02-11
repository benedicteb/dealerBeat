package dev.benedicte.dealerbeat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

class BlackjackTest {
    @Test
    fun  `should be able to calculate score of hand`() {
        assertEquals(0, listOf<Card>().calculateScore())

        assertEquals(5, listOf<Card>(Card("5")).calculateScore())

        assertEquals(13, listOf<Card>(Card("3"), Card("Q")).calculateScore())

        assertEquals(21, listOf<Card>(Card("A"), Card("Q")).calculateScore())

        assertEquals(22, listOf<Card>(Card("A"), Card("A")).calculateScore())

        assertEquals(30, listOf<Card>(Card("K"),
            Card("Q"),
            Card("J")).calculateScore())

        assertThrows<IllegalArgumentException> { Card("I") }
    }
}