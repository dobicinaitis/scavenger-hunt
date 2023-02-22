package dev.dobicinaitis.scavengerhunt.config;

import dev.dobicinaitis.scavengerhunt.dto.QuestItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableConfigurationProperties
@EnableAutoConfiguration
@PropertySource(value = "classpath:quest.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "static")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestConfiguration {
    private String title;
    private String intro;
    private String outro;
    private String inputPlaceholder;
    private String submitButtonText;
    private String codeIsCorrectMessage;
    private String codeIsWrongMessage;
    private List<QuestItem> questItems;
}
