package dev.benedicte.dealerbeat

import java.lang.Integer.parseInt
import java.util.Random

enum class Suit() {
    CLUBS, HEARTS, DIAMONDS, SPADES,
}

data class Card(val suit: Suit, val value: String) {
    val score = when (value) {
        "J", "Q", "K" -> 10
        "A" -> 11
        "2", "3", "4", "5", "6", "7", "8", "9", "10" -> parseInt(value)
        else -> throw IllegalArgumentException("Invalid card value")
    }
}

infix fun String.of(suit: Suit) = Card(suit, this)

typealias Hand = List<Card>
fun Hand.calculateScore() = this.sumBy { it.score }

fun drawDeck(random: Random = Random()) = Suit.values().flatMap { suit ->
    listOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A").map { value ->
        Card(suit, value) } }.shuffled(random)

fun main() {
    println("Hello World!")
}
