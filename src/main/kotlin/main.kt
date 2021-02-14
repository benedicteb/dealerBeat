package dev.benedicte.dealerbeat

import java.lang.Integer.parseInt
import java.util.Random

enum class Suit(val asString: String) {
    CLUBS("C"), HEARTS("H"), DIAMONDS("D"), SPADES("S"),
}

data class Card(val suit: Suit, val value: String) {
    val score = when (value) {
        "J", "Q", "K" -> 10
        "A" -> 11
        "2", "3", "4", "5", "6", "7", "8", "9", "10" -> parseInt(value)
        else -> throw IllegalArgumentException("Invalid card value")
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
infix fun Hand.drawsFrom(deck: Deck) = listOf(this, listOf(deck.drawCard())).flatten()

data class Deck(var cards: List<Card>) {
    companion object {
        fun generateShuffled(random: Random = Random()): Deck = Deck(Suit.values().flatMap { suit ->
            listOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A").map { value ->
                Card(suit, value) } }.shuffled(random))
    }

    fun drawCard(): Card {
        val card = cards.first()

        this.cards = cards.subList(1, cards.size)

        return card
    }

    fun size() = this.cards.size
}

fun main() {
    val deck = Deck.generateShuffled()

    println(deck.cards.map { it.toString() }.joinToString(","))
}
