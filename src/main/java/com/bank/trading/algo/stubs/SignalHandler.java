package com.bank.trading.algo.stubs;

import java.lang.reflect.InvocationTargetException;

/**
 * This is an upcall from our trading system, and we cannot change it.
 */
public interface SignalHandler {
    void handleSignal(int signal);
}
