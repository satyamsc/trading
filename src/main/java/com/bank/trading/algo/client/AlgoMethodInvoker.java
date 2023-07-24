package com.bank.trading.algo.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * AlgoMethodInvoker is a utility class that provides methods for invoking methods on an object using Java Reflection.
 * It is used to dynamically call methods on an object with or without arguments.
 * The class also provides utility methods for handling primitive types and their wrapper classes.
 */
@Component
@Slf4j
public class AlgoMethodInvoker {

    /**
     * Invokes a method on the given object without any arguments.
     *
     * @param obj        The object on which the method is to be invoked.
     * @param methodName The name of the method to be invoked.
     * @throws NoSuchMethodException     If the specified method is not found in the object's class.
     * @throws InvocationTargetException If the invoked method throws an exception.
     * @throws IllegalAccessException    If the method is not accessible due to Java access control.
     */
    public void invokeMethodNoArgs(Object obj, String methodName) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {

        Class<?> cls = obj.getClass();
        Method method = cls.getMethod(methodName);
        if (method.getParameterCount() == 0) {
            method.invoke(obj);
        } else {
            log.info("Method not found .");
        }

    }

    /**
     * Invokes a method on the given object with the specified arguments.
     *
     * @param obj        The object on which the method is to be invoked.
     * @param methodName The name of the method to be invoked.
     * @param arguments  The arguments to be passed to the method.
     * @throws InvocationTargetException If the invoked method throws an exception.
     * @throws IllegalAccessException    If the method is not accessible due to Java access control.
     */
    public void invokeMethodWithArgs(Object obj, String methodName,
                                     Object... arguments) throws InvocationTargetException, IllegalAccessException {

        Class<?> cls = obj.getClass();
        Method method = getMethodWithArgs(cls, methodName, arguments);
        if (method != null) {
            method.invoke(obj, arguments);
        } else {
            log.info("Method not found or arguments do not match.");
        }
    }

    /**
     * Searches for a method in the given class that matches the specified method name and arguments.
     *
     * @param cls        The class in which to search for the method.
     * @param methodName The name of the method to be searched.
     * @param arguments  The arguments for which to find a matching method.
     * @return The Method object representing the matching method, or null if not found.
     */

    private Method getMethodWithArgs(Class<?> cls, String methodName, Object... arguments) {
        Method[] methods = cls.getMethods();
        return Arrays.stream(methods).filter(method -> method.getName().equals(methodName)
                && method.getParameterCount() == arguments.length).filter(method -> {
            Class<?>[] parameterTypes = method.getParameterTypes();
            return IntStream.range(0, parameterTypes.length)
                    .allMatch(i -> isPrimitive(parameterTypes[i]) && isWrapper(arguments[i], parameterTypes[i]));
        }).findFirst().orElse(null);
    }

    /**
     * Checks if the given class represents a primitive type.
     *
     * @param cls The class to check.
     * @return True if the class represents a primitive type, otherwise false.
     */
    private boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive();
    }

    /**
     * Checks if the given object is of the same type or a wrapper class of the specified class.
     *
     * @param obj The object to check.
     * @param cls The class to compare with.
     * @return True if the object is of the same type or a wrapper class of the specified class, otherwise false.
     */
    private boolean isWrapper(Object obj, Class<?> cls) {
        Class<?> objClass = obj.getClass();
        if (!objClass.isPrimitive()) {
            cls = getWrapperClass(cls);
        }
        return cls.isAssignableFrom(objClass);
    }

    /**
     * Returns the corresponding wrapper class for a primitive type.
     *
     * @param cls The primitive class for which to find the wrapper class.
     * @return The wrapper class for the specified primitive class.
     */
    private Class<?> getWrapperClass(Class<?> cls) {
        String className = cls.getName();
        return switch (className) {
            case "int" -> Integer.class;
            case "double" -> Double.class;
            case "float" -> Float.class;
            case "long" -> Long.class;
            case "short" -> Short.class;
            case "byte" -> Byte.class;
            case "char" -> Character.class;
            case "boolean" -> Boolean.class;
            default -> cls;
        };
    }
}
