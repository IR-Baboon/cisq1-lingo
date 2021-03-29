package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private int score;
    private GameStatus gameStatus;
    private List<Round> rounds;

    private Game(){
        this.score = 0;
        gameStatus = GameStatus.WAITING;
        rounds = new ArrayList<>();
    }

    public Round getCurrentRound(){
        return rounds.get(rounds.size()-1);
    }

    public void startNewRound(String wordToGuess){
        Round newRound = new Round(wordToGuess);
        rounds.add(newRound);
        gameStatus = GameStatus.PLAYING;
    }

    public void guess(String attempt){
        try{
            getCurrentRound().guess(attempt);
        }catch (InvalidGuessException error){
            gameStatus = GameStatus.DEFEAT;
        }

    }
    public void addScore(Round round){
        score += (5 * (5 - round.getAttempts()) + 5);
    }
    public Progress showProgress(){
        Round round = getCurrentRound();
        Progress progress = new Progress(score, round.getFeedbackHistory() , round.giveHint());
        return progress;
    }

    public boolean isPlayerDefeated(){
        return gameStatus == GameStatus.DEFEAT;
    }
    public boolean isPlaying(){
        return gameStatus != GameStatus.PLAYING;
    }
    public int provideNextWordLength(){
        if(rounds.size() == 0){
            return 5;
        }
        Round round = getCurrentRound();
         switch (round.getCurrentWordLength()){
            case 5:
                return 6;
            case 6:
                return 7;
             default:
                return 5;
        }
    }
}
