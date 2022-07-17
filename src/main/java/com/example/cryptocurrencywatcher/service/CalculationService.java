package com.example.cryptocurrencywatcher.service;

public class CalculationService {

    public static boolean isDifferenceBetweenValuesMoreThanPercentage(double firstValue, double secondValue, double percentage) {
        return CalculationService.getPercentageBetweenValues(firstValue, secondValue) > percentage;
    }

    public static double getPercentageBetweenValues(double firstValue, double secondValue){
        return firstValue / secondValue * 100 - 100;
    }
}
