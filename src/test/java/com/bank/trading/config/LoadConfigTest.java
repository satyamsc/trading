package com.bank.trading.config;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {LoadConfig.class})
@ExtendWith(SpringExtension.class)
class LoadConfigTest {
    @Autowired
    private LoadConfig loadConfig;

    @MockBean
    private ResourceLoader resourceLoader;

    @Test
    void shouldReadConfig() throws IOException {
        assertTrue(loadConfig.readConfig() instanceof ObjectNode);
    }
}

