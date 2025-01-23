package com.gasparbarancelli.interactors;

import com.gasparbarancelli.entities.CategoryResponse;
import com.gasparbarancelli.entities.Location;
import com.gasparbarancelli.entities.ProductResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private static final String CATEGORY_API_URL = "https://menorpreco.notaparana.pr.gov.br/api/v1/categorias";
    private static final String PRODUCT_API_URL = "https://menorpreco.notaparana.pr.gov.br/api/v1/produtos";
    private static final int BATCH_SIZE = 50;

    private final RestClient restClient;

    public ProductService() {
        this.restClient = RestClient.builder().build();
    }

    public List<CategoryResponse.Category> fetchCategories(String product) {
        try {
            String url = String.format("%s?local=%s&termo=%s&raio=2",
                    CATEGORY_API_URL, Location.PATO_BRANCO.getCode(), product);

            CategoryResponse response = restClient.method(HttpMethod.GET)
                    .uri(url)
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .retrieve()
                    .body(CategoryResponse.class);

            return response.categories();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching categories for product: " + product, e);
        }
    }

    public List<ProductResponse.Product> searchProductsInLocation(String location, String product, String categoryId) {
        List<ProductResponse.Product> allProducts = new ArrayList<>();
        int offset = 0;

        while (true) {
            try {
                String url = String.format("%s?local=%s&termo=%s&categoria=%s&offset=%d&raio=2&data=-1&ordem=0",
                        PRODUCT_API_URL, location, product, categoryId, offset);

                ProductResponse response = restClient.method(HttpMethod.GET)
                        .uri(url)
                        .header(HttpHeaders.ACCEPT, "application/json")
                        .retrieve()
                        .body(ProductResponse.class);

                if (response.products() != null && !response.products().isEmpty()) {
                    allProducts.addAll(response.products());
                }

                if (response.products() == null || response.products().size() < BATCH_SIZE) {
                    break;
                }

                offset += BATCH_SIZE;
            } catch (Exception e) {
                throw new RuntimeException("Error fetching products for category: " + categoryId, e);
            }
        }
        return allProducts;
    }

}
