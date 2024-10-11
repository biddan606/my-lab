package dev.biddan.multipartfiledemo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.Builder;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileApiController {

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // MultipartFile을 Resource로 변환
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html#getResource--
        Resource resource = file.getResource();

        // 파일 내용을 바이트 배열로 읽고 문자열로 변환
        byte[] fileContent = file.getInputStream().readAllBytes();
        String fileContentString = new String(fileContent, StandardCharsets.UTF_8);

        FileUploadResponse response = FileUploadResponse.builder()
                .originalFileName(file.getOriginalFilename())   // 원본 파일명: 사용자가 업로드한 파일의 원래 이름을 표시할 때 사용
                .contentType(file.getContentType())             // 컨텐츠 타입: 파일의 MIME 타입을 확인하여 적절한 처리 방법을 결정할 때 사용
                .name(file.getName())                           // 폼 필드 이름: 멀티파트 요청에서 해당 파일 필드를 식별할 때 사용
                .size(file.getSize())                           // 파일 크기(바이트 단위): 파일 용량 제한 확인이나 저장 공간 관리에 사용
                .empty(file.isEmpty())                          // 파일 비어있음 여부: 유효한 파일이 업로드되었는지 확인할 때 사용
                .bytes(Base64.getEncoder()
                        .encodeToString(file.getBytes()))  // Base64 인코딩된 파일 내용: 파일을 문자열로 전송하거나 저장할 때 사용
                .exists(resource.exists())                      // 리소스 존재 여부: 파일이 실제로 존재하는지 확인할 때 사용, 업로드 되었으므로 항상 true
                .readable(resource.isReadable())                // 읽기 가능 여부: 파일에 대한 읽기 권한이 있는지 확인할 때 사용, 업로드 되어 읽을 수 있음 getBytes() 가능
                .file(resource.isFile())                        // 파일 시스템 파일 여부: 리소스가 실제 파일 시스템의 파일인지 확인할 때 사용, 메모리나 임시로 디스크에 저장되어 false
                .description(resource.getDescription())         // 리소스 설명: 디버깅이나 로깅 목적으로 리소스에 대한 정보를 제공할 때 사용
                .fileContent(fileContentString)                 // 파일 내용: 텍스트 파일의 경우 내용을 직접 확인하거나 처리할 때 사용
                .build();

        return ResponseEntity.ok(response);
    }

    @Builder
    public record FileUploadResponse(
            String originalFileName,
            String contentType,
            String name,
            long size,
            boolean empty,
            String bytes,
            boolean exists,
            boolean readable,
            boolean file,
            String description,
            String fileContent
    ) {

    }
}
