package com.scholanova.projectstore.services;

import org.springframework.stereotype.Service;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.ProductNameCannotBeEmptyException;
import com.scholanova.projectstore.exceptions.ProductPriceNotValidException;
import com.scholanova.projectstore.exceptions.ProductTypeNotValidException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Product;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.repositories.ProductRepository;
import com.scholanova.projectstore.repositories.StoreRepository;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private StoreRepository storeRepository;
    
    public ProductService(ProductRepository productRepository,StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.storeRepository  = storeRepository;
    }
    
    public Product create(Product product) throws ProductNameCannotBeEmptyException,ProductTypeNotValidException, ProductPriceNotValidException{
    	/*
        if (isNameMissing(product)) {
            throw new ProductNameCannotBeEmptyException();
        }
        if(!isTypeValid(product)) {
        	throw new ProductTypeNotValidException();
        }
        if(isPriceNotValid(product)) {
        	throw new ProductPriceNotValidException();
        }*/
    	return productRepository.create(product);
    }
    
    private boolean isNameMissing(Product product) {
        return product.getName() == null ||
        		product.getName().trim().length() == 0;
    }
    
    private boolean isTypeValid(Product product) {
    	return product.getType().equals("Fruit") || product.getType().equals("Nail");
    }
    
    private boolean isPriceNotValid(Product product) {
    	return product.getPrice() < 1;
    }
    
    public Store getById(Integer id) throws ModelNotFoundException {
    	
        return storeRepository.getById(id);
    }
}
