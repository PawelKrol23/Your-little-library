package com.example.Spring6webapp.models.author;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Nationality {

    UNITED_KINGDOM("\uD83C\uDDEC\uD83C\uDDE7"),
    GERMAN("\uD83C\uDDE9\uD83C\uDDEA");

    private final String utfFlag;
}
