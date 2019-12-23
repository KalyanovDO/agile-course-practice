package ru.unn.agile.dijkstraalgorithm.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class EmptyLogger implements ILogger {
    private final ArrayList<String> log = new ArrayList<>();

    @Override
    public List<String> getLog() {
        return log;
    }

    @Override
    public void log(final String s) {
        log.add(s);
    }
}
