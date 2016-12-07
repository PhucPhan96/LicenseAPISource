package nfc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nfc.model.Product;
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
}
