package nfc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import nfc.model.SupplierWork;
import nfc.service.ISupplierWorkService;
import nfc.serviceImpl.common.Utils;

@RestController
public class SupplierWorkManagementController {
	@Autowired
	private ISupplierWorkService supplierworkDAO;
	
	@RequestMapping(value="supplierwork",method=RequestMethod.GET)
	public String getSupplierWork(){
		List<SupplierWork> supplierwork = supplierworkDAO.getListSupplierWork();
		return Utils.convertObjectToJsonString(supplierwork);
	}
}
