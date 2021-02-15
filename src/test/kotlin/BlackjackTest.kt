import Suit.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BlackjackTest {
    @Test
    fun `playing a game with a given deck should produce given outcome`() {
        val deck = Deck(listOf("A" of CLUBS, "5" of DIAMONDS, "9" of HEARTS, "Q" of HEARTS, "8" of SPADES))
        val result = Blackjack.play(deck)

        assertSamWon(result)
    }

    @Test
    fun `given a deck sam should be given first and third card and dealer second and fourth`() {
        val deck = Deck(listOf("A" of CLUBS, "A" of DIAMONDS, "Q" of HEARTS, "Q" of SPADES))
        val result = Blackjack.play(deck)

        assertEquals(listOf("A" of CLUBS, "Q" of HEARTS), result.sam)
        assertEquals(listOf("A" of DIAMONDS, "Q" of SPADES), result.dealer)
    }

    @Test
    fun `if both players start with blackjack sam should win`() {
        val deck = Deck(listOf("A" of CLUBS, "A" of DIAMONDS, "Q" of HEARTS, "Q" of SPADES))
        val result = Blackjack.play(deck)

        assertSamWon(result)
    }

    @Test
    fun `if both players start with 22 dealer should win`() {
        val deck = Deck(listOf("A" of CLUBS, "A" of DIAMONDS, "A" of HEARTS, "A" of SPADES))
        val result = Blackjack.play(deck)

        assertDealerWon(result)
    }

    @Test
    fun `if sam starts with 22 dealer should win`() {
        val deck = Deck(listOf("A" of CLUBS, "3" of DIAMONDS, "A" of HEARTS, "4" of SPADES))
        val result = Blackjack.play(deck)

        assertDealerWon(result)
    }

    @Test
    fun `sam should stop drawing cards when score reaches 17 and dealer when higher score than sam`() {
        val deck = Deck(
            listOf(
                "10" of CLUBS, "K" of DIAMONDS, "5" of HEARTS, "5" of SPADES, "2" of SPADES,
                "2" of HEARTS, "2" of CLUBS, "2" of DIAMONDS, "6" of CLUBS
            )
        )
        val result = Blackjack.play(deck)

        assertScore(17, result.sam)
        assertScore(19, result.dealer)
    }

    @Test
    fun `sam loses if he draws a score higher than 21`() {
        val deck = Deck(listOf("10" of CLUBS, "K" of DIAMONDS, "5" of HEARTS, "5" of SPADES, "K" of SPADES))
        val result = Blackjack.play(deck)

        assertDealerWon(result)
        assertScore(25, result.sam)
    }

    @Test
    fun `dealer loses if they draw a score higher than 21`() {
        val deck = Deck(
            listOf(
                "10" of CLUBS, "K" of DIAMONDS, "5" of HEARTS, "5" of SPADES, "2" of SPADES,
                "2" of HEARTS, "A" of SPADES
            )
        )
        val result = Blackjack.play(deck)

        assertSamWon(result)
        assertScore(28, result.dealer)
    }

    @Test
    fun `dealer wins if they have the highest score at the end of the game`() {
        val deck = Deck(
            listOf(
                "10" of CLUBS, "10" of DIAMONDS, "5" of HEARTS, "5" of SPADES, "2" of SPADES,
                "2" of HEARTS, "2" of SPADES
            )
        )
        val result = Blackjack.play(deck)

        assertScore(17, result.sam)
        assertScore(19, result.dealer)

        assertDealerWon(result)
    }

}

fun assertSamWon(result: GameResult) = assertTrue(result.samWon, "sam did not win")
fun assertDealerWon(result: GameResult) = assertFalse(result.samWon, "dealer did not win")
