package nfc.controller;

import java.util.List;

import nfc.model.Product;
import nfc.model.Role;
import nfc.model.ViewModel.ProductView;
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
/*	@PreAuthorize("hasRole('SysAdmin')")*/
	@RequestMapping(value="product/supplier/{id}",method=RequestMethod.GET)
	public String getListProductViewBySupplier(@PathVariable("id") int supplId){
		List<ProductView> lstProductView = productDAO.getListProductView(supplId);
		return Utils.convertObjectToJsonString(lstProductView);
	}
	@RequestMapping(value="products/{id}",method=RequestMethod.GET)
	/*	@PreAuthorize("hasRole('SysAdmin')")*/
		public String getProductsBySuppID(@PathVariable("id") int supplId){
			List<Product> products = productDAO.getListProduct(supplId);
			System.out.println(products);
			return Utils.convertObjectToJsonString(products);
		}
	@RequestMapping(value="app/product/{id}", method=RequestMethod.GET)
	public String getProduct(@PathVariable("id") String productId){
		/*String resutl =  Utils.convertObjectToJsonString(productDAO.getProduct(Integer.parseInt(productId)));
		return resutl;*/
		String resutl =  Utils.convertObjectToJsonString(productDAO.getProductView(Integer.parseInt(productId)));
		return resutl;
	}
	@RequestMapping(value="app/products/{id}", method=RequestMethod.GET)
	public String getProductOptional(@PathVariable("id") String productId){
		/*String resutl =  Utils.convertObjectToJsonString(productDAO.getProduct(Integer.parseInt(productId)));
		return resutl;*/
		String resutl =  Utils.convertObjectToJsonString(productDAO.getProductOptional(Integer.parseInt(productId)));
		return resutl;
	}
	@RequestMapping(value="product/add", method=RequestMethod.POST)
	public @ResponseBody String insertGroup(@RequestBody ProductView productView){
		String data = productDAO.insertProductView(productView) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="product/edit", method=RequestMethod.PUT)
	public @ResponseBody String eidtProduct(@RequestBody ProductView productView){
		String data = productDAO.updateProductView(productView) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	

	@RequestMapping(value="product/delete", method=RequestMethod.POST)
	public @ResponseBody String deleteProduct(@RequestBody List<ProductView> productViews){
		String data = productDAO.deleteProductView(productViews) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	
}
