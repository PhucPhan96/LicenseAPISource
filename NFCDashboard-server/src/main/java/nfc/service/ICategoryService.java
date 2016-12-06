package nfc.service;

import java.util.List;

import nfc.model.Category;

public interface ICategoryService {
	List<Category> getListCategory();
	boolean insertCategory(Category cate);
	boolean updateCategory(Category cate);
}
