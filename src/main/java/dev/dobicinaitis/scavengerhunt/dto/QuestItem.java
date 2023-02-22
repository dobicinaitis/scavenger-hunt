package dev.dobicinaitis.scavengerhunt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QuestItem {
    private Integer id;
    private String hint;
    private String code;
}
