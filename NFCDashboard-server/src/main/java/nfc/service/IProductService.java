package nfc.service;
import java.util.List;

import nfc.model.Product;
import nfc.model.Role;
public interface IProductService {
	List<Product> getListProduct();
	boolean insertProduct(Product product);
	Product getProduct(String prodId);
}
