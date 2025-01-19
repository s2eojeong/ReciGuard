package com.ReciGuard.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private Set<String> uploadedFileNames = new HashSet<>();
    private Set<Long> uploadedFileSizes = new HashSet<>();

    @Value("${spring.cloud.aws.S3.bucket}")
    private String bucket;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSizeString;

    private void clear() {
        uploadedFileNames.clear();
        uploadedFileSizes.clear();
    }

    //중복 사진 여부 확인
    private boolean isDuplicate(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        Long fileSize = multipartFile.getSize();

        if (uploadedFileNames.contains(fileName) && uploadedFileSizes.contains(fileSize)) {
            return true;
        }

        uploadedFileNames.add(fileName);
        uploadedFileSizes.add(fileSize);

        return false;
    }

    // 사진 파일 명 중복 방지
    private String generateRandomFilename(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = validateFileExtension(originalFilename);
        String randomFilename = UUID.randomUUID() + "." + fileExtension; // 중복 방지

        return randomFilename;
    }

    // 파일 확장자 체크
    private String validateFileExtension(String originalFilename) {
        // 파일 이름이 null이거나 비어 있으면 빈 문자열 반환
        if (originalFilename == null || originalFilename.isEmpty()) {
            return ""; // 이미지가 없는 상태를 처리
        }
        // 확장자 추출
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("Invalid file: 파일 확장자 없음");
        }
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif", "jpeg");

        if (!allowedExtensions.contains(fileExtension)) {
            throw new IllegalArgumentException("Invalid file: 파일 확장자 없음");
        }
        return fileExtension;
    }

    //단일 사진 저장
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

        log.info("Uploading single file: {}", file.getOriginalFilename());

        // 파일 확장자 검증 및 이름 생성
        String fileExtension = validateFileExtension(file.getOriginalFilename());
        String randomFilename = UUID.randomUUID() + "." + fileExtension;

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // S3에 파일 업로드
        try {
            amazonS3.putObject(bucket, randomFilename, file.getInputStream(), metadata);
        } catch (IOException e) {
            log.error("File upload failed for file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("File upload failed", e);
        }

        // 업로드된 파일의 URL 반환
        String uploadedUrl = amazonS3.getUrl(bucket, randomFilename).toString();
        log.info("Uploaded file URL: {}", uploadedUrl);
        return uploadedUrl;
    }

    // 여러 사진 저장 (1장도 가능)
    public Map<String, String> saveFiles(Map<String, MultipartFile> multipartFiles) {
        Map<String, String> uploadedUrls = new HashMap<>();

        multipartFiles.forEach((key, file) -> {
            String uploadedUrl = saveFile(file);
            uploadedUrls.put(key, uploadedUrl);
        });

        return uploadedUrls;
    }
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException("File URL cannot be null or empty");
        }

        int lastIndex = fileUrl.lastIndexOf("/");
        if (lastIndex == -1 || lastIndex == fileUrl.length() - 1) {
            throw new IllegalArgumentException("Invalid file URL: " + fileUrl);
        }

        String fileName = fileUrl.substring(lastIndex + 1);

        try {
            log.info("Attempting to delete file from S3. Bucket: {}, File: {}", bucket, fileName);
            amazonS3.deleteObject(bucket, fileName);
            log.info("File deleted successfully from S3. Bucket: {}, File: {}", bucket, fileName);
        } catch (AmazonServiceException e) {
            log.error("Failed to delete file from S3. Bucket: {}, File: {}, Error: {}", bucket, fileName, e.getMessage());
            throw new RuntimeException(
                    String.format("Failed to delete file '%s' from bucket '%s'", fileName, bucket), e);
        }
    }
}
