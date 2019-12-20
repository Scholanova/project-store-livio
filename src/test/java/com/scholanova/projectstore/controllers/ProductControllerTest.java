package com.scholanova.projectstore.controllers;


import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.models.Product;
import com.scholanova.projectstore.services.ProductService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductControllerTest {
	

    @LocalServerPort
    private int port;

    private TestRestTemplate template = new TestRestTemplate();

    @MockBean
    private ProductService productService;

    @Captor
    ArgumentCaptor<Product> createProductArgumentCaptor;

    @Captor
    ArgumentCaptor<String> productTypeArgumentCaptor;

    @AfterEach
    public void reset_mocks() {
        Mockito.reset(productService);
    }

    @Captor
    ArgumentCaptor<Integer> storeIdArgumentCaptor;

    @Captor
    ArgumentCaptor<Integer> productIdArgumentCaptor;
    
    @Nested
    class Test_createStock {

        @Test
        void givenCorrectBody_whenCalled_createsStock() throws Exception {
            // given
            String url = "http://localhost:{port}/stores/1/stocks";

            Map<String, String> urlVariables = new HashMap<>();
            urlVariables.put("port", String.valueOf(port));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String requestJson = "{" +
                    "\"name\":\"PS4\"," +
                    "\"type\":\"nail\"," +
                    "\"price\":100," +
                    "\"idStore\":1" +
                    "}";
            HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

            Product createdProduct = new Product(1, "PS4", "nail", 100, 1);
            //when(storeService.create(createStoreArgumentCaptor.capture())).thenReturn(createdStore);
            when(productService.create(createProductArgumentCaptor.capture())).thenReturn(createdProduct);
            // When
            ResponseEntity responseEntity = template.exchange(url,
                    HttpMethod.POST,
                    httpEntity,
                    String.class,
                    urlVariables);

            // Then
            assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
            assertThat(responseEntity.getBody()).isEqualTo(
                    "{" +
                            "\"id\":1," +
                            "\"name\":\"PS4\"," +
                            "\"type\":\"nail\"," +
                            "\"price\":100," +
                            "\"idStore\":1" +
                            "}"
            );
            Product productToCreate = createProductArgumentCaptor.getValue();
            System.out.println(productToCreate.toString());
            assertThat(productToCreate.getType()).isEqualTo("nail");
            assertThat(productToCreate.getName()).isEqualTo("PS4");
            assertThat(productToCreate.getidStore()).isEqualTo(1);
        }

    }
}
