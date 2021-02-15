import Suit.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

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
        assertEqualsDeckWith(listOf("A" of SPADES, "9" of HEARTS, "10" of CLUBS), Deck.fromString("SA, H9, C10"))
        assertEqualsDeckWith(listOf("A" of SPADES, "9" of HEARTS), Deck.fromString("SA,H9,"))
        assertEqualsDeckWith(listOf("A" of SPADES, "9" of HEARTS), Deck.fromString(",SA,H9,"))
        assertEqualsDeckWith(listOf("A" of SPADES, "9" of HEARTS), Deck.fromString(",SA  ,H9,,,"))
        assertEqualsDeckWith(listOf("A" of SPADES, "9" of HEARTS, "10" of CLUBS), Deck.fromString(",SA  ,H9,,C10    ,"))
    }

    @Test
    fun `drawing cards from an empty deck should throw OutOfCardsException`() {
        assertThrows<OutOfCardsException> { Deck(listOf()).drawCard() }
    }

    @Test
    fun `newly generated deck of cards should only contain 52 unique cards`() {
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
