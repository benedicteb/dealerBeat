package dev.benedicte.dealerbeat

import java.lang.Integer.parseInt

data class Card(val value: String) {
    val score = when (value) {
        "J", "Q", "K" -> 10
        "A" -> 11
        "2", "3", "4", "5", "6", "7", "8", "9", "10" -> parseInt(value)
        else -> throw IllegalArgumentException("Invalid card value")
    }
}

typealias Hand = List<Card>
fun Hand.calculateScore() = this.sumBy { it.score }

fun main(args: Array<String>) {
    println("Hello World!")
}