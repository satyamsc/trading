package com.bank.trading.algo.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("MethodInvoker")
class AlgoMethodInvokerTest {

    @Test
    void ShouldInvokeMethodNoArgsWhenMethodExistsAndHasNoParameters() {
        SampleTestClass testObject = new SampleTestClass();
        String methodName = "isMethodWithNoArgsInvoked";

        assertDoesNotThrow(() -> new AlgoMethodInvoker().invokeMethodNoArgs(testObject, methodName));
    }

    @Test
    void ShouldInvokeMethodWithArgsWhenMethodHasParameters() {
        SampleTestClass testObject = new SampleTestClass();
        String methodName = "methodWithParameters";
        assertDoesNotThrow(() -> new AlgoMethodInvoker().invokeMethodWithArgs(testObject, methodName, List.of(1.2)));
    }

    @Test
    void ShouldNotInvokeMethodNoArgsWhenMethodDoesNotExist() {
        SampleTestClass testObject = mock(SampleTestClass.class);
        assertThrows(NoSuchMethodException.class, () -> new AlgoMethodInvoker().invokeMethodNoArgs(testObject, "nonExistingMethod"));
        // Verify that the method was not invoked
        verifyNoInteractions(testObject);
    }
}