package nfc.controller;

import nfc.model.AttachFile;
import nfc.model.Category;
import nfc.model.Role;
import nfc.model.ViewModel.CategoryView;
import nfc.model.ViewModel.SupplierProductView;
import nfc.service.ICategoryService;
import nfc.service.IFileService;
import nfc.serviceImpl.common.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private IFileService fileDAO;
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
	@RequestMapping(value="app/category/view/{type}", method=RequestMethod.GET)
	public @ResponseBody String getListCategoryViewFilterType(@PathVariable("type") String type){
		List<CategoryView> categoryView = categoryDAO.getListCategoryView(type);
		return Utils.convertObjectToJsonString(categoryView);
	}
	@RequestMapping(value="category/add", method=RequestMethod.POST)
	public @ResponseBody String insertCategory(@RequestBody Category cate){
		cate.setApp_id(Utils.appId);
		Date date = new Date();
		cate.setCreated_date(date);
		String data = categoryDAO.insertCategory(cate)+"";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="category/edit", method=RequestMethod.PUT)
	public @ResponseBody String editCategory(@RequestBody Category cate){
		String data = categoryDAO.updateCategory(cate)+"";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="category/delete/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteCategory(@PathVariable("id") String cateID){
		String data = categoryDAO.deleteCategory(cateID)+"";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="category/getCate/{id}", method=RequestMethod.GET)
	public String getCategory(@PathVariable("id") String cateID){
		Category category = categoryDAO.getCategory(cateID);
		return Utils.convertObjectToJsonString(category);
	} 
	@RequestMapping(value="category/getFile/{id}", method=RequestMethod.GET)
	public String getCategory(@PathVariable("id") int fileId){
		System.out.println("FileId " + fileId);
		AttachFile file = fileDAO.getAttachFile(fileId);
		return Utils.convertObjectToJsonString(file);
	} 
	@RequestMapping(value="app/product/store/{id}", method=RequestMethod.GET)
	public String getProductOfStore(@PathVariable("id") int supplierId){
		List<SupplierProductView> lstSupplierProductView = categoryDAO.getListProductOfCategory(supplierId);
		return Utils.convertObjectToJsonString(lstSupplierProductView);
	} 

}
