package com.scholanova.projectstore.repositories;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.models.Product;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.resources.StoreResource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@SpringJUnitConfig(StoreRepository.class)
@JdbcTest
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "STORES");
    }

    @Nested
    class Test_getById {

        @Test
        void whenNoStoresWithThatId_thenThrowsException() throws Exception {
            // Given
            Integer id = 1000;

            // When & Then
            assertThrows(ModelNotFoundException.class, () -> {
                storeRepository.getById(id);
            });
        }

        @Test
        void whenStoreExists_thenReturnsTheStore() throws Exception {
            // Given
            Integer id = 1;
            Store store = new Store(id, "Carrefour");
            insertStore(store);

            // When
            Store extractedStore = storeRepository.getById(id);

            // Then
            assertThat(extractedStore).isEqualToComparingFieldByField(store);
        }
    }

    @Nested
    class Test_create {

        @Test
        void whenCreateStore_thenStoreIsInDatabaseWithId() {
            // Given
            String storeName = "Auchan";
            Store storeToCreate = new Store(null, storeName);

            // When
            Store createdStore = storeRepository.create(storeToCreate);

            // Then
            assertThat(createdStore.getId()).isNotNull();
            assertThat(createdStore.getName()).isEqualTo(storeName);
        }
    }
    
    
    @Nested
    class Test_delete {

        @Test
        void whenNoStoresWithThatId_thenThrowsException() throws Exception {
            // Given
            Integer id = 1;

            // When & Then
            assertThrows(ModelNotFoundException.class, () -> {
                storeRepository.delete(id);
            });
        }

        @Test
        void whenStoreExists_thenReturnsNothing() throws Exception {
            // Given
            Integer id = 1;
            Store store = new Store(id, "Carrefour");
            insertStore(store);

            // When
            storeRepository.delete(id);

            // Then
            assertThrows(ModelNotFoundException.class, () -> {
                storeRepository.getById(id);
            });
        }
    }
    
    @Nested 
    class Test_getStoresWithPrice{
    	@Test
    	void whenExistingStoresWithProductsThenReturnsStoreResource(){
    		
    		//Given
    		Store auchan = new Store(1,"Auchan");
    		Store cora = new Store(2,"Cora");
    		Store carrouf = new Store(3,"Carrouf");  		
    		insertStore(auchan);
    		insertStore(cora);
    		insertStore(carrouf);
    		
            Product mockedProduct = new Product(1, "Poire", "Fruit", 30, 1);
            insertProduct(mockedProduct);
            Product mockedProduct2 = new Product(2, "Pomme", "Fruit", 400, 2);
            insertProduct(mockedProduct2);
            Product mockedProduct3 = new Product(3, "Papaye", "Fruit", 500, 3);
            insertProduct(mockedProduct3);
            
            //When
            List<StoreResource> list = storeRepository.getStoresSuperiorPrice(100L);
            //Then
            assertEquals(2,list.size());
    		
    	}
    	@Test
    	void whenGetStoresSuperiorPriceThenReturnedTotalStockValueIsSuperiorToPrice(){
    		
    		//Given
    		
    		/*
    		Store auchan = new Store(1,"Auchan");
    		Store cora = new Store(2,"Cora");
    		Store carrouf = new Store(3,"Carrouf");  		
    		insertStore(auchan);
    		insertStore(cora);
    		insertStore(carrouf);
    		
            Product mockedProduct = new Product(1, "Poire", "Fruit", 30, 1);
            insertProduct(mockedProduct);
            Product mockedProduct2 = new Product(2, "Pomme", "Fruit", 400, 2);
            insertProduct(mockedProduct2);
            Product mockedProduct3 = new Product(3, "Papaye", "Fruit", 500, 3);
            insertProduct(mockedProduct3);
            */
    		
            long price = 100;
            //When
            List<StoreResource> list = storeRepository.getStoresSuperiorPrice(price);
            //Then
            
            for(StoreResource store : list) {
            	System.out.println(store);
            	assertTrue(store.getStockTotalValue()>= price);
            	
            }

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
}