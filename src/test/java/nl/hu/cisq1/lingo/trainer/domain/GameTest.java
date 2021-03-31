package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidRoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class GameTest {
    private Game game;

    @BeforeEach
    void createNewGame(){
        game = new Game();
    }

    @Nested
    @DisplayName("test starting")
    class TestStartingPoint {

        @Test
        void testStatusNotPlaying(){
            assertFalse(game.isPlaying());
        }
        @Test
        void testStatusNotDefeated(){
            assertFalse(game.isPlayerDefeated());
        }

        @Test
        void testScore(){
            assertEquals(0, game.getScore());
        }

        @Test
        void testCurrentRound(){
            assertNull(game.getCurrentRound());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class StartNewRound {

        @BeforeEach
        void createNewRound(){
            game.startNewRound("woord");
        }

        @Test
        void assertingThrows(){
            assertThrows(InvalidRoundException.class, ()-> game.startNewRound("tralal"));
        }

        @Test
        void testStatusPlaying(){
            assertTrue(game.isPlaying());
        }
        @Test
        void testStatusNotDefeated(){
            assertFalse(game.isPlayerDefeated());
        }

        @Test
        void roundNumber(){
            assertEquals(1, game.getCurrentRound().getRoundNumber());
        }
        @Test
        void testScoreBefore(){
            assertEquals(0, game.getScore());
        }
        @Test
        void roundNumberTwo(){
            game.guess("woord");
            game.startNewRound("woord");
            assertEquals(2, game.getCurrentRound().getRoundNumber());
        }

        @ParameterizedTest
        @MethodSource("provideScoreExamples")
        void testScoreAfterGuesses(List<String> guesses, int expectedScore){
            for(String guess : guesses){
                game.guess(guess);
            }
            assertEquals(expectedScore, game.getScore());
        }

        @Test
        void testStatusDefeated(){
            game.guess("tests");
            game.guess("tests");
            game.guess("tests");
            game.guess("tests");
            game.guess("tests");
            assertTrue(game.isPlayerDefeated());
        }

        private Stream<Arguments> provideScoreExamples(){
            return Stream.of(
                    Arguments.of(List.of("woord"), 25),
                    Arguments.of(List.of("sloot","woord"), 20),
                    Arguments.of(List.of("sloot","sloot","woord"), 15),
                    Arguments.of(List.of("sloot","sloot","sloot","woord"), 10),
                    Arguments.of(List.of("sloot","sloot","sloot","sloot","woord"), 5),
                    Arguments.of(List.of("sloot","sloot","sloot","sloot","sloot"), 0)
            );
        }
    }
}