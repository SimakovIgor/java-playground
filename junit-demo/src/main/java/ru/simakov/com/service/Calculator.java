package ru.simakov.com.service;

public class Calculator {

    public long add(final int a, final int b) {
        long result = (long) a + b;
        return result;
    }

    public double divide(final int a, final int b) {
        return a / b;
    }
}
