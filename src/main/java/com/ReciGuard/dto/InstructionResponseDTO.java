package com.ReciGuard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class InstructionResponseDTO {

    private Integer instructionId;
    private String instructionImage; // 단계 번호
    private String instruction;

    public InstructionResponseDTO(Integer instructionId, String instructionImage, String instruction) {
        this.instructionId = instructionId;
        this.instructionImage = instructionImage;
        this.instruction = instruction;
    }
}
