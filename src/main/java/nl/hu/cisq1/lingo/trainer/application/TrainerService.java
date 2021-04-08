package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidWordException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Transactional
@Service
public class TrainerService {
    private final SpringGameRepository gameRepository;
    private final WordService wordService;

    public TrainerService(SpringGameRepository gameRepository, WordService wordService){
        this.gameRepository = gameRepository;
        this.wordService = wordService;
    }

    public Progress startNewGame(){
        String word = wordService.provideRandomWord(5);
        Game game = new Game();
        game.startNewRound(word);
        gameRepository.save(game);
        return game.showProgress();
    }

    public Progress guess(String attempt, long id) throws NotFoundException {
        if(wordService.provideWordCheck(attempt) != null){
            Game game = gameRepository.findById(id).orElseThrow(() -> new NotFoundException("game not found"));
            game.guess(attempt);
            gameRepository.save(game);
            return game.showProgress();
        }else{
            throw InvalidWordException.wordDoesNotExist();
        }
}

    public Progress startNewRound(long id) throws NotFoundException {
        Game game = gameRepository.findById(id).orElseThrow(() -> new NotFoundException("game not found"));
        String newWord = wordService.provideRandomWord(game.provideNextWordLength());
        game.startNewRound(newWord);
        gameRepository.save(game);
        return game.showProgress();
    }

    public Progress getGameProgress(long id) throws NotFoundException {
        Game game = gameRepository.findById(id).orElseThrow(() -> new NotFoundException("game not found"));
        return game.showProgress();
    }

    public List<Progress> getAllGames() {
        List<Game> games = gameRepository.findAll();
        List<Progress> progressList = new ArrayList<>();
        for(Game game : games){
            progressList.add(game.showProgress());
        }
        return progressList;
    }
}
