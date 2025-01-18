package com.ReciGuard.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class InstructionResponseDTO {
    private String instructionImage; // 단계 번호
    private String instruction;

    public InstructionResponseDTO(String instructionImage, String instruction) {
        this.instructionImage = instructionImage;
        this.instruction = instruction;
    }
}
