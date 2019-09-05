package com.orion.shoppingcart.manager;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orion.mockdata.generate.utils.DateUtils;
import com.orion.mockdata.generate.utils.RandomUtil;
import com.orion.shoppingcart.domain.Customer;
import com.orion.shoppingcart.domain.Order;
import com.orion.shoppingcart.domain.OrderDetail;
import com.orion.shoppingcart.domain.Product;
import com.orion.shoppingcart.domain.ShoppingCart;
import com.orion.shoppingcart.util.exception.CustomerInActiveException;

public class ShoppingCartManager {

	private static final Logger log = LoggerFactory.getLogger(ShoppingCartManager.class);
	private ShoppingCart cart = new ShoppingCart();
	
	public ShoppingCartManager(Customer customer) {
		log.info("In the constructor of ShoppingCartManager");
		cart.setCustomer(customer);
	}

	public void add(Product product, int quantity) {
		log.info("Adding a product with quantity: {}", quantity);
		this.cart.add(product, quantity);
		log.info("Completed adding a product with quantity: {}", quantity);
	}

	public Order checkout() {
		log.info("Lets checkout the shopping cart");
		log.info("Lets get the list of products from the shopping cart first.");
		Map<Product, Integer> productQuantityMap = cart.getProductQuantityMap();
		
		log.info("Creating a new Order for the customer");
		Order order = new Order();
		order.setOrderNumber(RandomUtil.generateRandomAlphaString(3) + RandomUtil.generateRandomNumericString(6));
		order.setLastUpdated(DateUtils.getNow());
		order.setOrderDate(DateUtils.getNow());
		order.setCustomer(cart.getCustomer());

		productQuantityMap.forEach((product,quantity) -> process(order, product, quantity));
		order.setOrderComplete(true);
		
		log.info("Completing the Order ...");
		return order;
	}

	private void process(Order order, Product product, Integer quantity) {
		log.info("Processing the Order ...");
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setOrderNumber(order.getOrderNumber());
		orderDetail.setOrderDetailNumber(RandomUtil.generateRandomAlphaString(3) + RandomUtil.generateRandomAlphaString(6));
		orderDetail.setProduct(product);
		orderDetail.setQuantity(quantity);
		
		log.info("Adding OrderDetail to the order ...");
		order.add(orderDetail);
		log.info("Completed adding OrderDetail to the order");
	}

	public void validate() {
		log.info("Validating the Order ...");
		Customer customer = cart.getCustomer();
		if (!customer.isActive()) {
			throw new CustomerInActiveException("Cannot create an order for an inactive customer: Customer is: " + customer.getAccountNumber());
		}
		log.info("Completed validating Order");
	}
}
