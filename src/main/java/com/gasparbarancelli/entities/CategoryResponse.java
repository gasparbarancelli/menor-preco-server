package com.gasparbarancelli.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CategoryResponse(
    @JsonProperty("categorias") List<Category> categories
) {
    public record Category(
        String id,
        @JsonProperty("qtd") Integer qtd,
        @JsonProperty("desc") String name
    ) {}
}
