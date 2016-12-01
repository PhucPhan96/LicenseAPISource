package nfc.controller;

import java.util.List;

import nfc.model.Supplier;
import nfc.service.ISupplierService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SupplierManagementController {
	@Autowired
	private ISupplierService supplierDAO;
	
	@RequestMapping("/supplier")
	public String getSupplier(){
		List<Supplier> suppliers = supplierDAO.getListSupplier();
		System.out.println("Size " + suppliers.size());
		return "aaaa";
	}
//	@RequestMapping("/insertSupplier")
//	public void insertSupplier(){
//		Supplier supplier = new Supplier();
//		
//		supplier.setSuppl_id(001);
//		supplier.setSupplier_name("RedBull Live");	
//		supplier.setShort_name("Live");		
//		supplier.setOfficial_name("RedBull Live");		
//		supplier.setPhone_no("0902722894");	
//		supplier.setMobile_no("01655850535");	
//		supplier.setAddress("SBI");	
//		supplier.setCity("HCM");		
//		supplier.setRegion("South VN");
//		supplier.setZip_code("70000");		
//		supplier.setCountry("Viet Nam");		
//		supplier.setOrder_phone_no("0902722894");		
//		supplier.setFax_no("78263");		
//		supplier.setBusi_intro("Hello");
//		supplier.setSales_info("Welcome");
//		supplier.setApp_id(1);		
//		Boolean insertsupllier = supplierDAO.insertSupplier(supplier);
//		System.out.println("Inserted");
//	}
}
