package nfc.service;

import java.util.List;

import nfc.model.Category;

public interface ICategoryService {
	List<Category> getListCategory();
	List<Category> getListCategoryFilterType(String type);
}
