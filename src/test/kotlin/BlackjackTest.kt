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
    fun  `creating cards with invalid value should throw IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> { "I" of DIAMONDS }
        assertThrows<IllegalArgumentException> { "11" of DIAMONDS }
    }

    @Test
    fun  `parsing cards from strings should throw IllegalArgumentException if invalid`() {
        assertThrows<IllegalArgumentException> { Deck.fromString("I2") }
        assertThrows<IllegalArgumentException> { Deck.fromString("C11") }
    }

    @Test
    fun  `parsing cards from string produce the right cards`() {
        assertEqualsDeckWith(listOf(), Deck.fromString(""))
        assertEqualsDeckWith(listOf("A" of SPADES), Deck.fromString("SA"))
        assertEqualsDeckWith(listOf("A" of SPADES, "9" of HEARTS), Deck.fromString("SA, H9"))
    }

    @Test
    fun  `should be able to calculate score of hand`() {
        assertScore(0, listOf())
        assertScore(5, listOf("5" of HEARTS))
        assertScore(13, listOf("3" of CLUBS, "Q" of CLUBS))
        assertScore(21, listOf("A" of SPADES, "Q" of HEARTS))
        assertScore(22, listOf("A" of HEARTS, "A" of DIAMONDS))
        assertScore(30, listOf("K" of DIAMONDS, "Q" of HEARTS, "J" of SPADES))
    }

    @Test
    fun  `drawing cards from an empty deck should throw OutOfCardsException`() {
        assertThrows<OutOfCardsException> { Deck(listOf()).drawCard() }
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

    @Test
    fun  `sam should stop drawing cards when score reaches 17 and dealer when higher score than sam`() {
        val deck = Deck(listOf("10" of CLUBS, "K" of DIAMONDS, "5" of HEARTS, "5" of SPADES, "2" of SPADES,
            "2" of HEARTS, "2" of CLUBS))
        val result = Blackjack.play(deck)

        assertScore(17, result.sam)
        assertScore(19, result.dealer)
    }

    @Test
    fun  `sam loses if he draws a score higher than 21`() {
        val deck = Deck(listOf("10" of CLUBS, "K" of DIAMONDS, "5" of HEARTS, "5" of SPADES, "K" of SPADES))
        val result = Blackjack.play(deck)

        assertDealerWon(result)
        assertScore(25, result.sam)
    }

    @Test
    fun  `dealer loses if they draw a score higher than 21`() {
        val deck = Deck(listOf("10" of CLUBS, "K" of DIAMONDS, "5" of HEARTS, "5" of SPADES, "2" of SPADES,
            "2" of HEARTS, "A" of SPADES))
        val result = Blackjack.play(deck)

        assertSamWon(result)
        assertScore(28, result.dealer)
    }

    private fun drawDeckWithSeed(seed: Long) = Deck.generateShuffled(Random(seed))

    private fun assertEqualsDeckWith(cards: List<Card>, deck: Deck) = assertEquals(cards, deck.cards)
    private fun assertSamWon(result: GameResult) = assertTrue(result.samWon)
    private fun assertDealerWon(result: GameResult) = assertFalse(result.samWon)
    private fun assertOnlyDistinct(deck: List<Card>) = assertEquals(deck.size, deck.distinct().size)
    private fun assertContainsDuplicates(deck: List<Card>) = assertNotEquals(deck.size, deck.distinct().size)
    private fun assertScore(score: Int, hand: Hand) = assertEquals(score, hand.score())
}
