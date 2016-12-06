package nfc.controller;

import nfc.model.Category;
import nfc.model.Role;
import nfc.model.User;
import nfc.service.ICategoryService;
import nfc.serviceImpl.common.Utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	@RequestMapping(value="category/add", method=RequestMethod.POST)
	public @ResponseBody String insertCategory(@RequestBody Category cate){
		cate.setApp_id(Utils.appId);
		String data = categoryDAO.insertCategory(cate)+"";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="category/edit", method=RequestMethod.PUT)
	public @ResponseBody String editCategory(@RequestBody Category cate){
		String data = categoryDAO.updateCategory(cate)+"";
		return "{\"result\":\"" + data + "\"}";
	}

}
