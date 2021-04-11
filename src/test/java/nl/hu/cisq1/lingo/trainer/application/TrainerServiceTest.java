package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.*;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidWordException;
import nl.hu.cisq1.lingo.words.application.WordService;

import org.junit.jupiter.api.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {


    @Test
    @DisplayName("When starting new game service provides Progress")
    void providesProgress() {
        // define mockery
        WordService wordService = mock(WordService.class);
        SpringGameRepository mockRepository = mock(SpringGameRepository.class);
        TrainerService service = new TrainerService(mockRepository, wordService);
        // define when
        when(wordService.provideRandomWord(5)).thenReturn("appel");

        List<Feedback> feedbacklist = new ArrayList<>();
        Progress progress = new Progress(0, feedbacklist, "a....", 0, GameStatus.PLAYING );
        assertEquals(progress, service.startNewGame());
    }

    @Test
    @DisplayName("When having a game, be able to get the right progress")
    void providesProgressOnRequest() throws NotFoundException {
        // define mockery
        WordService wordService = mock(WordService.class);
        SpringGameRepository mockRepository = mock(SpringGameRepository.class);
        TrainerService service = new TrainerService(mockRepository, wordService);
        // define when
        when(wordService.provideRandomWord(5)).thenReturn("appel");
        when(wordService.provideWordCheck("appel")).thenReturn("appel");
        //create game
        Game game = new Game();
        // start new round with mocked word
        game.startNewRound(wordService.provideRandomWord(game.provideNextWordLength()));
        // mock game return
        when(mockRepository.findById(0)).thenReturn(Optional.of(game));

        List<Feedback> feedbacklist = new ArrayList<>();
        Progress progress = new Progress(0, feedbacklist, "a....", 0, GameStatus.PLAYING );
        assertEquals(progress, service.getGameProgress(0));
    }

    @Test
    @DisplayName("When having a game, be able to make a guess and get a Progress in return")
    void providesProgressOnGuess() throws NotFoundException {
        // define mockery
        WordService wordService = mock(WordService.class);
        SpringGameRepository mockRepository = mock(SpringGameRepository.class);
        TrainerService service = new TrainerService(mockRepository, wordService);
        // define when
        when(wordService.provideRandomWord(5)).thenReturn("appel");
        when(wordService.provideWordCheck("appel")).thenReturn("appel");
        //create game
        Game game = new Game();
        // start new round with mocked word
        game.startNewRound(wordService.provideRandomWord(game.provideNextWordLength()));
        game.guess("apeel");
        // mock game return
        when(mockRepository.findById(0)).thenReturn(Optional.of(game));

        List<Feedback> feedbacklist = new ArrayList<>();
        Feedback feedback = new Feedback("apeel", List.of(Mark.CORRECT, Mark.CORRECT,Mark.ABSENT,Mark.CORRECT,Mark.CORRECT));
        feedbacklist.add(feedback);
        Progress progress = new Progress(0, feedbacklist, "ap.el", 0, GameStatus.PLAYING );
        assertEquals(progress, service.getGameProgress(0));
    }
    @Test
    @DisplayName("When a game is won, be able to start a new round and get a Progress in return")
    void providesProgressWhenStartingNewRound() throws NotFoundException {
        // define mockery
        WordService wordService = mock(WordService.class);
        SpringGameRepository mockRepository = mock(SpringGameRepository.class);
        TrainerService service = new TrainerService(mockRepository, wordService);
        // define when
        when(wordService.provideRandomWord(5)).thenReturn("appel");
        when(wordService.provideWordCheck("appel")).thenReturn("appel");
        when(wordService.provideRandomWord(6)).thenReturn("appels");
        //create game
        Game game = new Game();
        // start new round with mocked word
        game.startNewRound(wordService.provideRandomWord(game.provideNextWordLength()));
        game.guess("appel");
        // mock game return
        when(mockRepository.findById(0)).thenReturn(Optional.of(game));

        List<Feedback> feedbacklist = new ArrayList<>();
        Progress progress = new Progress(25, feedbacklist, "a.....", 0, GameStatus.PLAYING );
        assertEquals(progress, service.startNewRound(0));
    }
    @Test
    @DisplayName("When having a game, be not able to start a new round")
    void throwErrorWhenStartingRound() throws NotFoundException {
        // define mockery
        WordService wordService = mock(WordService.class);
        SpringGameRepository mockRepository = mock(SpringGameRepository.class);
        TrainerService service = new TrainerService(mockRepository, wordService);
        // define when
        when(wordService.provideRandomWord(5)).thenReturn("appel");
        when(wordService.provideWordCheck("appel")).thenReturn("appel");
        //create game
        Game game = new Game();
        // start new round with mocked word
        game.startNewRound(wordService.provideRandomWord(game.provideNextWordLength()));
        game.guess("apeel");
        // mock game return
        when(mockRepository.findById(0)).thenReturn(Optional.of(game));

        List<Feedback> feedbacklist = new ArrayList<>();
        Feedback feedback = new Feedback("apeel", List.of(Mark.CORRECT, Mark.CORRECT,Mark.ABSENT,Mark.CORRECT,Mark.CORRECT));
        feedbacklist.add(feedback);
        Progress progress = new Progress(0, feedbacklist, "ap.el", 0, GameStatus.PLAYING );
        assertThrows(InvalidRoundException.roundActive().getClass(), () -> service.startNewRound(0));
    }

    @Test
    @DisplayName("Provides a list of progresses when requesting all games")
    void providesAllProgresses() {
        // define mockery
        WordService wordService = mock(WordService.class);
        SpringGameRepository mockRepository = mock(SpringGameRepository.class);
        TrainerService service = new TrainerService(mockRepository, wordService);
        // define when
        when(wordService.provideRandomWord(5)).thenReturn("appel");
        when(wordService.provideRandomWord(6)).thenReturn("appels");
        Game game1 = new Game();
        Game game2 = new Game();
        Game game3 = new Game();
        when(mockRepository.findAll()).thenReturn(List.of(game1, game2, game3));
        // assert
        System.out.println(service.getAllGames());
        assertTrue(service.getAllGames().size() > 1);
    }

    @Test
    @DisplayName("throw error when asking progress from non existing game")
    void cannotProviceProgress() {
        // define mockery
        WordService wordService = mock(WordService.class);
        SpringGameRepository mockRepository = mock(SpringGameRepository.class);
        TrainerService service = new TrainerService(mockRepository, wordService);
        // define when
        when(wordService.provideRandomWord(5)).thenReturn("appel");
        Game game = new Game();
        // start new round with mocked word
        game.startNewRound(wordService.provideRandomWord(game.provideNextWordLength()));
        game.guess("apeel");
        // mock game return
        when(mockRepository.findById(0)).thenReturn(Optional.of(game));

        assertThrows(NotFoundException.class, ()-> service.getGameProgress(1000000));
    }

    @Test
    @DisplayName("throw word does not exist error when guessed word does not exists")
    void throwErrorOnInvalidWord() {
        // define mockery
        WordService wordService = mock(WordService.class);
        SpringGameRepository mockRepository = mock(SpringGameRepository.class);
        TrainerService service = new TrainerService(mockRepository, wordService);
        // define when
        when(wordService.provideRandomWord(5)).thenReturn("appel");
        Game game = new Game();
        // start new round with mocked word
        game.startNewRound(wordService.provideRandomWord(game.provideNextWordLength()));
        game.guess("apeel");
        // mock game return
        when(mockRepository.findById(0)).thenReturn(Optional.of(game));

        assertThrows(InvalidWordException.wordDoesNotExist().getClass(), ()-> service.guess("aarfd", game.getId()));
    }
}