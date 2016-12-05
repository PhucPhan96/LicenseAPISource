package nfc.controller;

import java.util.List;

import nfc.model.Group;
import nfc.model.Role;
import nfc.service.IGroupService;
import nfc.service.IRoleService;
import nfc.serviceImpl.common.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
}
