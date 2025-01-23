package com.gasparbarancelli.interactors;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.gasparbarancelli.entities.Location;
import com.gasparbarancelli.entities.ProductResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@Description("Recupera as notas fiscais de produtos comercializados no dia atual, podendo identificar qual foi o preco mais alto ou baixo pago por um produto")
public class FunctionSearchProducts implements Function<FunctionSearchProducts.Request, FunctionSearchProducts.Response> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionSearchProducts.class);

    private final ProductService productService;

    public FunctionSearchProducts(ProductService productService) {
        this.productService = productService;
    }

    @JsonClassDescription("Informações referentes ao material de construção, como descrição, preço e estabelecimento em qual o material foi comercializado")
    public record Request(String product, String categoryId) {}
    public record Response(List<ProductResponse.Product> products) {}

    @Override
    public Response apply(Request request) {
        var product = request.product();
        var categoryId = request.categoryId();

        List<String> locationCodes = Location.getCodes();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<List<ProductResponse.Product>>> futures = locationCodes.stream()
                    .map(location -> executor.submit(() -> productService.searchProductsInLocation(location, product, categoryId)))
                    .toList();

            return new Response(futures.stream()
                    .flatMap(future -> {
                        try {
                            return future.get(30, TimeUnit.SECONDS).stream();
                        } catch (Exception e) {
                            LOGGER.info("Erro ou timeout para a localização: " + e.getMessage());
                            return Stream.empty();
                        }
                    })
                    .sorted(Comparator.comparing(ProductResponse.Product::price))
                    .toList());
        }
    }

}