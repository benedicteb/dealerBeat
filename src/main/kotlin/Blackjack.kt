package dev.benedicte.dealerbeat

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
