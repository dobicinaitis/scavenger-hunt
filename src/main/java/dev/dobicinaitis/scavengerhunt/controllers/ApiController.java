package dev.dobicinaitis.scavengerhunt.controllers;

import dev.dobicinaitis.scavengerhunt.dto.ValidateCodeRequest;
import dev.dobicinaitis.scavengerhunt.dto.ValidateCodeResponse;
import dev.dobicinaitis.scavengerhunt.services.QuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private QuestService service;

    @PostMapping(value = "/code/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Validates the submitted code and returns the next hint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Code validation successful", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ValidateCodeResponse.class))
            })
    })
    public ValidateCodeResponse validate(@Parameter(description = "Code to validate") @RequestBody ValidateCodeRequest code) {
        log.info("POST /api/code/validate was called, code = {}", code);
        ValidateCodeResponse response = service.validateCode(code);
        log.info("Sending response: {}", response);
        return response;
    }

    @GetMapping(value = "/code/generate", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Generates alphanumeric codes that can be used for quest setup")
    public String generateCodes(@RequestParam(defaultValue = "10") @Min(1) @Max(1000) int count,
                                @RequestParam(defaultValue = "5") @Min(1) @Max(100) int length) {
        log.info("GET /api/code/generate/{} was called", count);
        return service.generateCodes(count, length);
    }
}
