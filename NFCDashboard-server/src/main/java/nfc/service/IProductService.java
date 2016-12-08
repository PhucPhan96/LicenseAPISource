package nfc.service;

import java.util.List;

import nfc.model.Product;

public interface IProductService {
	List<Product> getListProduct(int supplId);
	Product getProduct(int productId);
	boolean insertProduct(Product product);
}
