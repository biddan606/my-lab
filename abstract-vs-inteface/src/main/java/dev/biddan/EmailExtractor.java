package dev.biddan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EmailExtractor extends FileDataExtractor<String> {

    private  static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w+.-]+@[\\w+.-]+$");

    public EmailExtractor(String fileName) {
        super(fileName);
    }

    @Override
    protected List<String> processData(List<String> rawData) {
        List<String> emails = new ArrayList<>();

        for (String line : rawData) {
            if (EMAIL_PATTERN.matcher(line.trim()).matches()) {
                emails.add(line.trim());
            } else {
                System.out.println("유효하지 않은 이메일 무시: " + line);
            }
        }

        return emails;
    }
}
