package nfc.service;

import java.util.List;

import nfc.model.Category;
import nfc.model.ViewModel.CategoryView;

public interface ICategoryService {
	List<Category> getListCategory();
	List<Category> getListCategoryFilterType(String type);
	boolean insertCategory(Category cate);
	boolean updateCategory(Category cate);
	boolean deleteCategory(String cateID);
	Category getCategory(String cateID);
	List<CategoryView> getListCategoryView(String type);
}
