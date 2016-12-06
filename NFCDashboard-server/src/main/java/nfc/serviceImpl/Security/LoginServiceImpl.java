package nfc.serviceImpl.Security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nfc.model.AppUser;
import nfc.model.UserRole;
import nfc.service.ILoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements UserDetailsService{
	@Autowired
    private ILoginService loginDAO;
	
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		AppUser user = (AppUser)loginDAO.findUserByUsername(username);
		List<GrantedAuthority> authorities = buildUserAuthority(loginDAO.getListUserRole(username));
	    return buildUserForAuthentication(user, authorities);
	}
	private List<GrantedAuthority> buildUserAuthority(Set<UserRole> appUserRole) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        // Build user's authorities
        for (UserRole userRole : appUserRole) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getRoleId() + ""));
        }
        List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
        return Result;
    }
	private User buildUserForAuthentication(AppUser user, List<GrantedAuthority> authorities) {
        return new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }
}
