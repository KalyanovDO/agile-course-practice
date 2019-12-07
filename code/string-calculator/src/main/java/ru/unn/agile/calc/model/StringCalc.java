package ru.unn.agile.calc.model;

import java.util.ArrayList;
import java.util.List;

public class StringCalc {
    public StringCalc() {

    }

    public double result(final String string) {
        if (string == null) {
            throw new IllegalArgumentException("String can't be NULL");
        }
        if (string.equals("")) {
            throw new IllegalArgumentException("String can't be empty");
        }
        char firstSymbol = string.charAt(0);
        String[] parsedNumbers;
        String[] parsedOperators;
        try {
            parsedNumbers = string.split("\\+|\\-|\\*|\\/");
            parsedOperators = string.split("[0-9\\.]*");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("String can't contain letters");
        }
        int index = 0;
        char subSymbol = Operation.SUBTRACT.value().charAt(0);
        if (firstSymbol == subSymbol) {
            index++;
            parsedNumbers[index] = subSymbol + parsedNumbers[index];
            parsedOperators[0] = "";
        }

        List<Operation> operationsList = new ArrayList<Operation>();

        for (String operator : parsedOperators) {
            if (Operation.getOperationBySymbol(operator) != null) {
                operationsList.add(Operation.getOperationBySymbol(operator));
            }
        }

        double[] numbersArray = new double[parsedNumbers.length - index];
        parseNumbersFromStringToDouble(parsedNumbers, index, numbersArray);

        Operation[] operationsArray = new Operation[operationsList.size()];
        operationsList.toArray(operationsArray);

        return calcKernel(numbersArray, operationsArray);
    }

    private double calcKernel(final double[] numbersArray, final Operation[] operationsArray) {
        if (operationsArray.length == 0) {
            return numbersArray[0];
        }

        boolean isOnlyFirstPriorityOperationInExpression = true;
        double resultFirstPriorityOperation = 0;
        int i = 0;
        for (Operation operation : operationsArray) {
            double resultTmp = 0;
            switch (operation) {
                case DIVIDE:
                case MULTIPLY:
                    resultTmp = firstPriorityAction(numbersArray, i, operation);
                    break;
                case SUMMARY:
                case SUBTRACT:
                    isOnlyFirstPriorityOperationInExpression = false;
                    break;
                default:
                    break;
            }
            resultFirstPriorityOperation = resultTmp;
            i++;
        }

        if (isOnlyFirstPriorityOperationInExpression) {
            return resultFirstPriorityOperation;
        }

        i = 0;
        double result = 0;
        for (Operation operation : operationsArray) {
            switch (operation) {
                case DIVIDE:
                case MULTIPLY:
                    numbersArray[i+1] = numbersArray[i];
                    break;
                case SUMMARY:
                case SUBTRACT:
                    result = secondPriorityAction(numbersArray, i, operation);
                    break;
                default:
                    break;
            }
            i++;
        }
        return result;
    }

    private double secondPriorityAction(
            final double[] numbers,
            final int i,
            final Operation operation) {
        double resultTmp;
        double result;
        resultTmp = operation.action(numbers[i], numbers[i + 1]);
        numbers[i + 1] = resultTmp;
        result = resultTmp;
        return result;
    }

    private double firstPriorityAction(
            final double[] numbers,
            final int i,
            final Operation operation) {
        double resultTmp;
        resultTmp = operation.action(numbers[i], numbers[i + 1]);
        numbers[i] = resultTmp;
        numbers[i + 1] = resultTmp;
        return resultTmp;
    }

    private void parseNumbersFromStringToDouble(
            final String[] parsedNumbers,
            final int index,
            final double[] numbers) {
        for (int i = 0; i < parsedNumbers.length - index; i++) {
            numbers[i] = Double.parseDouble(parsedNumbers[i + index]);
        }
    }
}
