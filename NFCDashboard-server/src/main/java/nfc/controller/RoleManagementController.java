package nfc.controller;
import java.util.List;

import nfc.model.Role;
import nfc.service.IRoleService;
import nfc.serviceImpl.common.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RoleManagementController {
	@Autowired
	private IRoleService roleDAO;
	
	@RequestMapping(value="roles",method=RequestMethod.GET)
	public String getRoles(){
		List<Role> roles = roleDAO.getListRole();
		return Utils.convertObjectToJsonString(roles);
		//return roles;
	}
	@RequestMapping(value="role/{id}", method=RequestMethod.GET)
	public String getRole(@PathVariable("id") String roleId){
		String roleStr =  Utils.convertObjectToJsonString(roleDAO.getRole(roleId));
		System.out.println("RoleStr " + roleStr);
		return roleStr;
	}
	@RequestMapping(value="role/add", method=RequestMethod.POST)
	public @ResponseBody String insertRole(@RequestBody Role role){
		role.setApp_id(Utils.appId);
		String data = roleDAO.insertRole(role) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="role/edit", method=RequestMethod.PUT)
	public @ResponseBody String editRole(@RequestBody Role role){
		String data = roleDAO.updateRole(role) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="role/delete/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteRole(@PathVariable("id") String roleId){
		System.out.println("Vao delete " + roleId);
		String data = roleDAO.deleteRole(roleId) + "";
		return "{\"result\":\"" + data + "\"}";
	}
}
