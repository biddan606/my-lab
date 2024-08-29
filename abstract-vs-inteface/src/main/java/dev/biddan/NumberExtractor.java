package dev.biddan;

import java.util.ArrayList;
import java.util.List;

public class NumberExtractor extends FileDataExtractor<Integer> {

    public NumberExtractor(String fileName) {
        super(fileName);
    }

    @Override
    protected List<Integer> processData(List<String> rawData) {
        List<Integer> numbers = new ArrayList<>();

        for (String line : rawData) {
            try {
                numbers.add(Integer.parseInt(line.trim()));
            } catch (NumberFormatException e) {
                System.out.println("숫자가 아닌 값 무시: " + line);
            }
        }

        return numbers;
    }
}
