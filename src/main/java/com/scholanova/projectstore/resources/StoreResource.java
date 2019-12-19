package com.scholanova.projectstore.resources;

public class StoreResource {
	
	private String name;
	private Integer id;
	private Long stockTotalValue;
	
	public StoreResource() {
		
	}
	
	public StoreResource(String name, Integer id, Long stockTotalValue) {
		super();
		this.name = name;
		this.id = id;
		this.stockTotalValue = stockTotalValue;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getStockTotalValue() {
		return stockTotalValue;
	}
	public void setStockTotalValue(Long stockTotalValue) {
		this.stockTotalValue = stockTotalValue;
	}
	@Override
	public String toString() {
		return "StoreResource [name=" + name + ", id=" + id + ", stockTotalValue=" + stockTotalValue + "]";
	}
	
	
	
}
