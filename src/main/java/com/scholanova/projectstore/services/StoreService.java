package com.scholanova.projectstore.services;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.PriceNotValidException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.repositories.StoreRepository;
import com.scholanova.projectstore.resources.StoreResource;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StoreService {

    private StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Store create(Store store) throws StoreNameCannotBeEmptyException {

        if (isNameMissing(store)) {
            throw new StoreNameCannotBeEmptyException();
        }

        return storeRepository.create(store);
    }

    private boolean isNameMissing(Store store) {
        return store.getName() == null ||
                store.getName().trim().length() == 0;
    }
    
    
    public Store getById(Integer id) throws ModelNotFoundException {
    	
        return storeRepository.getById(id);
    }
    
    public void delete(Integer id) throws ModelNotFoundException {
    	
        storeRepository.delete(id);
    }
    
    public List<StoreResource> getStoresSuperiorPrice(Long price) throws PriceNotValidException{
    	if(price <=0) {
    		throw new PriceNotValidException();
    	}
    	
    	return storeRepository.getStoresSuperiorPrice(price);
    }
}
