package ru.unn.agile.calc.model;

import java.util.ArrayList;
import java.util.List;

public class StringCalc {
    public StringCalc() {

    }

    public double result(final String string) {
        return calcImpl(string);
    }

    private static final String SUMOPERATOR = "+";
    private static final String SUBOPERATOR = "-";
    private static final String MULOPERATOR = "*";
    private static final String DIVOPERATOR = "/";

    private enum Operations {
        SUMMARY {
            public double action(final double x, final double y) {
                return x + y;
            }
        },
        SUBTRACT {
            public double action(final double x, final double y) {
                return x - y;
            }
        },
        MULTIPLY {
            public double action(final double x, final double y) {
                return x * y;
            }
        },
        DIVIDE {
            public double action(final double x, final double y) {
                return x / y;
            }
        };
        public abstract double action(final double x, final double y);
    }

    private double calcImpl(final String string) {
        try {
            char firstSymbol = string.charAt(0);
            String[] parsedNumbers = string.split("\\+|\\-|\\*|\\/");
            String[] parsedOperators = string.split("[1234567890\\.a-z]*");
            int index = 0;
            char subSymbol = SUBOPERATOR.charAt(0);
            if (firstSymbol == subSymbol) {
                index++;
                parsedNumbers[index] = subSymbol + parsedNumbers[index];
                parsedOperators[0] = "";
            }

            List<Operations> operationsList = new ArrayList<Operations>();
            for (String operator : parsedOperators) {
                filterOperationSymbolsFromNonNumberSymbols(operationsList, operator);
            }

            double[] numbersArray = new double[parsedNumbers.length - index];
            parseNumbersFromStringToDouble(parsedNumbers, index, numbersArray);

            Operations[] operationsArray = new Operations[operationsList.size()];
            operationsList.toArray(operationsArray);

            double result = calcKernel(numbersArray, operationsArray);

            return result;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("String can't contain letters");
        }
    }

    private double calcKernel(final double[] numbersArray, final Operations[] operationsArray) {
        if (operationsArray.length == 0) {
            return numbersArray[0];
        }

        boolean isOnlyFirstPriorityOperationsInExpression = true;
        double resultFirstPriorityOperations = 0;
        int i = 0;
        for (Operations operation : operationsArray) {
            double resultTmp = 0;
            switch (operation) {
                case DIVIDE:
                    resultTmp = firstPriorityAction(numbersArray, i, operation);
                    break;
                case MULTIPLY:
                    resultTmp = firstPriorityAction(numbersArray, i, operation);
                    break;
                case SUMMARY:
                    isOnlyFirstPriorityOperationsInExpression = false;
                    break;
                case SUBTRACT:
                    isOnlyFirstPriorityOperationsInExpression = false;
                    break;
                default:
                    break;
            }
            resultFirstPriorityOperations = resultTmp;
            i++;
        }

        if (isOnlyFirstPriorityOperationsInExpression) {
            return resultFirstPriorityOperations;
        }

        i = 0;
        double result = 0;
        for (Operations operation : operationsArray) {
            double resultTmp = 0;
            switch (operation) {
                case SUMMARY:
                    result = secondPriorityAction(numbersArray, i, operation);
                    break;
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

    private double secondPriorityAction(final double[] numbers, final int i, final Operations operation) {
        double resultTmp;
        double result;
        resultTmp = operation.action(numbers[i], numbers[i + 1]);
        numbers[i + 1] = resultTmp;
        result = resultTmp;
        return result;
    }

    private double firstPriorityAction(final double[] numbers, final int i, final Operations operation) {
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

    private void filterOperationSymbolsFromNonNumberSymbols(
            final List<Operations> operationsList,
            final String operator) {
        switch (operator) {
            case SUMOPERATOR:
                operationsList.add(Operations.SUMMARY);
                break;
            case SUBOPERATOR:
                operationsList.add(Operations.SUBTRACT);
                break;
            case MULOPERATOR:
                operationsList.add(Operations.MULTIPLY);
                break;
            case DIVOPERATOR:
                operationsList.add(Operations.DIVIDE);
                break;
            default:
                break;
        }
    }
}
