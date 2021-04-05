package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidWordException;
import org.hibernate.annotations.Cascade;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue
    private long id;

    private int score;

    @Column(name="game_status")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @OneToMany
    @JoinColumn
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Round> rounds;

    public long getId() {
        return id;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public Game(){
        this.score = 0;
        gameStatus = GameStatus.WAITING;
        rounds = new ArrayList<>();
    }

    public Round getCurrentRound(){
        if(!rounds.isEmpty()){
            return rounds.get(rounds.size() -1);
        }
        return null;
    }

    public boolean startNewRound(String wordToGuess){
        if(!isPlaying() && !isPlayerDefeated()){
            if (provideNextWordLength() != wordToGuess.length()) {
                throw InvalidWordException.invalidLength();
            }else{
                Round newRound = new Round(wordToGuess, rounds.size() + 1);
                rounds.add(newRound);
                gameStatus = GameStatus.PLAYING;
                return true;
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
        return new Progress(getScore(), round.getFeedbackHistory() , round.giveHint(), getId(), this.gameStatus);
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

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", score=" + score +
                ", gameStatus=" + gameStatus +
                ", rounds=" + rounds +
                '}';
    }
}
