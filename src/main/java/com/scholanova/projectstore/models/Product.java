package com.scholanova.projectstore.models;

public class Product {
    private Integer id;
    private String name;
    private String type;
    private Integer price;
    private Integer idStore;
    
    public Product() {
    }

    public Product(Integer id, String name,String type,Integer price,Integer idStore) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.idStore = idStore;
    }
    
    public Product(Product p) {
        this.id = p.getId();
        this.name = p.getName();
        this.type = p.getType();
        this.price = p.getPrice();
        this.idStore = p.getidStore();
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
    
    public String getType() {
    	return type;
    }
    
    public void setType(String type) {
    	this.type = type;
    }
    
    public Integer getPrice() {
    	return this.price;
    }
    
    public void setPrice(int price) {
    	this.price = price;
    }
    public Integer getidStore() {
    	return this.idStore;
    }
    public void setIdStore(int idStore) {
    	this.idStore = idStore;
    }
    
}
