package nfc.controller;
import java.util.List;

import nfc.model.Role;
import nfc.model.Supplier;
import nfc.service.ISupplierService;
import nfc.serviceImpl.common.Utils;

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
import com.google.gson.Gson;

@RestController
public class SupplierManagementController {
	@Autowired
	private ISupplierService supplierDAO;
	
	@RequestMapping(value="supplier",method=RequestMethod.GET)
	public String getSupplier(){
		List<Supplier> supplier = supplierDAO.getListSupplier();
		return Utils.convertObjectToJsonString(supplier);
		//return supplier;
	}
}
