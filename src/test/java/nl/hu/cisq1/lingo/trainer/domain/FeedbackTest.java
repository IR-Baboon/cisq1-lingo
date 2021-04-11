package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidFeedBackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed() throws InvalidFeedBackException {
        Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(feedback.isWordGuessed());
    }
    @Test
    @DisplayName("word is not guessed if all letters are not correct")
    void wordIsNotGuessed() throws InvalidFeedBackException {
        Feedback feedback = new Feedback("aoord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("guess is not valid when its length is not the same as the word to guess")
    void guessIsInvalid() throws InvalidFeedBackException {
        Feedback feedback = new Feedback("woorden", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        assertTrue(feedback.guessIsInvalid());

    }

    @Test
    @DisplayName("guess is valid when its length is the same as the word to guess")
    void guessIsNotInvalid() throws InvalidFeedBackException {
        Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertFalse(feedback.guessIsInvalid());
    }

    @Test
    @DisplayName("Constructor throws InvalidFeedbackException exception when word and marksize do not correspond")
    void constructorThrowsExcpetion() throws InvalidFeedBackException {
        assertThrows( InvalidFeedBackException.wrongLength().getClass(),
                () -> new Feedback("woord", List.of(Mark.CORRECT)));
    }

    @Test
    @DisplayName("Two feedbacks equal eachother")
    void feedbackEqual() throws InvalidFeedBackException {
        Feedback feedback1 = new Feedback("woord", List.of(Mark.CORRECT, Mark.ABSENT,Mark.CORRECT, Mark.ABSENT,Mark.CORRECT));
        Feedback feedback2 = new Feedback("woord", List.of(Mark.CORRECT, Mark.ABSENT,Mark.CORRECT, Mark.ABSENT,Mark.CORRECT));
        assertEquals(feedback1, feedback2);

    }

    @Test
    @DisplayName("Two feedbacks do not equal eachother")
    void feedbacksNotEqual() throws InvalidFeedBackException {
        Feedback feedback1 = new Feedback("woor1", List.of(Mark.CORRECT, Mark.ABSENT,Mark.CORRECT, Mark.ABSENT,Mark.CORRECT));
        Feedback feedback2 = new Feedback("woord", List.of(Mark.CORRECT, Mark.ABSENT,Mark.CORRECT, Mark.ABSENT,Mark.CORRECT));
        assertNotEquals(feedback1, feedback2);
    }

    @Test
    @DisplayName("Two feedbacks do not equal eachother")
    void feedbacksgiveFalseEquals() throws InvalidFeedBackException {
        Round round = new Round();
         Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.ABSENT,Mark.CORRECT, Mark.ABSENT,Mark.CORRECT));
        assertNotEquals(round, feedback);
    }

    @Test
    @DisplayName("giveHint() throws InvalidFeedbackException exception when attempt length and hint length do not correspond")
    void giveHintThrowsExcpetion() throws InvalidFeedBackException {
        assertThrows( InvalidFeedBackException.class, () -> new Feedback("woorse", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)).giveHint("wxxxx"));
    }
    @Test
    @DisplayName("giveHint() throws InvalidFeedbackException exception when marks do not correspond with previousHint")
    void giveHintThrowsExcpetionTestTwo() throws InvalidFeedBackException {
        assertThrows( InvalidFeedBackException.invalidMarks().getClass(), () -> new Feedback("woord", List.of(Mark.CORRECT)).giveHint("w...."));
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("feedback provides a hint based on the letters guess right")
    void giveHint( String previousHint,  Feedback feedback, String expected) throws InvalidFeedBackException {
        String hint = feedback.giveHint(previousHint);
        assertEquals(expected, hint);
    }

    static Stream<Arguments> provideHintExamples() throws InvalidFeedBackException {
        return Stream.of(
                Arguments.of("b....", new Feedback("bloem", List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)), "b...."),
                Arguments.of("b....", new Feedback("bloed", List.of(Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT)), "b...d"),
                Arguments.of("b....", new Feedback("baard", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)), "baard")
        );
    }
}