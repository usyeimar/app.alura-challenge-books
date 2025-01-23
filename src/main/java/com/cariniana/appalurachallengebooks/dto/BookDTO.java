package com.cariniana.appalurachallengebooks.dto;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public record BookDTO(
        int id,
        String title,
        List<AuthorDTO> authors,
        List<TranslatorDTO> translators,
        List<String> subjects,
        List<String> bookshelves,
        List<String> languages,
        boolean copyright,
        String mediaType,
        Map<String, String> formats,
        @SerializedName("download_count") int downloadCount
) {
}
