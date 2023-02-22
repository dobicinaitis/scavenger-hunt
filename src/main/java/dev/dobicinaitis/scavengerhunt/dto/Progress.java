package dev.dobicinaitis.scavengerhunt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Progress towards completion of the quest")
public class Progress {
    @Schema(example = "1/10")
    private String visual;
    @Schema(example = "0.10")
    private double numerical;
}
