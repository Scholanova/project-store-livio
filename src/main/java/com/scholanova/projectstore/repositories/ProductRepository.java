package com.scholanova.projectstore.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.ProductNotFoundException;
import com.scholanova.projectstore.models.Product;
import com.scholanova.projectstore.models.Store;

@Repository
public class ProductRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;

	public ProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Product getById(Integer id) throws ProductNotFoundException {
		String query = "SELECT ID as id, " +
				"NAME as name, " +
				"TYPE as type, " +
				"PRICE as price, "+
				"IDSTORE as idstore "+ 
				"FROM PRODUCT " +
				"WHERE ID = :id";

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("id", id);

		return jdbcTemplate.query(query,
				parameters,
				new BeanPropertyRowMapper<>(Product.class))
				.stream()
				.findFirst()
				.orElseThrow(ProductNotFoundException::new);
	}
	
	public Product create(Product product) {
		KeyHolder holder = new GeneratedKeyHolder();

		String query = "INSERT INTO PRODUCT (NAME,TYPE,PRICE,IDSTORE) VALUES (:name, :type, :price, :idstore) ";
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("name", product.getName()/*+product.getType()+product.getPrice()+product.getidStore()*/).addValue("type", product.getType()).addValue("price", product.getPrice()).addValue("idstore", product.getidStore());

		jdbcTemplate.update(query, parameters, holder);
		
		Integer newlyCreatedId = (Integer) holder.getKeys().get("ID");
		try {
			return this.getById(newlyCreatedId);
		} catch (ProductNotFoundException e) {
			return null;
		}

	}

	public int update(Product product) {
			String query = "UPDATE PRODUCT SET NAME = :name, PRICE = :price WHERE ID = :id";
	
	        Map<String, Object> parameters = new HashMap<>();
	        parameters.put("id", product.getId());
	        parameters.put("name", product.getName());
	        parameters.put("price", product.getPrice());
	        
	        return jdbcTemplate.update(query, parameters);
	}

}
