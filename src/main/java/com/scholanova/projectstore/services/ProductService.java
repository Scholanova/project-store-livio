package com.scholanova.projectstore.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.ProductNameCannotBeEmptyException;
import com.scholanova.projectstore.exceptions.ProductNotFoundException;
import com.scholanova.projectstore.exceptions.ProductNotInStoreException;
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
        this.storeRepository = storeRepository;
    }
    
    public Store getStoreById(Integer id) throws ModelNotFoundException {
    	
        return storeRepository.getById(id);
    }
        
    public Product create(Product product) throws ProductNameCannotBeEmptyException,ProductTypeNotValidException, ProductPriceNotValidException{
    	
        if (isNameMissing(product)) {
            throw new ProductNameCannotBeEmptyException();
        }
        if(!isTypeValid(product)) {
        	throw new ProductTypeNotValidException();
        }
        if(isPriceNotValid(product)) {
        	throw new ProductPriceNotValidException();
        }
    	return productRepository.create(product);
    }
    
    private boolean isNameMissing(Product product) {
        return product.getName() == null ||
        		product.getName().trim().length() == 0;
    }
    
    private boolean isTypeValid(Product product) {
    	return product.getType().equals("fruit") || product.getType().equals("nail");
    }
    
    private boolean isPriceNotValid(Product product) {
    	return product.getPrice() < 1;
    }

    public Product getProduct(Integer id,Integer storeId) throws ProductNotInStoreException, ProductNotFoundException {
    	Product p = productRepository.getById(id);
    	if(!isProductInStore(p,storeId)) {
    		throw new ProductNotInStoreException("id is "+p.getId() +" and product store id is " + p.getidStore() + " but call was made from storeId " + storeId);
    	}
        return p;
    }
    
    private boolean isProductInStore(Product p,Integer storeId) {
    	return p.getidStore() == storeId;
    }

	public Product updateProduct(Product product) throws ProductNotFoundException, ProductNotInStoreException, ProductNameCannotBeEmptyException, ProductTypeNotValidException, ProductPriceNotValidException {

        if (isNameMissing(product)) {
            throw new ProductNameCannotBeEmptyException();
        }
        if(isPriceNotValid(product)) {
        	throw new ProductPriceNotValidException();
        }
        //supposément lance product not in store si produit pas dans le store
		Product p = getProduct(product.getId(),product.getidStore());
		
		int rows = productRepository.update(product);
		if ( rows >  0 ) {
			return productRepository.getById(product.getId()); 
		} else {
			return null;
		}
	}

	public List<Product> getProducts(Integer idstore) {
		// TODO Auto-generated method stub
		return productRepository.getProducts(idstore);
	}
	
	public int getStoreSum(Integer idstore) {
		return productRepository.getStoreSum(idstore);
	}

}
