package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidFeedBackException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private String wordToGuess;
    private String hint;
    private List<Feedback> attempts;

    public Round(String wordToGuess) {
        this.wordToGuess = wordToGuess;
        this.hint = this.wordToGuess.charAt(0) + ".".repeat(wordToGuess.length() -1);
        attempts = new ArrayList<>();
    }

    public void guess(String attempt) throws InvalidGuessException {
        if(attempts.size() > 4 || hint.equals(wordToGuess)){
            throw InvalidGuessException.noMoreGuesses();
        }
        List<Mark> marks = new ArrayList<>();
        for(int index = 0; index < attempt.length(); index++){

            if(attempt.length() != wordToGuess.length()) {
                marks.add(Mark.INVALID);
            }
            else  if(attempt.charAt(index) == wordToGuess.charAt(index)){
                marks.add(Mark.CORRECT);
            }
            else if(attempt.charAt(index) != wordToGuess.charAt(index) && wordToGuess.contains(attempt.substring(index, index +1))){
                // This 2 following lines i used a built in method from the Spring Framwork.
                // It counts the occurences of a character in a String and return it as an integer.
                // In this line the method checks how many times this letter has already passed in the attempt.
                // it slices the attempt to the point of index and then counts the chars in both words.
                int amountAttemptLetter = StringUtils.countOccurrencesOf(attempt.substring(0, index +1), attempt.substring(index, index +1));
                // in this line we check the total amount of the letter in the wordToGuess
                int amountRoundWordLetter = StringUtils.countOccurrencesOf(wordToGuess, attempt.substring(index, index +1));
                // if the amount in attempt(so far including this step) is lower than in wordToGuess
                if(amountAttemptLetter <= amountRoundWordLetter){
                    marks.add(Mark.PRESENT);
                // if it is more, make an Absent mark and add it to the list
                }else{
                    marks.add(Mark.ABSENT);
                }
            // if letters dont match and wordToGuess does not contain the letter
            }else{
                marks.add(Mark.ABSENT);
            }
        }
        // create feedback and put the attempt and list of marks in it
        Feedback feedback = new Feedback(attempt, marks);
        // add feedback to list
        attempts.add(feedback);
        this.giveHint();
    }

    public String giveHint(){
        // check if the list size is more than 0
        if(attempts.size() > 0){
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

}
