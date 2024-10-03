package dev.biddan.uploadingimagetos3;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AwsProperties {

    private final String accessKey;

    private final String secretKey;

    private final String region;

    private final String bucketName;

    public AwsProperties(
            @Value("${aws.accessKeyId}") String accessKey,
            @Value("${aws.secretAccessKey}") String secretKey,
            @Value("${aws.region}") String region,
            @Value("${aws.bucketName}") String bucketName) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.bucketName = bucketName;
    }
}
