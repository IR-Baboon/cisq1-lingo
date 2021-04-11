package nl.hu.cisq1.lingo.trainer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.presentation.dto.AttemptInputDto;
import nl.hu.cisq1.lingo.trainer.presentation.dto.RoundInputDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class TrainerControllerWebTest {
    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("start a new game, check basevalues")
    void startNewGameWebTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/trainer/newGame");
        mockMvc.perform(requestBuilder)
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.score", is(0))
                )
                .andExpect(
                        jsonPath("$.gameStatus", is("PLAYING"))
                );
    }

    @Test
    @DisplayName("make a guess after a game has started")
    void guessWebTest() throws Exception {
        RequestBuilder gameRequest = MockMvcRequestBuilders.post("/trainer/newGame");

        MockHttpServletResponse response = mockMvc.perform(gameRequest).andReturn().getResponse();
        Integer id = JsonPath.read(response.getContentAsString(), "$.gameID");

        AttemptInputDto input = new AttemptInputDto();
        input.attempt = "pizza";
        input.gameID = id;
        String body = new ObjectMapper().writeValueAsString(input);

        RequestBuilder attemptRequest = MockMvcRequestBuilders.put("/trainer/attempt")
                .contentType(MediaType.APPLICATION_JSON).content(body);

        mockMvc.perform(attemptRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackList", hasSize(1)))
            .andExpect(jsonPath("$.gameID", greaterThanOrEqualTo(0)));
    }

    @Test
    @DisplayName("start a new game, check if starting new round gives bad request")
    void cannotStartNewRoundWebTest() throws Exception {
        RequestBuilder gameRequest = MockMvcRequestBuilders.post("/trainer/newGame");

        MockHttpServletResponse response = mockMvc.perform(gameRequest).andReturn().getResponse();
        Integer id = JsonPath.read(response.getContentAsString(), "$.gameID");

        RoundInputDto input = new RoundInputDto();
        input.gameID = id;
        String body = new ObjectMapper().writeValueAsString(input);

        RequestBuilder attemptRequest = MockMvcRequestBuilders.put("/trainer/continue")
                .contentType(MediaType.APPLICATION_JSON).content(body);

        mockMvc.perform(attemptRequest)
                .andExpect(status().isBadRequest())
        .andExpect(status().reason(containsString("There is already an active round. complete that first")) );
    }
}