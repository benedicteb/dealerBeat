package dev.benedicte.dealerbeat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import java.util.*

import dev.benedicte.dealerbeat.Suit.*

class DeckTest {
    @Test
    fun `parsing cards from strings should throw IllegalArgumentException if invalid`() {
        assertThrows<IllegalArgumentException> { Deck.fromString("I2") }
        assertThrows<IllegalArgumentException> { Deck.fromString("C11") }
    }

    @Test
    fun `parsing cards from string produce the right cards`() {
        assertEqualsDeckWith(listOf(), Deck.fromString(""))
        assertEqualsDeckWith(listOf("A" of SPADES), Deck.fromString("SA"))
        assertEqualsDeckWith(listOf("A" of SPADES, "9" of HEARTS), Deck.fromString("SA, H9"))
    }

    @Test
    fun `drawing cards from an empty deck should throw OutOfCardsException`() {
        assertThrows<OutOfCardsException> { Deck(listOf()).drawCard() }
    }

    @Test
    fun `deck of cards should only contain 52 unique cards`() {
        val deck = Deck.generateShuffled()

        assertEquals(52, deck.size())
        assertOnlyDistinct(deck.cards)
    }

    @Test
    fun `newly generated deck of cards should have a random order`() {
        assertEquals(drawDeckWithSeed(1), drawDeckWithSeed(1))
        assertNotEquals(drawDeckWithSeed(1), drawDeckWithSeed(2))
    }
}

fun drawDeckWithSeed(seed: Long) = Deck.generateShuffled(Random(seed))

fun assertEqualsDeckWith(cards: List<Card>, deck: Deck) = assertEquals(cards, deck.cards)
