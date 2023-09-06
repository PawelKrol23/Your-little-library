package com.example.yourlittlelibrary.models.book;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenreTest {

    @Test
    void capitalizedName_should_returnCapitalizedName() {
        // given
        final Genre genre = Genre.FANTASY;
        final String expectedOutput = "Fantasy";

        // when
        final String actualOutput = genre.capitalizedName();

        // then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }
}