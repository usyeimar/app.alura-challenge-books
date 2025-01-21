package com.cariniana.appalurachallengebooks.dto;

import java.util.List;

public record BookResponse(
        int count,
        String next,
        String previous,
        List<BookDTO> results
) {
}
