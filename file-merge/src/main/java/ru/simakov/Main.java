package ru.simakov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

@SuppressWarnings("PMD.AssignmentInOperand")
public class Main {
    public static void main(final String[] args) throws IOException {
        Path newFilePath = Paths.get("PrideAndPrejudiceTranslate.txt");
        if (Files.exists(newFilePath)) {
            Files.delete(newFilePath);
        }

        Files.createFile(newFilePath);

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (InputStream inputStreamRu = classloader.getResourceAsStream("Pride and Prejudice.txt");
             InputStreamReader streamReaderRu = new InputStreamReader(Objects.requireNonNull(inputStreamRu), StandardCharsets.UTF_8);
             BufferedReader readerRu = new BufferedReader(streamReaderRu);
             InputStream inputStreamEn = classloader.getResourceAsStream("Pride and Prejudice RU.txt");
             InputStreamReader streamReaderEn = new InputStreamReader(Objects.requireNonNull(inputStreamEn), StandardCharsets.UTF_8);
             BufferedReader readerEn = new BufferedReader(streamReaderEn)) {

            String lineEn;
            String lineRu;

            while ((lineEn = readerEn.readLine()) != null && (lineRu = readerRu.readLine()) != null) {
                String string = lineEn.isEmpty() || lineEn.charAt(0) == '0' || lineEn.charAt(0) == '1'
                                ? lineEn + System.lineSeparator()
                                : lineEn + " " + lineRu + System.lineSeparator();

                Files.writeString(newFilePath, string, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            }
        }
    }
}
