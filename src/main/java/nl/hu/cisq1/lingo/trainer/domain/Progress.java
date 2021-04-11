package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;
import java.util.Objects;

public class Progress {
    private long gameID;
    private long score;
    private List<Feedback> feedbackList;
    private String hint;
    private GameStatus gameStatus;

    public Progress(long score, List<Feedback> feedbacks, String hint, long gameID, GameStatus gameStatus){
        this.score = score;
        this.feedbackList = feedbacks;
        this.hint = hint;
        this.gameID = gameID;
        this.gameStatus = gameStatus;
    }

    public long getScore() {
        return score;
    }

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public String getHint() {
        return hint;
    }

    public long getGameID() {
        return gameID;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Progress)) return false;
        Progress progress = (Progress) o;
        return score == progress.score &&
                Objects.equals(feedbackList, progress.feedbackList) &&
                Objects.equals(hint, progress.hint) &&
                gameStatus == progress.gameStatus;
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(score, feedbackList, hint, gameStatus);
//    }

    @Override
    public String toString() {
        return "Progress{" +
                "gameID=" + gameID +
                ", score=" + score +
                ", feedbackList=" + feedbackList +
                ", hint='" + hint + '\'' +
                ", gameStatus=" + gameStatus +
                '}';
    }
}
