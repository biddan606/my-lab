package dev.biddan;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FileDataExtractor<T> {

    protected String fileName;

    protected FileDataExtractor(String fileName) {
        this.fileName = fileName;
    }

    public final List<T> extractData() {
        List<String> rawData = readFile();
        return processData(rawData);
    }

    private List<String> readFile() {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("파일을 읽는 중 에러 발생: " + e.getMessage());
        }

        return lines;
    }

    protected abstract List<T> processData(List<String> rawData);
}
