package com.bank.trading.service;

import com.bank.trading.algo.client.AlgoMethodInvoker;
import com.bank.trading.algo.stubs.Algo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

@SpringBootTest
class SignalProcessorTest {

    @Mock
    private Algo mockAlgo;

    private ResourceLoader resourceLoader;
    private SignalProcessor signalProcessor;

    @BeforeEach
    public void setUp() throws IOException {
        resourceLoader = new DefaultResourceLoader();
        JsonNode jsonNode = readConfig();
        AlgoMethodInvoker invoker = new AlgoMethodInvoker();
        signalProcessor = new SignalProcessor(mockAlgo, jsonNode, invoker);
    }

    public JsonNode readConfig() throws IOException {
        String configFilePath = "classpath:signal-config.json";
        try (InputStream inputStream = resourceLoader.getResource(configFilePath).getInputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(inputStream);
        }
    }

    @Test
    void ShouldHandleSignalFound() {
        int signalId = 1;
        signalProcessor.handleSignal(signalId);
        verify(mockAlgo).setUp();
        verify(mockAlgo).performCalc();
        verify(mockAlgo).submitToMarket();
        verify(mockAlgo).setAlgoParam(1, 60);

    }

    @Test
    void ShouldHandleSignalNotFound() {
        int signalId = 5;
        signalProcessor.handleSignal(signalId);
        verify(mockAlgo).cancelTrades();
        verify(mockAlgo, atLeastOnce()).cancelTrades();
        verify(mockAlgo, atLeastOnce()).doAlgo();
        verify(mockAlgo,never()).setUp();
        verify(mockAlgo,never()).performCalc();
        verify(mockAlgo,never()).submitToMarket();
    }
}
