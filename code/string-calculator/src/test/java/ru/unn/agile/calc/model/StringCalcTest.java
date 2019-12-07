package ru.unn.agile.calc.model;

import java.util.concurrent.ThreadLocalRandom;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringCalcTest {
    private final double delta = 0.00000001;
    @Test
    public void canParseNumber1() {
        StringCalc calc = new StringCalc();
        var number = calc.result("1");
        assertEquals(1, number, delta);
    }

    @Test
    public void canParseNumber143() {
        StringCalc calc = new StringCalc();
        var number = calc.result("143");
        assertEquals(143, number, delta);
    }

    @Test
    public void canParseRandomNumber() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, 100000);
        StringCalc calc = new StringCalc();
        var number = calc.result(Integer.toString(randomNum));
        assertEquals(randomNum, number, delta);
    }

    @Test
    public void canParseNotInt() {
        StringCalc calc = new StringCalc();
        var number = calc.result("143.26");
        assertEquals(143.26, number, delta);
    }

    @Test
    public void canParseSumOfTwoNumbers() {
        StringCalc calc = new StringCalc();
        var number = calc.result("143.26+2352.549");
        assertEquals(143.26 + 2352.549, number, delta);
    }

    @Test
    public void canParseSumOfThreeNumbers() {
        StringCalc calc = new StringCalc();
        var number = calc.result("143.26+2352.549+25436.25");
        assertEquals(143.26 + 2352.549 + 25436.25, number, delta);
    }

    @Test(expected = NumberFormatException.class)
    public void cantParseSumOfNumbersAndStrings() {
        StringCalc calc = new StringCalc();
        var number = calc.result("143.26+2352.549+ewffwfe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void cantParseNullStrings() {
        StringCalc calc = new StringCalc();
        var number = calc.result(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cantParseEmptyStrings() {
        StringCalc calc = new StringCalc();
        var number = calc.result("");
    }

    @Test
    public void canParseSumAndResidualOfThreeNumbers() {
        StringCalc calc = new StringCalc();
        var number = calc.result("1+1-1");
        assertEquals(1 + 1 - 1, number, delta);
    }


    @Test
    public void canParseSumAndResidualOfThreeNumbers2() {
        StringCalc calc = new StringCalc();
        var number = calc.result("-1+1-1");
        assertEquals(-1 + 1 - 1, number, delta);
    }

    @Test
    public void canParseMultiplication() {
        StringCalc calc = new StringCalc();
        var number = calc.result("5*6");
        assertEquals(5 * 6, number, delta);
    }

    @Test
    public void canParseMultiplicationWithSum() {
        StringCalc calc = new StringCalc();
        var number = calc.result("-7+5*6");
        assertEquals(-7 + 5 * 6, number, delta);
    }

    @Test
    public void canParseMultiplicationWithSum2() {
        StringCalc calc = new StringCalc();
        var number = calc.result("-7*5+1+5*6");
        assertEquals(-7 * 5 + 1 + 5 * 6, number, delta);
    }

    @Test
    public void canParseMultiplicationWithSum3() {
        StringCalc calc = new StringCalc();
        var number = calc.result("1 + 2*3 - 4/5");
        assertEquals(1 + 2*3 - 4./5., number, delta);
    }

    @Test
    public void canParseMultiplicationWithSum4() {
        StringCalc calc = new StringCalc();
        var number = calc.result("1 - 7 * 9 + 1");
        assertEquals(1 - 7 * 9 + 1, number, delta);
    }

    @Test
    public void canParseDivide() {
        StringCalc calc = new StringCalc();
        var number = calc.result("4/5");
        assertEquals(4./5., number, delta);
    }
}
