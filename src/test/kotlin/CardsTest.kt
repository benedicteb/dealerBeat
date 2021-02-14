package dev.benedicte.dealerbeat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

import dev.benedicte.dealerbeat.Suit.*

class CardsTest {
    @Test
    fun `creating cards with invalid value should throw IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> { "I" of DIAMONDS }
        assertThrows<IllegalArgumentException> { "11" of DIAMONDS }
    }

    @Test
    fun `should be able to calculate score of hand`() {
        assertScore(0, listOf())
        assertScore(5, listOf("5" of HEARTS))
        assertScore(13, listOf("3" of CLUBS, "Q" of CLUBS))
        assertScore(21, listOf("A" of SPADES, "Q" of HEARTS))
        assertScore(22, listOf("A" of HEARTS, "A" of DIAMONDS))
        assertScore(30, listOf("K" of DIAMONDS, "Q" of HEARTS, "J" of SPADES))
    }

    @Test
    fun `lists of the same cards with same order should be equal`() {
        assertEquals(listOf("A" of SPADES, "Q" of HEARTS),
            listOf("A" of SPADES, "Q" of HEARTS))

        assertNotEquals(listOf("Q" of HEARTS, "A" of SPADES),
            listOf("A" of SPADES, "Q" of HEARTS))

        assertNotEquals(listOf("A" of CLUBS, "Q" of HEARTS),
            listOf("A" of SPADES, "Q" of HEARTS))
    }

    @Test
    fun `distinct should distinguish between suits and values`() {
        assertOnlyDistinct(listOf("2" of HEARTS, "2" of SPADES))
        assertContainsDuplicates(listOf("A" of HEARTS, "A" of HEARTS))
        assertOnlyDistinct(listOf("3" of HEARTS, "4" of HEARTS))
    }
}

infix fun String.of(suit: Suit) = Card(suit, this)

fun assertScore(score: Int, hand: Hand) = assertEquals(score, hand.score())
fun assertContainsDuplicates(deck: List<Card>) = assertNotEquals(deck.size, deck.distinct().size)
fun assertOnlyDistinct(deck: List<Card>) = assertEquals(deck.size, deck.distinct().size)
