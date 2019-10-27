package com.ao.importCsvToH2.person;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ImportCsv {

    private static final String SAMPLE_CSV_FILE_PATH = "fileToImport.csv";

    public List<String[]> importCsvFromFile() throws IOException {

        Path myPath = Paths.get(SAMPLE_CSV_FILE_PATH);

        try (BufferedReader reader = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .withIgnoreQuotations(true)
                    .build();

            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .withCSVParser(parser)
                    .build();

            List<String[]> trimmedFile = new ArrayList<>();
            for (String[] strings : csvReader) {
                String[] array = Arrays.stream(strings).map(String::trim).toArray(String[]::new);
                trimmedFile.add(array);
            }
            return trimmedFile;
        }
    }
}