package nl.hu.cisq1.lingo.trainer.presentation.DTO;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;

import java.util.List;

public class ProgressOutputDto {
    public long gameID;
    public long score;
    public GameStatus gameStatus;
    public String hint;
    public List<Feedback> feedbackList;

    private ProgressOutputDto() {
    }

    public static class Builder{
        private long gameID;
        private long score;
        private GameStatus gameStatus;
        private String hint;
        private List<Feedback> feedbacklist;

        public Builder(long gameID){
            this.gameID = gameID;
        }

        public ProgressOutputDto.Builder score(long score){
            this.score = score;
            return this;
        }

        public ProgressOutputDto.Builder gameStatus(GameStatus gameStatus){
            this.gameStatus = gameStatus;
            return this;
        }

        public ProgressOutputDto.Builder hint(String hint){
            this.hint = hint;
            return this;
        }

        public ProgressOutputDto.Builder feedback(List<Feedback> feedbacks){
            this.feedbacklist = feedbacks;
            return this;
        }

        public ProgressOutputDto build(){
            ProgressOutputDto progressOutputDTO = new ProgressOutputDto();
            progressOutputDTO.gameID = this.gameID;
            progressOutputDTO.score = this.score;
            progressOutputDTO.gameStatus = this.gameStatus;
            progressOutputDTO.hint = this.hint;
            progressOutputDTO.feedbackList = this.feedbacklist;
            return progressOutputDTO;
        }
    }
}
