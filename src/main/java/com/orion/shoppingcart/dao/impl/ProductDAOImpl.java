package com.orion.shoppingcart.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orion.mockdata.generate.DataGenerator;
import com.orion.shoppingcart.dao.ProductDAO;
import com.orion.shoppingcart.domain.Product;
import com.orion.shoppingcart.exception.ShoppingCartException;
import com.orion.shoppingcart.persist.PersistLayer;
import com.orion.shoppingcart.util.exception.SQLException;

public class ProductDAOImpl implements ProductDAO {

	private DataGenerator dataGenerator = new DataGenerator();
	private static final Logger log = LoggerFactory.getLogger(ProductDAOImpl.class);
	private PersistLayer persistLayer = new PersistLayer();

	@Override
	public Product get(String productNumber) {
		log.info("Getting a product");
		return dataGenerator.generateProduct(productNumber);
	}

	public Product generate() {
		log.info("Generating a product");
		return dataGenerator.generateProduct();
	}
	
	public Product generateProductWithSku() {
		log.info("Generating a product with Sku");
		Product product = dataGenerator.generateProduct();
		product.addSku(dataGenerator.generateRandomSku());
		return product;
	}

	@Override
	public Product create(String productNumber) {
		log.info("Creating a product");
		return dataGenerator.generateProduct(productNumber);
	}

	@Override
	public Product update(Product product) {
		try {
			log.info("Updating Product");
			boolean success = persistLayer.persist(product);
			if (success)
				return product;
		} catch (SQLException e) {
			log.error("SQL Exception while updating product: {}", e.getMessage());
		}
		return null;
	}

	@Override
	public boolean delete(Product product) {
		try {
			log.info("Deleting Product");
			boolean success = persistLayer.persist(product);
			if (success)
				return true;
		} catch (Exception e) {
			throw new ShoppingCartException("Unable to delete Product " + product.getProductNumber());			
		}
		return false;
	}
}
