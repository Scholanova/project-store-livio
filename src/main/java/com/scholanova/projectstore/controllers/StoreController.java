package com.scholanova.projectstore.controllers;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.services.StoreService;

import java.util.HashMap;

import javax.websocket.server.PathParam;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping(path = "/stores/{id}")
    public ResponseEntity<?> getStore(@PathVariable("id") Integer id)throws ModelNotFoundException {
        try {
        	return ResponseEntity.ok().body(storeService.getById(id));
		} catch (ModelNotFoundException e) {
			// TODO Auto-generated catch block
			HashMap<String, String> returnBody = new HashMap<String, String>();
        	returnBody.put("message", "Id must be valid");
        	return ResponseEntity.badRequest().body(returnBody);
		}
    }

    @PostMapping(path = "/stores")
    public ResponseEntity<?> createStore(@RequestBody Store store) throws StoreNameCannotBeEmptyException {
        try{
        	Store createdStore = storeService.create(store);
        	return ResponseEntity.ok()
        			.body(createdStore);
        }catch(StoreNameCannotBeEmptyException e) {
        	HashMap<String, String> returnBody = new HashMap<String, String>();
        	returnBody.put("message", "Name cannot be empty");
        	return ResponseEntity.badRequest().body(returnBody);
        }
    }
    
    @DeleteMapping(path = "/stores/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable("id") Integer id) {
        try {
        	storeService.delete(id);
        	return ResponseEntity.noContent().build();
		} catch (ModelNotFoundException e) {
			HashMap<String, String> returnBody = new HashMap<String, String>();
        	returnBody.put("message", "Id must be valid");
        	return ResponseEntity.badRequest().body(returnBody);
		}
    }
}
