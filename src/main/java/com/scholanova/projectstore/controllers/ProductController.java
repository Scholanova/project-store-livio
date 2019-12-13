package com.scholanova.projectstore.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scholanova.projectstore.exceptions.ProductNameCannotBeEmptyException;
import com.scholanova.projectstore.exceptions.ProductPriceNotValidException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Product;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.services.ProductService;

@RestController
public class ProductController {
	private final ProductService ps;
	
	public ProductController(ProductService p) {
		this.ps = p;
	}
	
    @PostMapping(path = "/stores/{store_id}/stocks")
    public ResponseEntity<?> createProduct(@RequestBody Product product, @PathVariable("store_id") Integer storeid) throws Exception {
    	try {
    		product.setIdStore(storeid);
    		Product newProduct = ps.create(product);
    		return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
    	}
    }
	
}
