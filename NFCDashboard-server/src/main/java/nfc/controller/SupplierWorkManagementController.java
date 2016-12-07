package nfc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nfc.model.Role;
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
	@RequestMapping(value="supplierwork/add", method=RequestMethod.POST)
	public @ResponseBody String insertSupplierWork(@RequestBody SupplierWork supplierwork){
		//SupplierWork.setApp_id(Utils.appId);
		supplierwork.setSuppl_id(1);
		supplierwork.setIs_active(1);
		supplierwork.setWd_start_hm("08:00");
		supplierwork.setWd_end_hm("22:00");
		supplierwork.setSat_start_hm("08:00");
		supplierwork.setSat_end_hm("16:00");
		supplierwork.setSun_start_hm("08:00");
		supplierwork.setSun_end_hm("13:00");
		supplierwork.setIs_online(1);
		supplierwork.setRank5(3);
		supplierwork.setOrder_count(500);
		supplierwork.setFavorite_count(200);
		supplierwork.setCall_order(982347);
		supplierwork.setDirect_pay(78236);
		supplierwork.setVisit_pay(97823);
		supplierwork.setMin_order_amt(10);
		supplierwork.setOwner_suppl_id(0);
		supplierwork.setManage_suppl_id(0);
		String data = supplierworkDAO.insertSupplierWork(supplierwork)+"";
		return "{\"result\":\"" + data + "\"}";
	}
}
