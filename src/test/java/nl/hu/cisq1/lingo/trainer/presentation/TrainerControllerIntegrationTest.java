package nl.hu.cisq1.lingo.trainer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.presentation.dto.AttemptInputDto;
import nl.hu.cisq1.lingo.trainer.presentation.dto.RoundInputDto;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TrainerControllerIntegrationTest {

    @MockBean
    private SpringGameRepository gameRepository;

    @MockBean
    private SpringWordRepository wordRepository;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("tets starting a new game")
    void startNewGame() throws Exception {
        when(wordRepository.findRandomWordByLength(5)).thenReturn(
                Optional.of(new Word("brood"))
        );

        RequestBuilder request = MockMvcRequestBuilders.post("/trainer/newGame");
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score", is(0)))
                .andExpect(jsonPath("$.gameID", is(0)))
                .andExpect(jsonPath("$.hint", is("b....")))
                .andExpect(jsonPath("$.feedbackList", hasSize(0)));
    }

    @Test
    @DisplayName("tets creating a new round after completing a round")
    void newRound() throws Exception {
        when(wordRepository.findByValue("woord")).thenReturn(Optional.of(new Word("woord")));
        when(wordRepository.findRandomWordByLength(5)).thenReturn(
                Optional.of(new Word("brood"))
        );
        when(wordRepository.findRandomWordByLength(6)).thenReturn(
                Optional.of(new Word("brokje"))
        );

        Game game = new Game();
        game.startNewRound(wordRepository.findRandomWordByLength(5).get().getValue());
        game.guess("brood");
        when(gameRepository.findById(0)).thenReturn(Optional.of(game));


        RoundInputDto roundInputDTO = new RoundInputDto();
        roundInputDTO.gameID = 0;
        String body = new ObjectMapper().writeValueAsString(roundInputDTO);

        RequestBuilder attemptRequest = MockMvcRequestBuilders.put("/trainer/continue")
                .contentType(MediaType.APPLICATION_JSON).content(body);

        mockMvc.perform(attemptRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackList", hasSize(0)))
                .andExpect(jsonPath("$.score", is(25)))
                .andExpect(jsonPath("$.gameID", is(0)));
    }

    @Test
    @DisplayName("cannot start new round when already started one")
    void NotStartNewRound() throws Exception {
        when(wordRepository.findByValue("woord")).thenReturn(Optional.of(new Word("woord")));
        when(wordRepository.findRandomWordByLength(5)).thenReturn(
                Optional.of(new Word("brood"))
        );
        when(wordRepository.findRandomWordByLength(6)).thenReturn(
                Optional.of(new Word("brokje"))
        );
        Game game = new Game();
        game.startNewRound(wordRepository.findRandomWordByLength(5).get().getValue());
        when(gameRepository.findById(0)).thenReturn(Optional.of(game));


        RoundInputDto roundInputDTO = new RoundInputDto();
        roundInputDTO.gameID = 0;
        String body = new ObjectMapper().writeValueAsString(roundInputDTO);

        RequestBuilder attemptRequest = MockMvcRequestBuilders.put("/trainer/continue")
                .contentType(MediaType.APPLICATION_JSON).content(body);

        mockMvc.perform(attemptRequest)
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("There is already an active round. complete that first")));
    }

    @Test
    @DisplayName("make a guess")
    void makeGuess() throws Exception {
        when(wordRepository.findByValue("woord")).thenReturn(Optional.of(new Word("woord")));
        when(wordRepository.findRandomWordByLength(5)).thenReturn(
                Optional.of(new Word("brood"))
        );

        Game game = new Game();
        game.startNewRound(wordRepository.findRandomWordByLength(5).get().getValue());

        when(gameRepository.findById(0)).thenReturn(Optional.of(game));


        AttemptInputDto input = new AttemptInputDto();
        input.attempt = "woord";
        input.gameID = 0;
        String body = new ObjectMapper().writeValueAsString(input);

        RequestBuilder attemptRequest = MockMvcRequestBuilders.put("/trainer/attempt")
                .contentType(MediaType.APPLICATION_JSON).content(body);

        mockMvc.perform(attemptRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackList", hasSize(1)))
                .andExpect(jsonPath("$.gameID", greaterThanOrEqualTo(0)));
    }

    @Test
    @DisplayName("get a game by its ID")
    void getGameById() throws Exception {
        when(wordRepository.findByValue("woord")).thenReturn(Optional.of(new Word("woord")));
        when(wordRepository.findRandomWordByLength(5)).thenReturn(
                Optional.of(new Word("brood"))
        );

        Game game = new Game();
        game.startNewRound(wordRepository.findRandomWordByLength(5).get().getValue());

        when(gameRepository.findById(0)).thenReturn(Optional.of(game));


        RoundInputDto input = new RoundInputDto();
        input.gameID = 0;
        String body = new ObjectMapper().writeValueAsString(input);

        RequestBuilder attemptRequest = MockMvcRequestBuilders.get("/trainer/getGame")
                .contentType(MediaType.APPLICATION_JSON).content(body);

        mockMvc.perform(attemptRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackList", hasSize(0)))
                .andExpect(jsonPath("$.gameID", greaterThanOrEqualTo(0)));
    }
    @Test
    @DisplayName("get all games")
    void getAll() throws Exception {
        when(wordRepository.findByValue("woord")).thenReturn(Optional.of(new Word("woord")));
        when(wordRepository.findRandomWordByLength(5)).thenReturn(
                Optional.of(new Word("brood"))
        );

        Game game1 = new Game();
        Game game2 = new Game();
        Game game3 = new Game();
        when(gameRepository.findAll()).thenReturn(List.of(game1, game2, game3));

        RequestBuilder attemptRequest = MockMvcRequestBuilders.get("/trainer/getAll")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(attemptRequest)
                .andExpect(status().isOk())
                ;
    }
}