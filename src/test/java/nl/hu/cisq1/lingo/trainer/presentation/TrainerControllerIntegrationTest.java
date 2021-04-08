package nl.hu.cisq1.lingo.trainer.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.presentation.DTO.AttemptInputDTO;
import nl.hu.cisq1.lingo.trainer.presentation.DTO.RoundInputDTO;
import nl.hu.cisq1.lingo.words.data.SpringWordRepository;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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


        RoundInputDTO roundInputDTO = new RoundInputDTO();
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


        RoundInputDTO roundInputDTO = new RoundInputDTO();
        roundInputDTO.gameID = 0;
        String body = new ObjectMapper().writeValueAsString(roundInputDTO);

        RequestBuilder attemptRequest = MockMvcRequestBuilders.put("/trainer/continue")
                .contentType(MediaType.APPLICATION_JSON).content(body);

        mockMvc.perform(attemptRequest)
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("There is already an active round. complete that first")));
    }

    @Test
    void makeGuess() throws Exception {
        when(wordRepository.findByValue("woord")).thenReturn(Optional.of(new Word("woord")));
        when(wordRepository.findRandomWordByLength(5)).thenReturn(
                Optional.of(new Word("brood"))
        );

        Game game = new Game();
        game.startNewRound(wordRepository.findRandomWordByLength(5).get().getValue());

        when(gameRepository.findById(0)).thenReturn(Optional.of(game));


        AttemptInputDTO input = new AttemptInputDTO();
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
}