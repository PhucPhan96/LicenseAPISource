package nfc.controller;

import nfc.model.Category;
import nfc.service.ICategoryService;
import nfc.serviceImpl.common.Utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryManagementController {
	@Autowired
	private ICategoryService categoryDAO;
	@RequestMapping(value="category",method=RequestMethod.GET)
	public String getListCategory(){
		List<Category> category = categoryDAO.getListCategory();
		return Utils.convertObjectToJsonString(category);
	} 
	@RequestMapping(value="category/{type}", method=RequestMethod.GET)
	public @ResponseBody String getListCategoryFilterType(@PathVariable("type") String type){
		List<Category> category = categoryDAO.getListCategoryFilterType(type);
		return Utils.convertObjectToJsonString(category);
	}

}
