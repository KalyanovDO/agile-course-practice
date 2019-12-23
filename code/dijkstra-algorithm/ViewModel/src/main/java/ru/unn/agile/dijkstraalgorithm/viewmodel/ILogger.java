package ru.unn.agile.dijkstraalgorithm.viewmodel;

import java.util.List;

public interface ILogger {
    void log(String s);

    List<String> getLog();
}
