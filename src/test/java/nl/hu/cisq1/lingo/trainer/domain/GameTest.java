package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidWordException;
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
    @DisplayName("before all and each test, create a game")
    void createNewGame(){
        game = new Game();
    }

    @Nested
    @DisplayName("test starting values when creating a game")
    class GameInitTestClass {

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
    @DisplayName("Test creating a new round and completing it")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GameRoundTestClass {

        @BeforeEach
        @DisplayName("before each test in this nested class, start a new round")
        void createNewRound(){
            game.startNewRound("woord");
        }

        @Test
        void assertingThrows(){
            assertThrows(InvalidRoundException.class, ()-> game.startNewRound("tralal"));
        }

        @Test
        void testStatusIsPlaying(){
            assertTrue(game.isPlaying());
        }
        @Test
        void testStatusIsNotPlaying(){
            game.guess("woord");
            assertFalse(game.isPlaying());
        }

        @Test
        void testStatusNotDefeated(){
            assertFalse(game.isPlayerDefeated());
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

        @Test
        void roundNumber(){
            assertEquals(1, game.getCurrentRound().getRoundNumber());
        }

        @Test
        void testScoreBefore(){
            assertEquals(0, game.getScore());
        }

        @Test
        void roundNumberTwoThrows(){
            game.guess("woord");

            assertThrows(InvalidWordException.invalidLength().getClass(),
            () ->  game.startNewRound("woord"));
        }
        @Test
        void roundNumberTwo(){
            game.guess("woord");
            game.startNewRound("woorde");
            assertEquals(2, game.getCurrentRound().getRoundNumber());
        }

        @ParameterizedTest
        @MethodSource("provideScoreExamples")
        void testScore(List<String> guesses, int expectedScore){
            for(String guess : guesses){
                game.guess(guess);
            }
            assertEquals(expectedScore, game.getScore());
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

    @Nested
    @DisplayName("Test progress return")
    class GameProgressTestClass {
        @BeforeEach
        @DisplayName("before each nested test class in this nested class, start a new round so that the progress can be checked.")
        void createNewRound(){
            game.startNewRound("brood");
        }

        @Nested
        @DisplayName("Test Progress in the beginning")
        class GameProgressTestBeginning {
            @Test
            @DisplayName("test score in Progress after starting new Round")
            void testScoreProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals(0, progress.getScore());
            }
            @Test
            @DisplayName("test hint in Progress after starting new Round")
            void testHintProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals("b....", progress.getHint());
            }
            @Test
            @DisplayName("test ammounts of feedback in Progress after starting new Round")
            void testFeedbackListProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals(0, progress.getFeedbackList().size());
            }
        }

        @Nested
        @DisplayName("Test Progress after a guess")
        class GameProgressTestAfterGuess {
            @BeforeEach
            @DisplayName("before each test in this nested class, make a guess")
            void makeOneGuess(){
                game.guess("braam");
            }

            @Test
            @DisplayName("test score in Progress after a guess")
            void testScoreProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals(0, progress.getScore());
            }
            @Test
            @DisplayName("test score in Progress after a guess")
            void testHintProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals("br...", progress.getHint());
            }
            @Test
            @DisplayName("test score in Progress after a guess")
            void testFeedbackListProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals(1, progress.getFeedbackList().size());
            }
        }

        @Nested
        @DisplayName("Test Progress after winning a Round")
        class GameProgressTestAfterWin {
            @BeforeEach
            @DisplayName("before each test in this nested class, Win the game")
            void winGame(){
                game.guess("brood");
            }
            @Test
            @DisplayName("test score in Progress after winning a Round")
            void testScoreProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals(25, progress.getScore());
            }
            @Test
            @DisplayName("test score in Progress after winning a Round")
            void testHintProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals("brood", progress.getHint());
            }
            @Test
            @DisplayName("test score in Progress after winning a Round")
            void testFeedbackListProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals(1, progress.getFeedbackList().size());
            }
        }

        @Nested
        @DisplayName("Test Progress in the beginning")
        class GameProgressTestAfterLoss {
            @BeforeEach
            @DisplayName("before each test in this nested class, lose the game")
            void loseGame(){
                game.guess("welke");
                game.guess("waard");
                game.guess("worde");
                game.guess("brons");
                game.guess("plons");
            }

            @Test
            @DisplayName("test score in Progress after losing a Round")
            void testScoreProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals(0, progress.getScore());
            }
            @Test
            @DisplayName("test score in Progress after losing a Round")
            void testHintProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals("bro.d", progress.getHint());
            }
            @Test
            @DisplayName("test score in Progress after losing a Round")
            void testFeedbackListProgressBeginning(){
                Progress progress = game.showProgress();
                assertEquals(5, progress.getFeedbackList().size());
            }
        }
    }
}