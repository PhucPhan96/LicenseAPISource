package nfc.controller;
import java.util.List;

import nfc.model.Supplier;
import nfc.service.ISupplierService;
import nfc.serviceImpl.common.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupplierManagementController {
	@Autowired
	private ISupplierService supplierDAO;
	
	@RequestMapping(value="supplier",method=RequestMethod.GET)
	public String getSupplier(){
		List<Supplier> supplier = supplierDAO.getListSupplier();
		return Utils.convertObjectToJsonString(supplier);
	}
	@RequestMapping(value="supplier/{id}", method=RequestMethod.GET)
	public String getSupplier(@PathVariable("id") String supplId){
		String supplierStr =  Utils.convertObjectToJsonString(supplierDAO.getSupplier(supplId));
		System.out.println("SupplierStr " + supplierStr);
		return supplierStr;
	}
}
