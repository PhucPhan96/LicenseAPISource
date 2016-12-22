package nfc.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nfc.model.Supplier;
import nfc.model.ViewModel.SupplierView;
import nfc.service.ISupplierService;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.common.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupplierManagementController {
	@Autowired
	private ISupplierService supplierDAO;
	@Value("Authorization")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

	@RequestMapping(value="supplier",method=RequestMethod.GET)
	public String getSupplier(){
		List<Supplier> supplier = supplierDAO.getListSupplier();
		return Utils.convertObjectToJsonString(supplier);
	}
	@RequestMapping(value="supplier/view",method=RequestMethod.GET)
	public String getListSupplierView(HttpServletRequest request){
		String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
		List<SupplierView> lstSupplierView = supplierDAO.getListSupplierView(username);
		return Utils.convertObjectToJsonString(lstSupplierView);
	}
	@RequestMapping(value="supplier/{id}", method=RequestMethod.GET)
	public String getSupplier(@PathVariable("id") String supplId){
		String supplierStr =  Utils.convertObjectToJsonString(supplierDAO.getSupplier(supplId));
		System.out.println("SupplierStr " + supplierStr);
		return supplierStr;
	}
	
	//get supplier view
	@RequestMapping(value="supplier/detail/{id}", method=RequestMethod.GET)
	public String getSupplierView(@PathVariable("id") String supplId, HttpServletRequest request){
		/*String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);*/
		String supplierView =  Utils.convertObjectToJsonString(supplierDAO.getSupplierView(Integer.parseInt(supplId)));
		System.out.println("supplierViewStr" + supplierView);
		return supplierView;
		
	}
	@RequestMapping(value="supplier/add", method=RequestMethod.POST)
	public @ResponseBody String insertSupplier(@RequestBody SupplierView supplierView, HttpServletRequest request){
		//System.out.println("Vao Inser supplier ne " + supplierView.getSupplier().getApp_id());
		//System.out.println("Image size " + supplierView.getImages().size());
		String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
		String data = supplierDAO.insertSupplierView(supplierView, username) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="supplier/edit", method=RequestMethod.PUT)
	public @ResponseBody String updateSupplier(@RequestBody SupplierView supplierView){
		//System.out.println("Vao Inser supplier ne " + supplierView.getSupplier().getApp_id());
		//System.out.println("Image size " + supplierView.getImages().size());
		String data = supplierDAO.updateSupplierView(supplierView) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="supplier/delete/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteRole(@PathVariable("id") String supplierId){
		System.out.println("Vao delete " + supplierId);
		String data = supplierDAO.deleteSupplierView(Integer.parseInt(supplierId)) + "";
		return "{\"result\":\"" + data + "\"}";
	}
}
