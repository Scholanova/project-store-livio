package com.scholanova.projectstore.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.scholanova.projectstore.exceptions.*;
import com.scholanova.projectstore.models.Product;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.repositories.ProductRepository;



@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productRepository);
	}
	
	@Test
	void givenNoProductName_whenCreated_failsThrowProductNameCannotBeEmptyException() {
		// GIVEN
		Integer id = 1;
		String name = null;
		String type = "fruit";
		Integer price = 70;
		Integer idStore = 1;

		Product emptyNameProduct = new Product(id, name,type,price,idStore);

		// WHEN
		assertThrows(ProductNameCannotBeEmptyException.class, () -> {
			productService.create(emptyNameProduct);
		});

		// THEN
		verify(productRepository, never()).create(emptyNameProduct);
	}
	@Test
	void givenUnvalidType_whenCreated_failsThrowsProductTypeNotValidException() {
		// GIVEN
		Integer id = 1;
		String name = "banane";
		String type = "Fruit";
		Integer price = 70;
		Integer idStore = 1;

		Product unvalidType = new Product(id, name,type,price,idStore);

		// WHEN
		assertThrows(ProductTypeNotValidException.class, () -> {
			productService.create(unvalidType);
		});

		// THEN
		verify(productRepository, never()).create(unvalidType);
	}
	
	@Test
	void givenWrongPrice_whenCreated_failsThrowsProductPriceNotValidException() {
		// GIVEN
		Integer id = 1;
		String name = "banane";
		String type = "fruit";
		Integer price = -1;
		Integer idStore = 1;

		Product wrongPrice = new Product(id, name,type,price,idStore);

		// WHEN
		assertThrows(ProductPriceNotValidException.class, () -> {
			productService.create(wrongPrice);
		});

		// THEN
		verify(productRepository, never()).create(wrongPrice);
	}
	
	@Test
	void givenCorrectProduct_whenCreated_savesProductInRepositoryInGivenStore() throws Exception {
		// GIVEN
		Integer id = 1;
		String name = "banane";
		String type = "fruit";
		Integer price = 90;
		Integer idStore = 2;

		Product correctProduct = new Product(null, name,type,price,idStore);
		Product savedProduct = new Product(id, name,type,price,idStore);
		when(productRepository.create(correctProduct)).thenReturn(savedProduct);

		// WHEN
		Product returnedStore = productService.create(correctProduct);

		// THEN
		verify(productRepository, atLeastOnce()).create(correctProduct);
		assertThat(returnedStore).isEqualTo(savedProduct);
	}
	
	@Test
	void givenCorrectId_whenDisplay_getProductFromRepository() throws Exception {
		// GIVEN
		Integer idProduct = 7;	
		
		Integer id = 7;
		String name = "banane";
		String type = "fruit";
		Integer price = 90;
		Integer idStore = 2;
		
		Product product = new Product(id, name,type,price,idStore);
		when(productRepository.getById(idProduct)).thenReturn(product);
		
		//WHEN
		Product returnedProduct = productService.getProduct(idProduct, idStore);
		
		//THEN
		verify(productRepository,atLeastOnce()).getById(idProduct);
		assertThat(returnedProduct).isEqualTo(product);

	}
	
	@Test
	void givenNotExistingId_whenDisplay_getProductNotFoundException() throws Exception {
		// GIVEN
		Integer idProduct = -1;
		Integer idStore = -1;
		when(productRepository.getById(idProduct)).thenThrow(new ProductNotFoundException());
		
		// WHEN
		// THEN
		assertThrows(ProductNotFoundException.class, () -> {
			productService.getProduct(idProduct,idStore);
		});
	}

}
