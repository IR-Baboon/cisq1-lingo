package nl.hu.cisq1.lingo.words.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.exception.WordLengthNotSupportedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class WordService {
    private final SpringWordRepository wordRepository;

    public WordService(SpringWordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public String provideRandomWord(Integer length) {
        return this.wordRepository
                .findRandomWordByLength(length)
                .orElseThrow(() -> new WordLengthNotSupportedException(length))
                .getValue();
    }
    public String provideWordCheck(String attempt) throws NotFoundException {
        return this.wordRepository
                .findByValue(attempt)
                .orElseThrow(() -> new NotFoundException("woord niet gevonden"))
                .getValue();
    }
}
