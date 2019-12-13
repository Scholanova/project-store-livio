package com.scholanova.projectstore.models;

public class Product {
    private Integer id;
    private String name;
    private Integer quantity;
    private Integer idStore;
    
    public Product() {
    }

    public Product(Integer id, String name,Integer quantity,Integer idStore) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.idStore = idStore;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getQuantity() {
    	return this.quantity;
    }
    
    public void setQuantity(int quantity) {
    	this.quantity = quantity;
    }
    public Integer getidStore() {
    	return this.idStore;
    }
    public void setIdStore(int idStore) {
    	this.idStore = idStore;
    }
    
}
