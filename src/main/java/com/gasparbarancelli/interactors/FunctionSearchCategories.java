package com.gasparbarancelli.interactors;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.gasparbarancelli.entities.CategoryResponse;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@Description("Antes de conseguirmos as informações dos materiais de construções precisamos identificar a sua categoria")
public class FunctionSearchCategories implements Function<FunctionSearchCategories.Request, FunctionSearchCategories.Response> {

    private final ProductService productService;

    public FunctionSearchCategories(ProductService productService) {
        this.productService = productService;
    }

    @JsonClassDescription("Informações referentes as categorias de um material de construção, como quantidade, nome e seu identificador")
    public record Request(String product) {}
    public record Response(List<CategoryResponse.Category> categories) {}

    @Override
    public Response apply(Request request) {
        var product = request.product();
        return new Response(productService.fetchCategories(product));
    }

}