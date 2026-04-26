package com.github.emmanuelAron.controller;

import com.github.emmanuelAron.service.ReplayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WebSocketReplayController.class)
class WebSocketReplayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReplayService replayService;

    /**
     * Verifies that the replay endpoint returns HTTP 204 (No Content)
     * and correctly delegates the request to the ReplayService.
     */
    @Test
    void shouldReturnNoContentAndCallService() throws Exception {
        // when
        mockMvc.perform(get("/replay/chess/ruy_lopez/CHIGORIN"))
                .andExpect(status().isNoContent());

        // then
        verify(replayService).replay("chess", "ruy_lopez", "CHIGORIN");
    }
}