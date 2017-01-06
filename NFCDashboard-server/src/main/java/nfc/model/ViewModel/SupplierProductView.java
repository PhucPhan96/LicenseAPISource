package nfc.model.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nfc.model.Category;
import nfc.model.Product;

public class SupplierProductView {
	private List<Product> products = new ArrayList<Product>();
	private Category category = new Category();
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
}
