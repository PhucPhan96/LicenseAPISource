package nfc.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import nfc.model.Supplier;
import nfc.model.SupplierFavorite;
import nfc.model.ViewModel.SupplierAppView;
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
	public List<Supplier> getSupplier(){
		List<Supplier> supplier = supplierDAO.getListSupplier();
		return supplier;
		//return Utils.convertObjectToJsonString(supplier);
	}
	@RequestMapping(value="supplier/view",method=RequestMethod.GET)
	public List<SupplierView> getListSupplierView(HttpServletRequest request){
		String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
		List<SupplierView> lstSupplierView = supplierDAO.getListSupplierView(username);
		System.out.println(Utils.convertObjectToJsonString(lstSupplierView));
		return lstSupplierView;
		//return Utils.convertObjectToJsonString(lstSupplierView);
	}
	@RequestMapping(value="app/supplier/{id}",method=RequestMethod.GET)
	public List<SupplierAppView> getListSupplierViewOfCategory(@PathVariable("id") int categoryId){
		List<SupplierAppView> lstSupplierView = supplierDAO.getListSupplierViewOfCategory(categoryId);
		return lstSupplierView;
		//return Utils.convertObjectToJsonString(lstSupplierView);
	}
	
	@RequestMapping(value="supplier/user",method=RequestMethod.GET)
	public Supplier getListSupplierUser(HttpServletRequest request){
		String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
		Supplier supplier = supplierDAO.getSupplierFromUser(username);
		return supplier;
		//return Utils.convertObjectToJsonString(supplier);
	}
	@RequestMapping(value="supplier/{id}", method=RequestMethod.GET)
	public Supplier getSupplier(@PathVariable("id") String supplId){
		Supplier supplier =  supplierDAO.getSupplier(supplId);
		//System.out.println("SupplierStr " + supplierStr);
		return supplier;
	}
	@RequestMapping(value="app/supplierFavorite/{id}", method=RequestMethod.GET)
	public @ResponseBody String getSupplierFavorite(@PathVariable("id") int supplId){
		String supplierFavorite =  supplierDAO.getSupplierFavorite(supplId);
		return "{\"result\":\"" + supplierFavorite + "\"}";
	}

	//get supplier view
	@RequestMapping(value="supplier/detail/{id}", method=RequestMethod.GET)
	public SupplierView getSupplierView(@PathVariable("id") String supplId, HttpServletRequest request){
		/*String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);*/
		SupplierView supplierView =  supplierDAO.getSupplierView(Integer.parseInt(supplId));
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
	@RequestMapping(value="app/supplierFavorite/add/{supplierID}/{userID}", method=RequestMethod.POST)
	public @ResponseBody String insertSupplierFavorite(@PathVariable("supplierID") int supplierId, @PathVariable("userID") String userId){
		//System.out.println("Vao Inser supplier ne " + supplierView.getSupplier().getApp_id());
		//System.out.println("Image size " + supplierView.getImages().size());
//		String token = request.getHeader(tokenHeader);
//        String username = jwtTokenUtil.getUsernameFromToken(token);
		String data = supplierDAO.insertSupplierFavorite(supplierId, userId) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="app/isSupplierFavorite/{id}", method=RequestMethod.GET)
	public SupplierFavorite isSupplierFavorite(@PathVariable("id") String userId){
		SupplierFavorite supplierFavorite =  supplierDAO.isSupplierFavorite(userId);
		//System.out.println("SupplierStr " + supplierStr);
		return supplierFavorite;
	}
	@RequestMapping(value="app/deleteSupplierFavorite/{supplierID}/{userID}", method=RequestMethod.POST)
	public String deleteSupplierFavorite(@PathVariable("supplierID") int supplId, @PathVariable("userID") String userId){
		System.out.println("Vao delete " + supplId);
		System.out.println("Vao delete " + userId);
		String data = supplierDAO.deleteSupplierFavorite(supplId, userId) + "";
		return "{\"result\":\"" + data + "\"}";
	}
}
