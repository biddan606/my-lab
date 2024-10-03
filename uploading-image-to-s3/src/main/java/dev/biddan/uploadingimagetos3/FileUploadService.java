package dev.biddan.uploadingimagetos3;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final S3Client s3Client;
    private final AwsProperties awsProperties;

    public String uploadFile(MultipartFile file) {
        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getBucketName())
                .key(key)
                .build();

        try {
            PutObjectResponse objectResponse = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes()));

            log.info("objectResponse={}", objectResponse);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는데 실패하였습니다: " + e.getMessage());
        }

        return "https://" + awsProperties.getBucketName() + ".s3.amazonaws.com/" + key;
    }
}
