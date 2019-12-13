package com.scholanova.projectstore.services;

import com.scholanova.projectstore.models.Product;
import com.scholanova.projectstore.repositories.ProductRepository;


public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    /*
    public Product create(Product product) {
    	
    }
    */
    private boolean isNameMissing(Product product) {
        return product.getName() == null ||
        		product.getName().trim().length() == 0;
    }
}
