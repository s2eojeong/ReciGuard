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
    private boolean ImageRemoved;
    private String instructionImage; // 추가: S3에 업로드된 이미지 URL 저장

    @JsonCreator
    public InstructionRequestDTO(@JsonProperty("instructionId") Integer instructionId,
                                 @JsonProperty("instruction") String instruction,
                                 @JsonProperty("imageRemoved") boolean imageRemoved,
                                 @JsonProperty("instructionImageFile") MultipartFile instructionImageFile,
                                 @JsonProperty("instructionImage") String instructionImage) {
        this.instructionId = instructionId;
        this.instruction = instruction;
        this.ImageRemoved = imageRemoved;
        this.instructionImageFile = instructionImageFile;
        this.instructionImage = instructionImage;
    }

    public boolean hasInstructionImage() {
        return instructionImageFile != null && !instructionImageFile.isEmpty();
    }
}
