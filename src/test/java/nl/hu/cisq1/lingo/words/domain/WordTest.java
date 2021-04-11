package nl.hu.cisq1.lingo.words.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WordTest {

    @Test
    @DisplayName("length is based on given word")
    void lengthBasedOnWord() {
        Word word = new Word("woord");
        int length = word.getLength();
        assertEquals(5, length);
    }

    @Test
    @DisplayName("length is based on given word")
    void valueBasedOnWord() {
        Word word = new Word("woord");
        String checkWord = word.getValue();
        assertEquals("woord", checkWord);
    }
    @Test
    @DisplayName("length is based on given word")
    void EmptyWord() {
        Word word = new Word();

        assertNull(word.getValue());
        assertNull(word.getLength());
    }
}
