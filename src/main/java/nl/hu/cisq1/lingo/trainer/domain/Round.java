package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="round")
public class Round {

    @Id
    @GeneratedValue
    private int id;

    private int roundNumber;

    private String wordToGuess;

    private String hint;

    @OneToMany
    @JoinColumn
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Feedback> attempts;

    public Round(){}

    public Round(String wordToGuess, int roundNumber) {
        this.roundNumber = roundNumber;
        this.wordToGuess = wordToGuess;
        this.hint = this.wordToGuess.charAt(0) + ".".repeat(wordToGuess.length() -1);
        attempts = new ArrayList<>();
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Feedback guess(String attempt) throws InvalidGuessException {
        if(attempts.size() > 4 ){
            throw InvalidGuessException.noMoreGuesses();
        }else if(getFeedbackHistory().stream().anyMatch(Feedback::isWordGuessed)){
            throw InvalidGuessException.wordIsGuessed();
        }
        List<Mark> marks = new ArrayList<>();
        String word = wordToGuess;
        for(int index = 0; index < attempt.length(); index++){
            if(word.length() != attempt.length()){
                marks.add(Mark.INVALID);
            } else if(isLetterCorrect(word, attempt, index)){
                marks.add(Mark.CORRECT);
            }else if(word.contains(attempt.substring(index, index+1))){
                if(isCharacterPresent(word, attempt, index)){
                    marks.add(Mark.PRESENT);
                }else{
                    marks.add(Mark.ABSENT);
                }
            }else{
                marks.add(Mark.ABSENT);
            }
        }

        // create feedback and put the attempt and list of marks in it
        Feedback feedback = new Feedback(attempt, marks);
        // add feedback to list
        attempts.add(feedback);
        this.giveHint();
        return feedback;
    }

    public String getHint() {
        return hint;
    }

    public boolean isCharacterPresent(String word, String attempt, int index){
        // turn all into chars and charArrays and pick out the character of importance
        char[] wordArray = word.toCharArray();
        char[] attemptArray = attempt.toCharArray();
        char character = attemptArray[index];


        // create lists for indexes wich are the same letters and a list for letters wich will not be correct
        List<Integer> indexWordNumbers = new ArrayList<>();
        List<Integer> indexAttemptNumbers = new ArrayList<>();
        List<Integer> notCorrectList = new ArrayList<>();
        // create a loop and check both words if indexLetter matches character of importance
        // if so, add the index of the matching character to the corresponding lists
        for(int idx = 0; idx < attempt.length(); idx++){
            if(wordArray[idx] == character){
                indexWordNumbers.add(idx);
            }
            if(attemptArray[idx] == character){
                indexAttemptNumbers.add(idx);
            }
        }

        // loop through the index lists and check if indexes are the same.
        // If not, add index of the attempt list to the notCorrectList.
        for(Integer idxX: indexAttemptNumbers){
            for(Integer idxY : indexWordNumbers){
                if(idxX.equals(idxY)){
                    break;
                }else if(!notCorrectList.contains(idxX)){
                    notCorrectList.add(idxX);
                }
            }
        }

        // filter the correct numbers out of the wordToGuess indexes
        // this has to be done apart form the for loop before because of a ConcurrentModificationException
        for (Integer number: indexAttemptNumbers) {
            indexWordNumbers.remove(number);
        }
        // if the not correct list size is smaller or the same size
        // When the character is present the list should correspond or the notCorrectList should be smaller
        // return outcome
        return notCorrectList.size() <= indexWordNumbers.size();
    }

    public boolean isLetterCorrect(String wordToGuess, String attempt, int index){
        return wordToGuess.charAt(index) == attempt.charAt(index);
    }

    public String giveHint(){
        // check if the list size is more than 0
        if(!attempts.isEmpty()){
            // get the last item in attempts
            hint = attempts.get(attempts.size()-1).giveHint(hint);
        }
        return hint;
    }

    public List<Feedback> getFeedbackHistory(){
        return attempts;
    }

    public int getAttempts(){
        return attempts.size();
    }

    public int getCurrentWordLength(){
        return wordToGuess.length();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Round)) return false;
        Round round = (Round) o;
        return id == round.id &&
                roundNumber == round.roundNumber &&
                Objects.equals(wordToGuess, round.wordToGuess) &&
                Objects.equals(hint, round.hint) &&
                Objects.equals(attempts, round.attempts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roundNumber, wordToGuess, hint, attempts);
    }

//    @Override
//    public String toString() {
//        return "Round{" +
//                "id=" + id +
//                ", roundNumber=" + roundNumber +
//                ", wordToGuess='" + wordToGuess + '\'' +
//                ", hint='" + hint + '\'' +
//                ", attempts=" + attempts +
//                '}';
//    }

}
