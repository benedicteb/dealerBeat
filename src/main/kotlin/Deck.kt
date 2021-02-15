import java.io.File
import java.util.*

data class Deck(var cards: List<Card>) {
    companion object {
        fun generateShuffled(random: Random = Random()): Deck = Deck(Suit.values().flatMap { suit ->
            listOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A").map { value ->
                Card(suit, value)
            }
        }.shuffled(random))

        fun fromFile(path: String): Deck {
            return fromString(File(path).readText())
        }

        fun fromString(cardsString: String): Deck {
            return Deck(cardsString.trim().split(",").filter { it.isNotEmpty() }.map { it.trim() }.map {
                /*
                 * it.drop(1) returns the string excluding the first character
                 * it.take(1) returns the first character of the string
                 */
                val suit = Suit.fromString(it.take(1))
                val value = it.drop(1)

                Card(suit, value)
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
