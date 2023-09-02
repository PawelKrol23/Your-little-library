package com.example.Spring6webapp.models.author;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NationalityTest {

    @Test
    void capitalizedName_should_returnCapitalizedName() {
        // given
        final Nationality nationality = Nationality.UNITED_KINGDOM;
        final String expectedOutput = "United Kingdom";

        // when
        final String actualOutput = nationality.capitalizedName();

        // then
        assertThat(actualOutput).isNotNull();
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }
}