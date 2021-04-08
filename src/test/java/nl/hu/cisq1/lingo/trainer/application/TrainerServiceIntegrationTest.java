package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidRoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
class TrainerServiceIntegrationTest {
    @Autowired
    private TrainerService trainerService;

    @Test
    @DisplayName("start a new game and check progress")
    void startNewGame(){
        Progress progress = trainerService.startNewGame();

        assertEquals(0, progress.getScore());
        assertEquals(5, progress.getHint().length());
        assertEquals(GameStatus.PLAYING, progress.getGameStatus());
        assertEquals(0, progress.getFeedbackList().size());
    }

    @Test
    @DisplayName("")
    void cannotStartNewRound(){
        Progress progress = trainerService.startNewGame();
        assertThrows(InvalidRoundException.roundActive().getClass(), ()-> trainerService.startNewRound(progress.getGameID()));
    }


}