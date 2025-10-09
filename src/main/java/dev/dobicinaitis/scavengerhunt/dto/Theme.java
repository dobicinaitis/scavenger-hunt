package dev.dobicinaitis.scavengerhunt.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Theme {
    DEFAULT("default"),
    HALLOWEEN("halloween");

    private final String name;

    public static Theme fromString(final String name) {
        for (Theme theme : Theme.values()) {
            if (theme.name().equalsIgnoreCase(name)) {
                return theme;
            }
        }
        return DEFAULT;
    }
}
