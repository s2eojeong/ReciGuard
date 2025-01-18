package com.ReciGuard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class InstructionResponseDTO {

    private Integer instructionId;
    private String instructionImage; // 단계 번호
    private String instruction;

    @JsonCreator
    public InstructionResponseDTO(@JsonProperty("instructionId") Integer instructionId,
                                  @JsonProperty("instructionImage") String instructionImage,
                                  @JsonProperty("instruction") String instruction) {
        this.instructionId = instructionId;
        this.instructionImage = instructionImage;
        this.instruction = instruction;
    }
}
