package dev.biddan;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        NumberExtractor numberExtractor = new NumberExtractor("data.txt");
        System.out.println("=== NumberExtractor에서 추출 시작 ===");
        List<Integer> numbers = numberExtractor.extractData();

        System.out.println("=== NumberExtractor에서 추출된 값 ===");
        numbers.forEach(System.out::println);
        System.out.println();

        EmailExtractor emailExtractor = new EmailExtractor("data.txt");
        System.out.println("=== EmailExtractor에서 추출 시작 ===");
        List<String> emails = emailExtractor.extractData();

        System.out.println("=== EmailExtractor에서 추출된 값 ===");
        emails.forEach(System.out::println);
        System.out.println();
    }
}
