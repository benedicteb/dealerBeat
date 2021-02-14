enum class Suit(val asString: String) {
    CLUBS("C"),
    HEARTS("H"),
    DIAMONDS("D"),
    SPADES("S");

    companion object {
        fun fromString(string: String) = values().find { it.asString == string }
            ?: throw IllegalArgumentException("Invalid suit value '$string'")
    }
}

data class Card(val suit: Suit, val value: String) {
    val score = when (value) {
        "J", "Q", "K" -> 10
        "A" -> 11
        "2", "3", "4", "5", "6", "7", "8", "9", "10" -> Integer.parseInt(value)
        else -> throw IllegalArgumentException("Invalid card value '$value'")
    }

    override fun toString() = "${suit.asString}$value"
}

typealias Hand = List<Card>

fun Hand.score() = this.sumBy { it.score }
fun Hand.hasBlackjack() = this.score() == 21
fun Hand.mapToString() = this.joinToString(", ") { it.toString() }
infix fun Hand.drawsFrom(deck: Deck) = listOf(this, listOf(deck.drawCard())).flatten()

