package nfc.service;

import java.util.List;

import nfc.model.Product;
import nfc.model.Role;

public interface IProductService {
	List<Product> getListProduct(int supplId);
	Product getProduct(int productId);
	boolean insertProduct(Product product);
	boolean updateProduct(Product product);
	Product getProducts(String productId);
	boolean deleteProduct(List<Product> products);
}
