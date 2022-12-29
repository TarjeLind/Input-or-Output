package ru.netology;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    protected static List<String[]> list = new ArrayList<>();

    public static void log(int productNum, int amount) {
        list.add(new String[]{Integer.toString(productNum), Integer.toString(amount)});
        System.out.println();
    }

    public static void exportCSV(File file) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("log.csv", true))) {
            csvWriter.writeAll(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
