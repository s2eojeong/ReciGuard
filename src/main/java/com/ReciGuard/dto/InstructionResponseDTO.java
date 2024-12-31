package com.ReciGuard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InstructionResponseDTO {

    private String instructionImage; // 단계 번호
    private String instruction;

    public InstructionResponseDTO(String instructionImage, String instruction) {
        this.instructionImage = instructionImage;
        this.instruction = instruction;
    }
}
