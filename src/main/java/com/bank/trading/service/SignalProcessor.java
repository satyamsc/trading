package com.bank.trading.service;

import com.bank.trading.algo.client.AlgoMethodInvoker;
import com.bank.trading.algo.stubs.Algo;
import com.bank.trading.algo.stubs.SignalHandler;
import com.bank.trading.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

/**
 * SignalProcessor is a service responsible for handling trading signals and invoking corresponding actions on the
 * Algo Object.
 */
@Service
@AllArgsConstructor
@Slf4j
public class SignalProcessor implements SignalHandler {

    private final Algo algo;
    private final JsonNode signalConfig;
    private final AlgoMethodInvoker algoMethodInvoker;

    /**
     * This method is called to handle a trading signal with the specified ID.
     *
     * @param signal The ID of the trading signal to be processed.
     */
    @Override
    public void handleSignal(int signal) {
        // Retrieve the signals array from the signal configuration
        JsonNode signalsArray = signalConfig.get("signals");
        if (signalsArray == null || !signalsArray.isArray()) {
            return;
        }

        // Check if the specified signal ID is present in the signals array
        boolean isFound = StreamSupport.stream(signalsArray.spliterator(), false)
                .anyMatch(signalNode -> signalNode.has("id") && signalNode.get("id").isInt() && signalNode.get("id").asInt() == signal);

        // Process the signal if it is found
        if (isFound) {
            StreamSupport.stream(signalsArray.spliterator(), false)
                    .filter(signalNode -> signalNode.get("id").asInt() == signal)
                    .flatMap(signalNode -> StreamSupport.stream(signalNode.get("actions").spliterator(), false))
                    .forEach(action -> {
                        String method = action.get("method").asText();
                        JsonNode params = action.get("params");
                        try {
                            executeAlgoAction(method, params);
                        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                            log(method, e);
                        }
                    });
        } else {
            // If the signal is not found, cancel all trades
            try {
                executeAlgoAction("cancelTrades", null);
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                log("cancelTrades", e);
            }
        }

        // Perform the main algorithm process
        algo.doAlgo();
    }

    /**
     * Logs the method and the exception when an error occurs during execution.
     *
     * @param method The method name where the error occurred.
     * @param e      The exception that was thrown.
     */
    private void log(String method, ReflectiveOperationException e) {
        log.error(" Error while executing {}, {}", method, e.getMessage());
        throw new ResourceNotFoundException("Resource or Method Not found." + e.getMessage());
    }

    /**
     * Executes the specified algorithm action with the given parameters.
     *
     * @param method The name of the algorithm action to be executed.
     * @param params The parameters to be passed to the algorithm action.
     * @throws InvocationTargetException If the invoked method throws an exception.
     * @throws IllegalAccessException    If the method cannot be accessed.
     * @throws NoSuchMethodException     If the method is not found.
     */
    private void executeAlgoAction(String method, JsonNode params) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (params != null) {
            List<Object> list = new ArrayList<>();
            params.forEach(element -> addParamValue(element, list));

            // Invoke the algorithm method with arguments
            algoMethodInvoker.invokeMethodWithArgs(algo, method, list.toArray());
            return;
        }

        // Invoke the algorithm method without arguments
        algoMethodInvoker.invokeMethodNoArgs(algo, method);
    }

    /**
     * Adds the parameter value from the JSON node to the list of values.
     *
     * @param element The JSON node representing the parameter value.
     * @param values  The list to which the parameter value will be added.
     */
    private void addParamValue(JsonNode element, List<Object> values) {
        switch (element.getNodeType()) {
            case NUMBER:
                if (element.isDouble()) {
                    values.add(element.asDouble());
                } else if (element.isInt()) {
                    values.add(element.asInt());
                }
                break;
            case BOOLEAN:
                values.add(element.asBoolean());
                break;
            case STRING:
                values.add(element.asText());
                break;
            default:
                // Handle other node types, if needed
                break;
        }
    }
}
