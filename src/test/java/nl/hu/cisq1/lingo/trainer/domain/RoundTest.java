package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidFeedBackException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @Test
    @DisplayName("make 5 guesses and then try to make another guess, asserting a throw")
    void noMoreAttempts() {
        Round round = new Round("woord", 1);
        round.guess("wort5");
        round.guess("wort1");
        round.guess("wort2");
        round.guess("wort3");
        round.guess("wort4");
        assertThrows(InvalidGuessException.playerDefeated().getClass(), () ->  round.guess("woord"));
    }
    @Test
    @DisplayName("when word has been guessed, round is over and no more guesses can be made. If so, an Exceptio will be thrown")
    void wordGuessedNoMoreAttempts() {
        Round round = new Round("woord", 1);
        round.guess("woord");
        round.giveHint();
        assertThrows(InvalidGuessException.noMoreGuesses().getClass(), () ->  round.guess("woord"));
    }

    @Test
    @DisplayName("Start new Round and create hint to assert it is the starting hint")
    void giveHintWithNoAttempt() {
        Round round = new Round("woord", 1);
        assertEquals("w....", round.giveHint());
    }
    @Test
    @DisplayName("validates that the right hint is given after a guess")
    void giveHintWithAttempt() {
        Round round = new Round("woord", 1);
        round.guess("waare");
        assertEquals("w..r.", round.giveHint());
    }

    @ParameterizedTest
    @MethodSource("provideGuessExamples")
    @DisplayName("checks if all marks are right -- parameterized")
    void marksAreValid(String wordToGuess, String attempt, Feedback expectedFeedback) {
        Round round = new Round(wordToGuess, 1);
        round.guess(attempt);
        Feedback feedback = round.getFeedbackHistory().get(0);
        assertEquals(expectedFeedback, feedback);
    }
    static Stream<Arguments> provideGuessExamples() throws InvalidFeedBackException {
        return Stream.of(
                Arguments.of("woord", "woord", new Feedback("woord",  List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))),
                Arguments.of("woord", "drooo", new Feedback("drooo",  List.of(Mark.PRESENT, Mark.PRESENT, Mark.CORRECT, Mark.PRESENT, Mark.ABSENT))),
                Arguments.of("woord", "wroks", new Feedback("wroks",  List.of(Mark.CORRECT, Mark.PRESENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT))),
                Arguments.of("woord", "boors", new Feedback("boors",  List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT))),
                Arguments.of("woord", "cysta", new Feedback("cysta",  List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)))
        );
    }
}