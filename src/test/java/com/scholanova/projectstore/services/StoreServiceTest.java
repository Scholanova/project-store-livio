package com.scholanova.projectstore.services;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.repositories.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

	private StoreService storeService;

	@Mock
	private StoreRepository storeRepository;

	@BeforeEach
	void setUp() {
		storeService = new StoreService(storeRepository);
	}

	@Test
	void givenNoStoreName_whenCreated_failsWithNoEmptyStoreNameError() {
		// GIVEN
		Integer id = null;
		String name = null;

		Store emptyNameStore = new Store(id, name);

		// WHEN
		assertThrows(StoreNameCannotBeEmptyException.class, () -> {
			storeService.create(emptyNameStore);
		});

		// THEN
		verify(storeRepository, never()).create(emptyNameStore);
	}

	@Test
	void givenCorrectStore_whenCreated_savesStoreInRepository() throws Exception {
		// GIVEN
		Integer id = 1234;
		String name = "BHV";

		Store correctStore = new Store(null, name);
		Store savedStore = new Store(id, name);
		when(storeRepository.create(correctStore)).thenReturn(savedStore);

		// WHEN
		Store returnedStore = storeService.create(correctStore);

		// THEN
		verify(storeRepository, atLeastOnce()).create(correctStore);
		assertThat(returnedStore).isEqualTo(savedStore);
	}


	@Test
	void givenCorrectId_whenDisplay_getStoreFromRepository() throws Exception {
		// GIVEN
		Integer id1 = 2;	
		Store storeA = new Store(2,"nomauchoix");
		when(storeRepository.getById(id1)).thenReturn(storeA);
		
		//WHEN
		Store returnedStore = storeService.getById(id1);
		
		//THEN
		verify(storeRepository,atLeastOnce()).getById(id1);
		assertThat(returnedStore).isEqualTo(storeA);

	}
	@Test
	void givenNotExistingId_whenDisplay_getModelNotFoundException() throws Exception {
		// GIVEN
		Integer id1 = 1;
		when(storeRepository.getById(id1)).thenThrow(new ModelNotFoundException());
		
		// WHEN
		// THEN
		assertThrows(ModelNotFoundException.class, () -> {
			storeService.getById(id1);
		});
	}
	
	
	
	@Test
	void givenCorrectId_whenDelete_doNothing() throws Exception {
		// GIVEN
		Integer id1 = 2;	
		Store storeA = new Store(2,"nomauchoix");
		doNothing().when(storeRepository).delete(id1);
		
		//WHEN
		storeService.delete(id1);
		
		//THEN
		verify(storeRepository,atLeastOnce()).delete(id1);

	}
	@Test
	void givenNotExistingId_whenDelete_getModelNotFoundException() throws Exception {
		// GIVEN
		Integer id1 = 1;
		doThrow(new ModelNotFoundException()).when(storeRepository).delete(id1);
		
		// WHEN
		// THEN
		assertThrows(ModelNotFoundException.class, () -> {
			storeService.delete(id1);
		});
		
		verify(storeRepository,atLeastOnce()).delete(id1);
	}
}