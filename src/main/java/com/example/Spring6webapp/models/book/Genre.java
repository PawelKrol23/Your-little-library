package com.example.Spring6webapp.models.book;

public enum Genre {
    ROMANCE;

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
