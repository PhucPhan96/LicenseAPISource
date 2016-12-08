package nfc.controller;

import java.util.List;

import nfc.model.Group;
import nfc.model.Product;
import nfc.service.IProductService;
import nfc.serviceImpl.common.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductManagementController {
	@Autowired
	private IProductService productDAO;
	@RequestMapping(value="products",method=RequestMethod.GET)
	public String getProducts(){
		int supplId=1;
		List<Product> products = productDAO.getListProduct(supplId);
		System.out.println(products);
		return Utils.convertObjectToJsonString(products);
	}
	@RequestMapping(value="product/{id}", method=RequestMethod.GET)
	public String getRole(@PathVariable("id") String productId){
		String roleStr =  Utils.convertObjectToJsonString(productDAO.getProduct(Integer.parseInt(productId)));
		System.out.println("RoleStr " + roleStr);
		return roleStr;
	}
	@RequestMapping(value="product/add", method=RequestMethod.POST)
	public @ResponseBody String insertGroup(@RequestBody Product product){
		product.setApp_id(Utils.appId);
		System.out.println("Vao insert");
		String data = productDAO.insertProduct(product) + "";
		return "{\"result\":\"" + data + "\"}";
	}
}
