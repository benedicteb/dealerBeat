package dev.benedicte.dealerbeat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.util.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

import dev.benedicte.dealerbeat.Suit.*

class BlackjackTest {
    @Test
    fun  `should be able to calculate score of hand`() {
        assertScore(0, listOf<Card>())
        assertScore(5, listOf<Card>("5" of HEARTS))
        assertScore(13, listOf<Card>("3" of CLUBS, "Q" of CLUBS))
        assertScore(21, listOf<Card>("A" of SPADES, "Q" of HEARTS))
        assertScore(22, listOf<Card>("A" of HEARTS, "A" of DIAMONDS))
        assertScore(30, listOf<Card>("K" of DIAMONDS, "Q" of HEARTS, "J" of SPADES))

        assertThrows<IllegalArgumentException> { "I" of DIAMONDS }
    }

    @Test
    fun  `deck of cards should be unique and contain 52`() {
        // Verify that distinct() can distinguish between suits and values
        assertOnlyDistinct(listOf("2" of HEARTS, "2" of SPADES))
        assertContainsDuplicates(listOf("A" of HEARTS, "A" of HEARTS))
        assertOnlyDistinct(listOf("3" of HEARTS, "4" of HEARTS))

        val deck = Deck.generateShuffled()

        // Verify length of deck
        assertEquals(52, deck.size())

        // Verify that the deck only has unique cards
        assertOnlyDistinct(deck.cards)
    }

    @Test
    fun  `deck of cards should be shuffled`() {
        assertEquals(drawDeckWithSeed(1), drawDeckWithSeed(1))
        assertNotEquals(drawDeckWithSeed(1), drawDeckWithSeed(2))
    }

    @Test
    fun  `playing a game with a given deck should produce given outcome`() {
        val deck = Deck(listOf("A" of CLUBS, "5" of DIAMONDS, "9" of HEARTS, "Q" of HEARTS, "8" of SPADES))
        val result = Blackjack.play(deck)

        assertEquals(listOf("A" of CLUBS, "9" of HEARTS), result.sam)
        assertEquals(listOf("5" of DIAMONDS, "Q" of HEARTS, "8" of SPADES), result.dealer)
    }

    private fun drawDeckWithSeed(seed: Long) = Deck.generateShuffled(Random(seed))

    private fun assertOnlyDistinct(deck: List<Card>) = assertEquals(deck.size, deck.distinct().size)
    private fun assertContainsDuplicates(deck: List<Card>) = assertNotEquals(deck.size, deck.distinct().size)
    private fun assertScore(score: Int, hand: Hand) = assertEquals(score, hand.score())
}
