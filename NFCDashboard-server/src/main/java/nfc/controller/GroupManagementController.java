package nfc.controller;

import java.util.List;

import nfc.model.Group;
import nfc.service.IGroupService;
import nfc.serviceImpl.common.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupManagementController {
	@Autowired
	private IGroupService groupDAO;
	
	@RequestMapping(value="groups",method=RequestMethod.GET)
	public String getRoles(){
		List<Group> groups = groupDAO.getListGroup();
		return Utils.convertObjectToJsonString(groups);
	}
	@RequestMapping(value="group/add", method=RequestMethod.POST)
	public @ResponseBody String insertGroup(@RequestBody Group group){
		group.setApp_id(Utils.appId);
		System.out.println("Vao insert");
		String data = groupDAO.insertGroup(group) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="group/edit", method=RequestMethod.PUT)
	public @ResponseBody String editGroup(@RequestBody Group group){
		String data = groupDAO.updateGroup(group) + "";
		return "{\"result\":\"" + data + "\"}";
	}
	@RequestMapping(value="group/delete/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteGroup(@PathVariable("id") String groupId){
		String data = groupDAO.deleteGroup(groupId)+"";
		return "{\"result\":\"" + data + "\"}";
	}
}
