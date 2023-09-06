package com.example.yourlittlelibrary.models.author;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Nationality {

    UNITED_KINGDOM("\uD83C\uDDEC\uD83C\uDDE7"),
    GERMAN("\uD83C\uDDE9\uD83C\uDDEA"),
    AMERICAN("\uD83C\uDDFA\uD83C\uDDF8"),
    SWEDE("\uD83C\uDDF8\uD83C\uDDEA"),
    FRENCH("\uD83C\uDDEB\uD83C\uDDF7"),
    SPANISH("\uD83C\uDDEA\uD83C\uDDF8"),
    JAPANESE("\uD83C\uDDEF\uD83C\uDDF5"),
    DANISH("\uD83C\uDDE9\uD83C\uDDF0"),
    DUTCH("\uD83C\uDDF3\uD83C\uDDF1");

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
