package com.bank.trading.config;

import com.bank.trading.algo.stubs.Algo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
/**
 * LoadConfig is a configuration class responsible for creating beans and reading the configuration from a JSON file.
 * The class is annotated with @Configuration, indicating that it contains Spring configuration beans.
 */
@Configuration
@AllArgsConstructor
public class LoadConfig {

    /**
     * ResourceLoader is used to load resources such as files from the classpath or the file system.
     */
    private final ResourceLoader resourceLoader;

    /**
     * Creates a new instance of the Algo bean and registers it with the Spring container.
     * The Algo bean is used to represent a stub for the trading algorithm.
     *
     * @return Algo - The Algo bean instance.
     */
    @Bean
    public Algo getAlgo() {
        return new Algo();
    }

    /**
     * Reads the configuration data from a JSON file and converts it into a JsonNode object.
     * The JSON file is specified by the configFilePath variable, and it is read using the ResourceLoader.
     * The ObjectMapper class is used to parse the JSON data and create the JsonNode object.
     *
     * @return JsonNode - The root node of the JSON configuration data.
     * @throws IOException if there is an error while reading the JSON file.
     */
    @Bean
    public JsonNode readConfig() throws IOException {
        String configFilePath = "classpath:signal-config.json";
        try (InputStream inputStream = resourceLoader.getResource(configFilePath).getInputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(inputStream);
        }
    }
}
