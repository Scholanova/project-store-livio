package com.scholanova.projectstore.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.ProductNameCannotBeEmptyException;
import com.scholanova.projectstore.exceptions.ProductPriceNotValidException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Product;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.resources.StoreResource;
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
    		List<Product> list = ps.getProductsByType(storeid, "fruit");
    		if(list.size()>4) {
    			return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE).body("Magasin is full of fruit");
    		}
    		
    		product.setIdStore(storeid);
    		Product newProduct = ps.create(product);
    		return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request" + e);
    	}
    }
    
    @GetMapping(path="/stores/{store_id}/stocks/{id}")
    	public ResponseEntity<?> getProduct(@PathVariable("id") Integer id,@PathVariable("store_id") Integer idstore) throws Exception {
        try {
        	return ResponseEntity.ok().body(ps.getProduct(id,idstore));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			HashMap<String, String> returnBody = new HashMap<String, String>();
        	returnBody.put("message",""+ e);
        	return ResponseEntity.badRequest().body(returnBody);
		}
    }
    
    @PutMapping(path="/stores/{store_id}/stocks/{id}")
	public ResponseEntity<?> putProduct(@RequestBody Product product,@PathVariable("id") Integer id,@PathVariable("store_id") Integer idstore) throws Exception {
    try {
    	product.setId(id);
    	product.setIdStore(idstore);
    	return ResponseEntity.ok().body(ps.updateProduct(product));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		HashMap<String, String> returnBody = new HashMap<String, String>();
    	returnBody.put("message","Hello World"+ e);
    	return ResponseEntity.badRequest().body(returnBody);
		}
    }
    
    /*
    @DeleteMapping(path = "/stores/{store_id}/stocks/{id}")
    public ResponseEntity<?> deleteStation(@PathVariable("id") Integer id,@PathVariable("store_id") Integer idstore) {
    	try {
			productService.delete(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		} catch (ModelNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ModelNotFoundException");
		}
        
    }*/
    
    @GetMapping(path="/stores/{store_id}/stocks")
	public ResponseEntity<?> getProducts(@PathVariable("store_id") Integer idstore,@RequestParam(name = "type") Optional<String>  type) throws Exception {
    try {
    	   	
    	if(type.isPresent()) {
    		return ResponseEntity.ok().body(ps.getProductsByType(idstore,type.get()));
    	}
    	else {
    		return ResponseEntity.ok().body(ps.getProducts(idstore));
    	}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		HashMap<String, String> returnBody = new HashMap<String, String>();
    	returnBody.put("message : there is a problem ",""+ e);
    	return ResponseEntity.badRequest().body(returnBody);
		}
    }
    
    @GetMapping(path="/stores/{store_id}/stocks/sum")
	public ResponseEntity<?> getStoreSum(@PathVariable("store_id") Integer idstore) throws Exception {
    try {
    	
    	StoreResource store = new StoreResource();

    	store.setName(ps.getStoreById(idstore).getName());
    	store.setId(idstore);
    	store.setStockTotalValue((long) ps.getStoreSum(idstore));
    	
    	return ResponseEntity.ok().body(store);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		HashMap<String, String> returnBody = new HashMap<String, String>();
    	returnBody.put("message : problemes list product ",""+ e);
    	return ResponseEntity.badRequest().body(returnBody);
		}
    }
    
    //Pouvoir récuppérer la liste de tous les objets d'un magasin d'un certain type
    /*
    @GetMapping(path="/stores/{idstore}/stocks")
	public ResponseEntity<?> getProductsByType(@PathVariable("idstore") String idstore,@RequestParam(name = "type")String type) throws Exception {
    	System.out.println("type = " + type);
    try {
    	System.out.println("type = " + type);
    	return ResponseEntity.ok().body(ps.getProductsByType(Integer.valueOf(idstore),type));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		HashMap<String, String> returnBody = new HashMap<String, String>();
    	returnBody.put("message : problemes list product ",""+ e);
    	return ResponseEntity.badRequest().body(returnBody);
		}
    }*/
    
}
