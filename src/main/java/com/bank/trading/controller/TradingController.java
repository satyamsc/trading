package com.bank.trading.controller;

import com.bank.trading.dto.SignalResponse;
import com.bank.trading.algo.stubs.SignalHandler;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TradingController is a Spring REST controller & responsible for receiving trading signals and processing them using
 * a SignalHandler.
 */
@AllArgsConstructor
@RestController
public class TradingController {

    /**
     * The SignalHandler instance used to process trading signals.
     */
    private final SignalHandler signalProcessor;

    /**
     * Receives a trading signal with the specified ID and processes it using the SignalHandler.
     *
     * @param signalId The ID of the trading signal to be processed.
     * @return SignalResponse - A response indicating that the signal has been processed.
     */
    @PostMapping("/signal/{signalId}")
    public SignalResponse receiveSignal(@PathVariable int signalId) {
        // Call the handleSignal method of the SignalHandler to process the signal
        signalProcessor.handleSignal(signalId);

        // Create and return a SignalResponse with a success message
        return SignalResponse.builder()
                .message("Signal " + signalId + " processed.")
                .build();
    }
}
