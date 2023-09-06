package com.example.yourlittlelibrary.models.book;

public enum Genre {
    ROMANCE,
    FANTASY,
    THRILLER,
    MYSTERY,
    HORROR,
    FICTION,
    COMEDY,
    NOVEL;

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
