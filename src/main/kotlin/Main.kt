package dev.benedicte.dealerbeat

import java.io.FileNotFoundException
import kotlin.system.exitProcess

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
