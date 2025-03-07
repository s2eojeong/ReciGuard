package com.ReciGuard.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class InstructionRequestDTO {

    private Integer instructionId;
    private MultipartFile instructionImageFile;
    private String instruction;
    private boolean ImageRemoved;
    private String instructionImage; // 추가: S3에 업로드된 이미지 URL 저장
}
