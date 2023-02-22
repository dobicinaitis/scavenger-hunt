package dev.dobicinaitis.scavengerhunt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Code validation request")
public class ValidateCodeRequest {
    @Schema(example = "ABC123")
    private String code;
}
