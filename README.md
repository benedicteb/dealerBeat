# dealerBeat

A simple blackjack simulator!

## Running

This project runs on Java 11.

Simulate a game with the example deck of cards in `exampleDeck.txt` by running:

```shell
$ ./gradlew run --args="exampleDeck.txt"
```

It should produce this output:

```shell
$ ./gradlew run --args="exampleDeck.txt"

> Task :run
sam
sam: CA, H9
dealer: D5, HQ, S8
```

Supply your own file by altering `exampleDeck.txt` to be the path to your own
deck of cards file. The file must be on the following format (`<suit><value>`):

```
CA, D4, H7, SJ, ..., S5, S9, D10
```

Where suit is `C` for clubs, `D` for diamonds, `H` for hearts and `S` for spades.
While the value can be from `2-10` or `J,Q,K,A`.

### Tests

Run the tests with Gradle:

```shell
$ ./gradlew test
```