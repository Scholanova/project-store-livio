package com.scholanova.projectstore.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.models.Product;


public class ProductRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;

	public ProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Product getById(Integer id) throws ModelNotFoundException {
		String query = "SELECT ID as id, " +
				"NAME as name, " +
				"TYPE as type, " +
				"PRICE as price "+
				"FROM PRODUCT " +
				"WHERE ID = :id";

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("id", id);

		return jdbcTemplate.query(query,
				parameters,
				new BeanPropertyRowMapper<>(Product.class))
				.stream()
				.findFirst()
				.orElseThrow(ModelNotFoundException::new);
	}
	
	public Product create(Product product) {
		KeyHolder holder = new GeneratedKeyHolder();

		String query = "INSERT INTO PRODUCT (NAME,TYPE,QUANTITY,IDSTORE) VALUES (:name) (:type) (:price) (:idstore) ";

		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("name", product.getName()).addValue("price", product.getPrice()).addValue("idstore", product.getidStore());

		jdbcTemplate.update(query, parameters, holder);
		
		Integer newlyCreatedId = (Integer) holder.getKeys().get("ID");
		try {
			return this.getById(newlyCreatedId);
		} catch (ModelNotFoundException e) {
			return null;
		}

	}
}
