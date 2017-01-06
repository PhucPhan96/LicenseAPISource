package nfc.service;

import java.util.List;

import nfc.model.Product;
import nfc.model.ProductCategory;
import nfc.model.ProductImage;
import nfc.model.Role;
import nfc.model.ViewModel.ProductView;

public interface IProductService {
	List<Product> getListProduct(int supplId);
	ProductView getProductView(int productId);
	List<ProductView> getListProductView(int supplId);
	List<ProductImage> getListProductImage(int productId);
	Product getProduct(int productId);
	boolean insertProductView(ProductView productView);
	boolean updateProductView(ProductView productView);
	//Product getProducts(String productId);
	boolean deleteProductView(List<ProductView> productViews);
	List<ProductCategory> getListProductCategory(int cateId);
	List<Product> getListProductOfCategory(int cateId, int supplId);
}
