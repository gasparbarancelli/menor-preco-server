package com.gasparbarancelli.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record ProductResponse(
    @JsonProperty("produtos") List<Product> products,
    @JsonProperty("total") int total,
    @JsonProperty("precos") ProductPrice price

) {
    public record ProductPrice(
            String min,
            String max
    ) {}

    public record Product(
        @JsonProperty("desc") String description,
        @JsonProperty("valor") String price,
        @JsonProperty("valor_desconto") String discount,
        @JsonProperty("estabelecimento") Company company
    ) {
        public record Company(
            @JsonPropertyDescription("Nome do estabelecimento em que o produto foi vendido") @JsonProperty("nm_fan") String name,
            @JsonProperty("tp_logr") String addressType,
            @JsonProperty("nm_logr") String addressName,
            @JsonPropertyDescription("Cidade em que o produto foi comercializado") @JsonProperty("mun") String city,
            String uf
        ) {}
    }
}
