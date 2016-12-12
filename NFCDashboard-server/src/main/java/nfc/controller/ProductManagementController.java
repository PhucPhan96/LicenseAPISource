package nfc.controller;

import java.util.List;

import nfc.model.Product;
import nfc.model.Role;
import nfc.service.IProductService;
import nfc.serviceImpl.common.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
/*@PreAuthorize("hasRole('Store')")*/
public class ProductManagementController {
	@Autowired
	private IProductService productDAO;
	@RequestMapping(value="products",method=RequestMethod.GET)
/*	@PreAuthorize("hasRole('SysAdmin')")*/
	public String getProducts(){
		System.out.println("Vao nay ne product");
		int supplId=1;
		List<Product> products = productDAO.getListProduct(supplId);
		System.out.println(products);
		return Utils.convertObjectToJsonString(products);
	}
	@RequestMapping(value="product/{id}", method=RequestMethod.GET)
	public String getProduct(@PathVariable("id") String productId){
		String resutl =  Utils.convertObjectToJsonString(productDAO.getProduct(Integer.parseInt(productId)));
		return resutl;
	}
	@RequestMapping(value="product/add", method=RequestMethod.POST)
	public @ResponseBody String insertGroup(@RequestBody Product product){
		product.setApp_id(Utils.appId);
		product.setSuppl_id(1);
		System.out.println("Vao insert");
		System.out.println("Size " + product.getAttachFiles().size());
		System.out.println("Size " + product.getCate_id());
		String data = productDAO.insertProduct(product) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="product/edit", method=RequestMethod.PUT)
	public @ResponseBody String eidtProduct(@RequestBody Product product){
		String data = productDAO.updateProduct(product) + "";
		return "{\"result\":\"" + data + "\"}";
	

	@RequestMapping(value="product/delete", method=RequestMethod.POST)
	public @ResponseBody String deleteProduct(@RequestBody List<Product> products){
		String data = productDAO.deleteProduct(products) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	
}
