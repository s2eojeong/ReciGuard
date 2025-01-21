package com.ReciGuard.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    // 파일 해시 계산
    public String calculateFileHash(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = file.getBytes();
        byte[] hash = digest.digest(fileBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash); // URL-safe 해시값 생성
    }

    //단일 사진 저장
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

        log.info("Uploading single file: {}", file.getOriginalFilename());

        // 파일 확장자 검증 및 해시 기반 이름 생성
        String fileExtension = validateFileExtension(file.getOriginalFilename());
        String fileHash;
        try {
            fileHash = calculateFileHash(file); // 파일 해시값 계산
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error("Failed to calculate file hash for: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("File hash calculation failed", e);
        }
        String hashedFilename = fileHash + "." + fileExtension;

        // S3에 동일한 파일이 있는지 확인
        if (amazonS3.doesObjectExist(bucket, hashedFilename)) {
            log.info("File already exists in S3: {}", hashedFilename);
            return amazonS3.getUrl(bucket, hashedFilename).toString(); // 기존 파일 URL 반환
        }

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // S3에 파일 업로드
        try {
            amazonS3.putObject(bucket, hashedFilename, file.getInputStream(), metadata);
        } catch (IOException e) {
            log.error("File upload failed for file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("File upload failed", e);
        }

        // 업로드된 파일의 URL 반환
        String uploadedUrl = amazonS3.getUrl(bucket, hashedFilename).toString();
        log.info("Uploaded file URL: {}", uploadedUrl);
        return uploadedUrl;
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

    public byte[] downloadFile(String s3FilePath) {
        // S3에서 파일 가져오기
        try (S3Object s3Object = amazonS3.getObject(bucket, s3FilePath)) {
            // 파일 내용을 바이트 배열로 변환
            return IOUtils.toByteArray(s3Object.getObjectContent());
        } catch (Exception e) {
            // 예외 발생 시 로그 출력 및 null 반환
            System.err.println("Failed to download file from S3: " + e.getMessage());
            return null;
        }
    }
}
