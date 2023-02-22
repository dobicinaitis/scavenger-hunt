package dev.dobicinaitis.scavengerhunt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestInfo {
    private String title;
    private String intro;
    private String inputPlaceholder;
    private String submitButtonText;
    private String codeIsCorrectMessage;
    private String codeIsWrongMessage;
}
