package nl.hu.cisq1.lingo.trainer.presentation;


import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidWordException;
import nl.hu.cisq1.lingo.trainer.presentation.DTO.AttemptInputDTO;
import nl.hu.cisq1.lingo.trainer.presentation.DTO.ProgressOutputDTO;
import nl.hu.cisq1.lingo.trainer.presentation.DTO.RoundInputDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("trainer")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService){
        this.trainerService = trainerService;
    }

    @PostMapping("newGame")
    public ResponseEntity startNewGame() {
        try {
            Progress progress =  this.trainerService.startNewGame();
            ProgressOutputDTO output = new ProgressOutputDTO.Builder(progress.getGameID())
                    .score(progress.getScore())
                    .gameStatus(progress.getGameStatus())
                    .hint(progress.getHint())
                    .feedback(progress.getFeedbackList())
                    .build();
            return new ResponseEntity<>(output, HttpStatus.OK);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    @PutMapping("attempt")
    public ResponseEntity makeGuess(@RequestBody AttemptInputDTO input){
        try{
            Progress progress = this.trainerService.guess(input.attempt, input.gameID);
            ProgressOutputDTO output = new ProgressOutputDTO.Builder(progress.getGameID())
                    .score(progress.getScore())
                    .gameStatus(progress.getGameStatus())
                    .hint(progress.getHint())
                    .feedback(progress.getFeedbackList())
                    .build();
            return new ResponseEntity<>(output, HttpStatus.OK);
        }catch (InvalidGuessException | NotFoundException | InvalidWordException exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }
    @PutMapping("continue")
    public ResponseEntity newRound(@RequestBody RoundInputDTO input){
        try{
            Progress progress = this.trainerService.startNewRound(input.gameID);
            ProgressOutputDTO output = new ProgressOutputDTO.Builder(progress.getGameID())
                    .score(progress.getScore())
                    .gameStatus(progress.getGameStatus())
                    .hint(progress.getHint())
                    .feedback(progress.getFeedbackList())
                    .build();
            return new ResponseEntity<>(output, HttpStatus.OK);
        }catch (InvalidRoundException | NotFoundException exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }
    @GetMapping("getGame")
    public ResponseEntity getGameById(@RequestBody RoundInputDTO input){
        try{
            Progress progress = this.trainerService.getGameProgress(input.gameID);
            ProgressOutputDTO output = new ProgressOutputDTO.Builder(progress.getGameID())
                    .score(progress.getScore())
                    .gameStatus(progress.getGameStatus())
                    .hint(progress.getHint())
                    .feedback(progress.getFeedbackList())
                    .build();
            return new ResponseEntity<>(output, HttpStatus.OK);
        }catch (NotFoundException exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }
    @GetMapping("getAll")
    public ResponseEntity getAllGames(){
        List<ProgressOutputDTO> progressOutputDTOS = new ArrayList<>();
        List<Progress> progress = this.trainerService.getAllGames();
        for (Progress progres: progress) {
            progressOutputDTOS.add( new ProgressOutputDTO.Builder(progres.getGameID())
                    .score(progres.getScore())
                    .gameStatus(progres.getGameStatus())
                    .hint(progres.getHint())
                    .feedback(progres.getFeedbackList())
                    .build());
        }
        return new ResponseEntity<>(progressOutputDTOS, HttpStatus.OK);
    }

}