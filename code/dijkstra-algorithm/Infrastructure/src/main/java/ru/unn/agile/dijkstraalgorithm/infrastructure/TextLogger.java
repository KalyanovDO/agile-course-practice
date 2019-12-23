package ru.unn.agile.dijkstraalgorithm.infrastructure;

import ru.unn.agile.dijkstraalgorithm.viewmodel.ILogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TextLogger implements ILogger {
    private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private final BufferedWriter writer;
    private final String filename;

    private static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW, Locale.ENGLISH);
        return sdf.format(cal.getTime());
    }

    public TextLogger(final String filename) {
        this.filename = filename;

        BufferedWriter bufferedLogWriter = null;
        try {
            bufferedLogWriter = new BufferedWriter(new FileWriter(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        writer = bufferedLogWriter;
    }

    @Override
    public void log(final String s) {
        try {
            writer.write(now() + " > " + s);
            writer.newLine();
            writer.flush();
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    @Override
    public List<String> getLog() {
        BufferedReader bufferedLogReader;
        ArrayList<String> log = new ArrayList<String>();
        try {
            bufferedLogReader = new BufferedReader(new FileReader(filename));
            String line = bufferedLogReader.readLine();

            while (line != null) {
                log.add(line);
                line = bufferedLogReader.readLine();
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }

        return log;
    }

}
