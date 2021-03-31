package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidWordException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private String id;
    private int score;
    private GameStatus gameStatus;
    private List<Round> rounds;

    public Game(){
        this.id = UUID.randomUUID().toString();
        this.score = 0;
        gameStatus = GameStatus.WAITING;
        rounds = new ArrayList<>();
    }

    public Round getCurrentRound(){
        if(rounds.size() > 0){
            return rounds.get(rounds.size() -1);
        }
        return null;
    }

    public void startNewRound(String wordToGuess){
        if(!isPlaying() && !isPlayerDefeated()){
            if (provideNextWordLength() != wordToGuess.length()) {
                throw InvalidWordException.invalidLength();
            }else{
                Round newRound = new Round(wordToGuess, rounds.size() + 1);
                rounds.add(newRound);
                gameStatus = GameStatus.PLAYING;
            }
        }else{
            throw InvalidRoundException.roundActive();
        }
    }

    public int getScore() {
        return score;
    }

    public void guess(String attempt){
            if(isPlaying()){
                Feedback feedback = getCurrentRound().guess(attempt);
                if(feedback.isWordGuessed()){
                    gameStatus = GameStatus.WAITING;
                    score += (5 * (5 - getCurrentRound().getAttempts()) + 5);
                }else if(getCurrentRound().getAttempts() == 5 && !feedback.isWordGuessed()){
                    gameStatus = GameStatus.DEFEAT;
                }
            }else if(isPlayerDefeated()){
                throw InvalidGuessException.playerDefeated();
            }
    }

    public Progress showProgress(){
        Round round = getCurrentRound();
        return new Progress(getScore(), round.getFeedbackHistory() , round.giveHint());
    }

    public boolean isPlayerDefeated(){
        return gameStatus == GameStatus.DEFEAT;
    }

    public boolean isPlaying(){
        return gameStatus == GameStatus.PLAYING;
    }

    public int provideNextWordLength(){
        Round round = getCurrentRound();
        if(round != null){
            switch (round.getCurrentWordLength()){
                case 5:
                    return 6;
                case 6:
                    return 7;
                default:
                    return 5;
            }
        }else{
            return 5;
        }

    }
}
