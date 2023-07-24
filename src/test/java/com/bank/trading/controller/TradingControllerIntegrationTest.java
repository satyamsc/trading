package com.bank.trading.controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
 class TradingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testReceiveSignal_Success() throws Exception {
        mockMvc.perform(post("/signal/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signal 1 processed."));

    }

    @Test
    void testReceiveSignal_NegativeId() throws Exception {
        mockMvc.perform(post("/signal/{id}", -1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signal -1 processed."));

    }

    @Test
    void testReceiveSignal_err() throws Exception {
        mockMvc.perform(post("/signal/{id}", 4))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Resource or Method Not found.com.bank.trading.algo.stubs.Algo.setAlgoParam()"));

    }
}
