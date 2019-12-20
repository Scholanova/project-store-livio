package com.scholanova.projectstore.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.ProductNotFoundException;
import com.scholanova.projectstore.models.Product;
import com.scholanova.projectstore.models.Store;

@SpringJUnitConfig(ProductRepository.class)
@JdbcTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "PRODUCT");
    }
    
    
    
    @Nested
    class Test_getProduct {

        @Test
        void whenNoProductWithThatId_thenThrowsException() throws Exception {
            // Given
            Integer id = 1000;

            // When & Then
            assertThrows(ProductNotFoundException.class, () -> {
            	productRepository.getById(id);
            });
        }

        @Test
        void whenProductExists_thenReturnsProduct() throws Exception {
            // Given
            Integer id = 1;
            Integer idStore = 1;
            Product product = new Product(id, "poire","fruit",60,idStore);
            insertProduct(product);

            // When
            Product extractedProduct = productRepository.getById(id);

            // Then
            assertThat(extractedProduct).isEqualToComparingFieldByField(product);
        }
    }
    
    @Nested
    class Test_createProduct {

        @Test
        void givenProductWithValues_whenCreateProductInDatabases_thenProductIsInDatabaseWithAllInformation() {
            // Given
            Product product = new Product(null, "poire","fruit",60,2);
            // When
            Product createdProduct = productRepository.create(product);

            // Then
            assertThat(createdProduct).isNotNull();
            assertThat(createdProduct.getId()).isNotNull();
            assertThat(createdProduct.getName()).isEqualTo(product.getName());
            assertThat(createdProduct.getType()).isEqualTo(product.getType());
            assertThat(createdProduct.getPrice()).isEqualTo(product.getPrice());
            assertThat(createdProduct.getidStore()).isEqualTo(product.getidStore());
        }
    }
    
    @Nested
    class Test_updateProduct {
    	
        @Test
        void whenUpdateProduct_thenDatabaseSendNumberofRowsUpdated() throws Exception {
            // Given
            String newName = "poire jaune";
            Integer newPrice = 200;            
            // When
            int rows = productRepository.update(new Product(1,newName,"fruit",newPrice,2));
            // Then
            assertThat(rows).isNotNegative();
            assertThat(rows).isNotNull();

        }
    	//On Cree Produit
        //On met a jour le produit en base, on recupère UpdatedProduct
        //Product est donc diifférent de UpdatedProduct
        //Mais j'ai une erreur DAO duplicate Key Exception
        @Test
        void givenAProductWhenUpdateProductWithNewValueThenProductisDifferentThanUpdatedProduct() throws Exception {
            // Given
            Product product2 = new Product(3, "pomme","fruit",90,2);
            insertProduct(product2);
            
            String newName = "pomme braeburn";
            Integer newPrice = 200;
            
            
            // When
            productRepository.update(new Product(3,newName,"fruit",newPrice,2));
            Product updatedProduct = productRepository.getById(3);
            updateProduct(updatedProduct);
            // Then
            
            assertThat(product2.getName()).isNotEqualTo(updatedProduct.getName());

        }
    }
    
    @Nested
    class Test_getStoreTotalValue {

        @Test
        void whenGivenExistingStoreWithStock_thenReturnStockTotalValue() throws ModelNotFoundException {
            // Given
            int mockedStoreId = 5;
            Store mockedStore = new Store(mockedStoreId, "Auchan");
            insertStore(mockedStore);
            Product mockedProduct = new Product(1, "Poire", "Fruit", 30, 5);
            insertProduct(mockedProduct);
            Product mockedProduct2 = new Product(2, "Pomme", "Fruit", 30, 5);
            insertProduct(mockedProduct2);

            // When
            int totalStoreValue = productRepository.getStoreSum(mockedStoreId);

            // Then
            assertThat(totalStoreValue).isEqualTo(60);
        }

        @Test
        void whenGivenExistingStoreWithoutStock_thenReturnStockTotalValue() throws ModelNotFoundException {
            // Given
            int mockedStoreId = 10;
            Store mockedStore = new Store(mockedStoreId, "Auchan");
            insertStore(mockedStore);

            // When
            Integer totalStoreValue = productRepository.getStoreSum(mockedStoreId);

            // Then
            assertThat(totalStoreValue).isEqualTo(0);
        }
    }
    
    
    private void insertStore(Store store) {
        String query = "INSERT INTO STORES " +
                "(ID, NAME) " +
                "VALUES ('%d', '%s')";
        jdbcTemplate.execute(
                String.format(query, store.getId(), store.getName()));
    }
    
    private void insertProduct(Product p) {
		String query = "INSERT INTO PRODUCT (ID,NAME,TYPE,PRICE,IDSTORE) VALUES ('%d','%s','%s','%d','%d') ";

        jdbcTemplate.execute(
                String.format(query, p.getId(), p.getName(),p.getType(),p.getPrice(),p.getidStore()));
    }
    
    private void updateProduct(Product p) {
    	String query = "UPDATE PRODUCT SET NAME = '%s', PRICE = '%d' WHERE ID = '%d'";
    	jdbcTemplate.execute(
                String.format(query,p.getName(),p.getPrice(),p.getId()));
    }
}
