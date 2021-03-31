package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;

public class Progress {
    private int score;
    private List<Feedback> feedbackList;
    private String hint;

    public Progress(int score, List<Feedback> feedbacks, String hint){
        this.score = score;
        this.feedbackList = feedbacks;
        this.hint = hint;
    }

    public int getScore() {
        return score;
    }

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public String getHint() {
        return hint;
    }
}
