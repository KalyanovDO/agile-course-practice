package ru.unn.agile.dijkstraalgorithm.infrastructure;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class RegExprMatcher extends BaseMatcher {
    private final String regex;

    public RegExprMatcher(final String regex) {
        this.regex = regex;
    }

    public boolean matches(final Object o) {
        return ((String) o).matches(regex);
    }

    public static Matcher<? super String> matchesPattern(final String regex) {
        RegExprMatcher matcher = new RegExprMatcher(regex);
        //NOTE: this ugly cast is needed to workaround 'unchecked' Java warning
        @SuppressWarnings (value = "unchecked")
        Matcher<? super String> castedMatcher = (Matcher<? super String>)   matcher;
        return castedMatcher;
    }

    public void describeTo(final Description description) {
        description.appendText("matches regex = ");
        description.appendText(regex);
    }
}
