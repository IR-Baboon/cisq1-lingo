package nl.hu.cisq1.lingo.trainer.application;

import javassist.NotFoundException;
import nl.hu.cisq1.lingo.trainer.application.DTO.GamePresentationDTO;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import nl.hu.cisq1.lingo.words.application.WordService;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Transactional
@Service
public class TrainerService {
    private final SpringGameRepository gameRepository;
    private final WordService wordService;

    public TrainerService(SpringGameRepository gameRepository, SpringWordRepository wordRepository){
        this.gameRepository = gameRepository;
        wordService = new WordService(wordRepository);
    }

    public Progress startNewGame(){
        String word = wordService.provideRandomWord(5);
        Game game = new Game();
        game.startNewRound(word);
        gameRepository.save(game);

        return game.showProgress();
    }

    public Progress guess(String attempt, String id) throws NotFoundException {
        Game game = gameRepository.findById(id).orElseThrow(() -> new NotFoundException("game not found"));
         try{
             game.guess(attempt);
         }catch (Exception e){
             System.out.println("mag niet");
         }
         gameRepository.save(game);
         return game.showProgress();
    }
    public Progress startNewRound(String id) throws NotFoundException {
        Game game = gameRepository.findById(id).orElseThrow(() -> new NotFoundException("game not found"));
        try{
            game.startNewRound(wordService.provideRandomWord(game.provideNextWordLength()));
        }catch (Exception e){
            System.out.println("mag niet");
        }
        gameRepository.save(game);
        return game.showProgress();
    }

}
