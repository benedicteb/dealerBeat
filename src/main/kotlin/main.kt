package dev.benedicte.dealerbeat

import java.io.File
import java.io.FileNotFoundException
import java.lang.Integer.parseInt
import java.lang.RuntimeException
import java.util.Random
import kotlin.system.exitProcess

enum class Suit(val asString: String) {
    CLUBS("C"), HEARTS("H"), DIAMONDS("D"), SPADES("S");

    companion object {
        fun fromString(string: String) = values().find { it.asString == string }
            ?: throw IllegalArgumentException("Invalid suit value '$string'")
    }
}

data class Card(val suit: Suit, val value: String) {
    val score = when (value) {
        "J", "Q", "K" -> 10
        "A" -> 11
        "2", "3", "4", "5", "6", "7", "8", "9", "10" -> parseInt(value)
        else -> throw IllegalArgumentException("Invalid card value '$value'")
    }

    override fun toString() = "${suit.asString}$value"
}
infix fun String.of(suit: Suit) = Card(suit, this)

data class GameResult(val sam: Hand, val dealer: Hand, val samWon: Boolean)

object Blackjack {
    fun play(deck: Deck): GameResult {
        var sam: Hand = listOf(deck.drawCard())
        var dealer: Hand = listOf(deck.drawCard())

        sam = sam drawsFrom deck
        dealer = dealer drawsFrom deck

        while (sam.score() < 17) {
            sam = sam drawsFrom deck
        }

        if (sam.score() > 21) {
            return GameResult(sam, dealer, false)
        }

        if (sam.hasBlackjack()) {
            return GameResult(sam, dealer, true)
        }

        while (dealer.score() <= sam.score()) {
            dealer = dealer drawsFrom deck
        }

        if (dealer.score() > 21) {
            return GameResult(sam, dealer, true)
        }

        if (dealer.hasBlackjack()) {
            return GameResult(sam, dealer, false)
        }

        return GameResult(sam, dealer, sam.score() > dealer.score())
    }
}

typealias Hand = List<Card>
fun Hand.score() = this.sumBy { it.score }
fun Hand.hasBlackjack() = this.score() == 21
fun Hand.mapToString() = this.joinToString(", ") { it.toString() }
infix fun Hand.drawsFrom(deck: Deck) = listOf(this, listOf(deck.drawCard())).flatten()

data class Deck(var cards: List<Card>) {
    companion object {
        fun generateShuffled(random: Random = Random()): Deck = Deck(Suit.values().flatMap { suit ->
            listOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A").map { value ->
                Card(suit, value) } }.shuffled(random))

        fun fromFile(path: String): Deck {
            return fromString(File(path).readText())
        }

        fun fromString(cardsString: String): Deck {
            return Deck(cardsString.split(", ").filter { it.isNotEmpty() }.map { it.trim() }.map {
                /*
                 * it.drop(1) returns the string excluding the first character
                 * it.take(1) returns the first character of the string
                 */
                it.drop(1) of Suit.fromString(it.take(1))
            })
        }
    }

    fun drawCard(): Card {
        if (cards.isEmpty()) {
            throw OutOfCardsException()
        }

        val card = cards.first()

        this.cards = cards.subList(1, cards.size)

        return card
    }

    fun size() = this.cards.size
    fun isEmpty() = this.cards.isEmpty()
}

class OutOfCardsException : RuntimeException()

fun main(args: Array<String>) {
    val deck: Deck

    if (args.isEmpty()) {
        deck = Deck.generateShuffled()
    } else {
        val filePath = args[0]

        try {
            deck = Deck.fromFile(filePath)
        } catch (e: IllegalArgumentException) {
            println("Unable to parse cards from '$filePath'\nError: ${e.message}")
            exitProcess(1)
        } catch (e: FileNotFoundException) {
            println("File not found '$filePath'")
            exitProcess(1)
        }

        if (deck.isEmpty()) {
            println("No cards found in '$filePath'")
            exitProcess(1)
        }
    }

    val gameResult: GameResult

    try {
        gameResult = Blackjack.play(deck)
    } catch (e: OutOfCardsException) {
        println("Ran out of cards in the deck while playing")
        exitProcess(1)
    }

    val winner = if (gameResult.samWon) "sam" else "dealer"

    println(winner)
    println("sam: ${gameResult.sam.mapToString()}")
    println("dealer: ${gameResult.dealer.mapToString()}")
}
