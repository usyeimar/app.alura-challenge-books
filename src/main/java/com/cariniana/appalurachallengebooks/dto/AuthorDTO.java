package com.cariniana.appalurachallengebooks.dto;

import com.google.gson.annotations.SerializedName;

public record AuthorDTO(
        String name,
        @SerializedName("birth_year") int birthYear,
        @SerializedName("death_year") int deathYear
) {
}
