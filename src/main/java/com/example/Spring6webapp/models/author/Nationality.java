package com.example.Spring6webapp.models.author;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Nationality {

    UNITED_KINGDOM("\uD83C\uDDEC\uD83C\uDDE7"),
    GERMAN("\uD83C\uDDE9\uD83C\uDDEA"),
    AMERICAN("\uD83C\uDDFA\uD83C\uDDF8");

    private final String utfFlag;

    public String capitalizedName() {
        char[] chars = this.name().toCharArray();
        for(int i = 1;i<chars.length;i++) {
            if(Character.isUpperCase(chars[i]) && chars[i-1] != ' ') {
                chars[i] = Character.toLowerCase(chars[i]);
            }

            if(chars[i] == '_') {
                chars[i] = ' ';
            }
        }

        return new String(chars);
    }
}
