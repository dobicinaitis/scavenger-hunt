package dev.dobicinaitis.scavengerhunt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Code validation response")
public class ValidateCodeResponse {
    @Schema(example = "The sweet is guarded by a vicious beast. Locate its lair.")
    private String message;
    private Progress progress;
    @JsonProperty(value = "isFinal")
    @Schema(defaultValue = "false")
    private boolean isFinal;
}
