package dev.dobicinaitis.scavengerhunt.services;

import dev.dobicinaitis.scavengerhunt.config.QuestConfiguration;
import dev.dobicinaitis.scavengerhunt.dto.Progress;
import dev.dobicinaitis.scavengerhunt.dto.QuestInfo;
import dev.dobicinaitis.scavengerhunt.dto.QuestItem;
import dev.dobicinaitis.scavengerhunt.dto.ValidateCodeRequest;
import dev.dobicinaitis.scavengerhunt.dto.ValidateCodeResponse;
import dev.dobicinaitis.scavengerhunt.exceptions.InvalidCodeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Getter
@Slf4j
public class QuestService {

    private QuestConfiguration quest;

    /**
     * Returns basic information about the quest for initialization of HTML pages.
     *
     * @return information for populating HTML pages
     */
    public QuestInfo getInfo() {
        return QuestInfo.builder()
                .title(quest.getTitle())
                .intro(quest.getIntro())
                .inputPlaceholder(quest.getInputPlaceholder())
                .submitButtonText(quest.getSubmitButtonText())
                .codeIsCorrectMessage(quest.getCodeIsCorrectMessage())
                .codeIsWrongMessage(quest.getCodeIsWrongMessage())
                .build();
    }

    /**
     * Validates the submitted code and returns the next hint if code is correct.
     *
     * @param request the submitted code
     * @return response with the next hint or the final message
     */
    public ValidateCodeResponse validateCode(final ValidateCodeRequest request) {
        log.info("Validating code: {}", request.getCode());
        final QuestItem questItem = getQuestItemByCode(request.getCode());
        final int questItemNo = getQuestItemNo(questItem);
        log.info("Correct code was supplied for quest No: {}", questItemNo);
        final int totalItemCount = quest.getQuestItems().size();

        if (questItemNo == totalItemCount) {
            log.info("Quest completed!");
            return ValidateCodeResponse.builder()
                    .message(quest.getOutro())
                    .progress(Progress.builder()
                            .numerical(1F)
                            .visual(totalItemCount + "/" + totalItemCount)
                            .build())
                    .isFinal(true)
                    .build();
        }

        final QuestItem nextQuestItem = quest.getQuestItems().get(questItemNo); // No 1 = Index 0
        return ValidateCodeResponse.builder()
                .message(nextQuestItem.getHint())
                .progress(Progress.builder()
                        .numerical((double) questItemNo / (double) totalItemCount) // 0.1
                        .visual(questItemNo + "/" + totalItemCount) // 1/10
                        .build())
                .build();
    }

    /**
     * Finds quest item by code (case-insensitive).
     * Limitation: if there are multiple quest items with the same code, the first one will be returned.
     *
     * @param code of a quest item
     * @return the quest item matching the code
     * @throws InvalidCodeException if the code is not found
     */
    private QuestItem getQuestItemByCode(final String code) {
        return quest.getQuestItems().stream()
                .filter(item -> item.getCode().equalsIgnoreCase(code.trim()))
                .findFirst()
                .orElseThrow(() -> new InvalidCodeException(quest.getCodeIsWrongMessage()));
    }

    /**
     * Returns the quest item number.
     *
     * @param questItem the quest item
     * @return the quest item number
     */
    private int getQuestItemNo(final QuestItem questItem) {
        return quest.getQuestItems().indexOf(questItem) + 1;
    }

    /**
     * Generates a number of random alphanumeric codes that can be used to configure quest.yml file.
     *
     * @param count  the number of codes to generate
     * @param length the length of each code
     * @return a new line separated list of codes
     */
    public String generateCodes(final int count, final int length) {
        final RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .build();
        final StringBuilder codes = new StringBuilder();
        for (int i = 0; i < count; i++) {
            final String code = generator.generate(length).toUpperCase();
            codes.append(code);
            codes.append("\n");
        }
        return codes.toString();
    }
}
