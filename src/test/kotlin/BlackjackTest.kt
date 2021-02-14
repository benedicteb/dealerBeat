package dev.benedicte.dealerbeat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import java.util.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
    fun  `lists of the same cards with same order should be equal`() {
        assertEquals(listOf("A" of SPADES, "Q" of HEARTS),
            listOf("A" of SPADES, "Q" of HEARTS))

        assertNotEquals(listOf("Q" of HEARTS, "A" of SPADES),
            listOf("A" of SPADES, "Q" of HEARTS))

        assertNotEquals(listOf("A" of CLUBS, "Q" of HEARTS),
            listOf("A" of SPADES, "Q" of HEARTS))
    }

    @Test
    fun  `distinct should distinguish between suits and values`() {
        assertOnlyDistinct(listOf("2" of HEARTS, "2" of SPADES))
        assertContainsDuplicates(listOf("A" of HEARTS, "A" of HEARTS))
        assertOnlyDistinct(listOf("3" of HEARTS, "4" of HEARTS))
    }

    @Test
    fun  `deck of cards should only contain 52 unique cards`() {
        val deck = Deck.generateShuffled()

        assertEquals(52, deck.size())
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

        assertSamWon(result)
    }

    @Test
    fun  `given a deck sam should be given first and third card and dealer second and fourth`() {
        val deck = Deck(listOf("A" of CLUBS, "A" of DIAMONDS, "Q" of HEARTS, "Q" of SPADES))
        val result = Blackjack.play(deck)

        assertEquals(listOf("A" of CLUBS, "Q" of HEARTS), result.sam)
        assertEquals(listOf("A" of DIAMONDS, "Q" of SPADES), result.dealer)
    }

    @Test
    fun  `if both players start with blackjack sam should win`() {
        val deck = Deck(listOf("A" of CLUBS, "A" of DIAMONDS, "Q" of HEARTS, "Q" of SPADES))
        val result = Blackjack.play(deck)

        assertSamWon(result)
    }

    @Test
    fun  `if both players start with 22 dealer should win`() {
        val deck = Deck(listOf("A" of CLUBS, "A" of DIAMONDS, "A" of HEARTS, "A" of SPADES))
        val result = Blackjack.play(deck)

        assertDealerWon(result)
    }

    @Test
    fun  `if sam starts with 22 dealer should win`() {
        val deck = Deck(listOf("A" of CLUBS, "3" of DIAMONDS, "A" of HEARTS, "4" of SPADES))
        val result = Blackjack.play(deck)

        assertDealerWon(result)
    }

    private fun drawDeckWithSeed(seed: Long) = Deck.generateShuffled(Random(seed))

    private fun assertSamWon(result: GameResult) = assertTrue(result.samWon)
    private fun assertDealerWon(result: GameResult) = assertFalse(result.samWon)
    private fun assertOnlyDistinct(deck: List<Card>) = assertEquals(deck.size, deck.distinct().size)
    private fun assertContainsDuplicates(deck: List<Card>) = assertNotEquals(deck.size, deck.distinct().size)
    private fun assertScore(score: Int, hand: Hand) = assertEquals(score, hand.score())
}
