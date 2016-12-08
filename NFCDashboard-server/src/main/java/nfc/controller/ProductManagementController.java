package nfc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nfc.model.Product;
import nfc.model.Role;
import nfc.service.IProductService;
import nfc.serviceImpl.common.Utils;
@RestController
public class ProductManagementController {
	@Autowired
	private IProductService productDAO;
	@RequestMapping(value="products",method=RequestMethod.GET)
	public String getProducts(){
		List<Product> products = productDAO.getListProduct();
		return Utils.convertObjectToJsonString(products);
	}
	@RequestMapping(value="products/add", method=RequestMethod.POST)
	public @ResponseBody String insertProduct(@RequestBody Product product){
		//product.setApp_id(Utils.appId);
		String data = productDAO.insertProduct(product)+"";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="products/{id}", method=RequestMethod.GET)
	public String getProduct(@PathVariable("id") String prodId){
		String prodStr =  Utils.convertObjectToJsonString(productDAO.getProduct(prodId));
		System.out.println("ProdStr " + prodStr);
		return prodStr;
	}
	@RequestMapping(value="product/{id}", method=RequestMethod.GET)
	public String getProducts(@PathVariable("id") String productId){
		String productStr =  Utils.convertObjectToJsonString(productDAO.getProducts(productId));
		System.out.println("RoleStr " + productStr);
		return productStr;
	}
}
