package com.scholanova.projectstore.controllers;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.services.StoreService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StoreControllerTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate template = new TestRestTemplate();

    @MockBean
    private StoreService storeService;

    @Captor
    ArgumentCaptor<Store> createStoreArgumentCaptor;
    @Captor
    ArgumentCaptor<Integer> idArgumentCaptor;
    @Nested
    class Test_createStore {

        @Test
        void givenCorrectBody_whenCalled_createsStore() throws Exception {
            // given
            String url = "http://localhost:{port}/stores";

            Map<String, String> urlVariables = new HashMap<>();
            urlVariables.put("port", String.valueOf(port));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestJson = "{" +
                    "\"name\":\"Boulangerie\"" +
                    "}";
            HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);

            Store createdStore = new Store(123, "Boulangerie");
            when(storeService.create(createStoreArgumentCaptor.capture())).thenReturn(createdStore);

            // When
            ResponseEntity responseEntity = template.exchange(url,
                    HttpMethod.POST,
                    httpEntity,
                    String.class,
                    urlVariables);

            // Then
            assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
            assertThat(responseEntity.getBody()).isEqualTo(
                    "{" +
                            "\"id\":123," +
                            "\"name\":\"Boulangerie\"" +
                            "}"
            );
            Store storeToCreate = createStoreArgumentCaptor.getValue();
            assertThat(storeToCreate.getName()).isEqualTo("Boulangerie");
        }
        
        @Test
        void givenIncorrectBody_whenCalled_error() throws Exception {
            // given
            String url = "http://localhost:{port}/stores";

            Map<String, String> urlVariables = new HashMap<>();
            urlVariables.put("port", String.valueOf(port));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestJson = "{" +
                    "\"name\":\"\"" +
                    "}";
            HttpEntity<String> httpEntity = new HttpEntity<>(requestJson, headers);
            
            Store createdStore = new Store(123, "");
            when(storeService.create(createStoreArgumentCaptor.capture())).thenThrow(new StoreNameCannotBeEmptyException());
            // When
            ResponseEntity responseEntity = template.exchange(url,
                    HttpMethod.POST,
                    httpEntity,
                    String.class,
                    urlVariables);

            // Then
            String errorBody = "{" +
                    "\"message\":\"Name cannot be empty\"" +
                    "}";
            
            assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
            assertThat(responseEntity.getBody()).isEqualTo(errorBody);
            Store storeToCreate = createStoreArgumentCaptor.getValue();
            assertThat(storeToCreate.getName()).isEqualTo("");
        }
    }
    

    	@Test
        void Test_getStore_givenValidId_thenReturnStoreById() throws Exception {
            // given
            String url = "http://localhost:{port}/stores/{id}";
            
            Integer id = 1;
            
            Map<String, String> urlVariables = new HashMap<>();
            urlVariables.put("port", String.valueOf(port));
            urlVariables.put("id", String.valueOf(id));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
 
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            
            Store getStore = new Store(1,"Boulangerie");
            when(storeService.getById(idArgumentCaptor.capture())).thenReturn(getStore);

            // When
            ResponseEntity responseEntity = template.exchange(url,
                    HttpMethod.GET,
                    httpEntity,
                    String.class,
                    urlVariables);

            // Then
            assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
            assertThat(responseEntity.getBody()).isEqualTo(
                    "{" +
                            "\"id\":1," +
                            "\"name\":\"Boulangerie\"" +
                            "}"
            );
            Integer storeIdToGet = idArgumentCaptor.getValue();
            assertThat(storeIdToGet).isEqualTo(1);
        }
    	@Test
        void Test_getStore_givenInvalidId_thenReturnStoreById() throws Exception {
    		// given
            String url = "http://localhost:{port}/stores/{id}";

            Map<String, String> urlVariables = new HashMap<>();
            urlVariables.put("port", String.valueOf(port));
            urlVariables.put("id", String.valueOf(port));

            Integer id = 1;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            
            Store createdStore = new Store(id, "Boulangerie");
            when(storeService.getById(idArgumentCaptor.capture())).thenThrow(new ModelNotFoundException());
            // When
            ResponseEntity responseEntity = template.exchange(url,
                    HttpMethod.GET,
                    httpEntity,
                    String.class,
                    urlVariables);

            // Then
            String errorBody = "{" +
                    "\"message\":\"Id must be valid\"" +
                    "}";
            
            assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
            assertThat(responseEntity.getBody()).isEqualTo(errorBody);
        }
    	
    	
    	
    	
    	@Test
        void Test_delete_Store_givenValidId_thenReturn204() throws Exception {
            // given
            String url = "http://localhost:{port}/stores/{id}";
            
            Integer id = 1;
            
            Map<String, String> urlVariables = new HashMap<>();
            urlVariables.put("port", String.valueOf(port));
            urlVariables.put("id", String.valueOf(id));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
 
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            
            
            doNothing().when(storeService).delete(id);
            
            // When
            ResponseEntity responseEntity = template.exchange(url,
                    HttpMethod.DELETE,
                    httpEntity,
                    String.class,
                    urlVariables);

            // Then
            assertThat(responseEntity.getStatusCode()).isEqualTo(NO_CONTENT);
          
        }
//    	@Test
//        void Test_getStore_givenInvalidId_thenReturnStoreById() throws Exception {
//    		// given
//            String url = "http://localhost:{port}/stores/{id}";
//
//            Map<String, String> urlVariables = new HashMap<>();
//            urlVariables.put("port", String.valueOf(port));
//            urlVariables.put("id", String.valueOf(port));
//
//            Integer id = 1;
//            
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
//            
//            Store createdStore = new Store(id, "Boulangerie");
//            when(storeService.getById(idArgumentCaptor.capture())).thenThrow(new ModelNotFoundException());
//            // When
//            ResponseEntity responseEntity = template.exchange(url,
//                    HttpMethod.GET,
//                    httpEntity,
//                    String.class,
//                    urlVariables);
//
//            // Then
//            String errorBody = "{" +
//                    "\"message\":\"Id must be valid\"" +
//                    "}";
//            
//            assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
//            assertThat(responseEntity.getBody()).isEqualTo(errorBody);
//        }
}