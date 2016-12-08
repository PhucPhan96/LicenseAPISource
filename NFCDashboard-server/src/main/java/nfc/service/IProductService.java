package nfc.service;
import java.util.List;

import nfc.model.Product;
public interface IProductService {
	List<Product> getListProduct();
	boolean insertProduct(Product product);
	List<Product> getProduct(String prodId);
	Product getProducts(String productId);
}
