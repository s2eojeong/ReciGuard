package com.ReciGuard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class InstructionRequestDTO {

    private Integer instructionId;
    private MultipartFile instructionImageFile;
    private String instruction;
    private boolean imageRemoved;
    private String instructionImage; // 추가: S3에 업로드된 이미지 URL 저장

    @JsonCreator
    public InstructionRequestDTO(@JsonProperty("instruction") String instruction,
                                 @JsonProperty("imageRemoved") boolean imageRemoved,
                                 @JsonProperty("instructionImageFile") MultipartFile instructionImageFile) {
        this.instruction = instruction;
        this.imageRemoved = imageRemoved;
        this.instructionImageFile = instructionImageFile;
    }

    public boolean hasInstructionImage() {
        return instructionImageFile != null && !instructionImageFile.isEmpty();
    }
}
