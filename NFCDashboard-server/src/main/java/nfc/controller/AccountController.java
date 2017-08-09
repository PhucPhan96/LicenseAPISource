package nfc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nfc.messages.BaseResponse;
import nfc.messages.ErrorResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nfc.model.User;
import nfc.model.UserRegister;
import nfc.model.ViewModel.UserModelLogin;
import nfc.service.IUserService;
import nfc.serviceImpl.Security.JwtAuthenticationRequest;
import nfc.serviceImpl.Security.JwtAuthenticationResponse;
import nfc.serviceImpl.Security.JwtTokenUtil;
import nfc.serviceImpl.Security.JwtUser;
import nfc.serviceImpl.common.Utils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

@RestController
public class AccountController {
	@Value("Authorization")
    private String tokenHeader;
	
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private IUserService userDAO;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest){
        // Perform the security
//		final Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        authenticationRequest.getUsername(),
//                        authenticationRequest.getPassword()
//                )
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        // Reload password post-security so we can generate token
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//        final String token = jwtTokenUtil.generateToken(userDetails);
//        //userDAO.insertUserLogin( authenticationRequest.getUsername());
//        // Return the token
//        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
//        
        
        // Perform the security
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException be) {
            ErrorResponse error = new ErrorResponse();
            error.setResultCode(BaseResponse.FAILED);
            error.setErrorMsg(be.getMessage());
            return ResponseEntity.ok(error);
        } catch (AuthenticationException ex) {
            ErrorResponse error = new ErrorResponse();
            error.setResultCode(BaseResponse.FAILED);
            error.setErrorMsg(ex.getMessage());
            return ResponseEntity.ok(error);
        }

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // create user model
        JwtUser jwtUser = (JwtUser) userDetails;
        UserModelLogin user = new UserModelLogin();
        user.setUser_id(jwtUser.getId());
        user.setUser_name(jwtUser.getUsername());
        user.setFirst_name(jwtUser.getFirstname());
        user.setLast_name(jwtUser.getLastname());
        user.setMiddle_name(jwtUser.getMiddlename());
        user.setEmail(jwtUser.getEmail());
        List<String> roles = new ArrayList<String>();
        for(GrantedAuthority grantAuth: jwtUser.getAuthorities()){
            roles.add(grantAuth.getAuthority());
        }
        user.setRoles(roles);
        // Return the token
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(token, user);
        response.setResultCode(BaseResponse.OK);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @RequestMapping(value = "/app/user/add", method = RequestMethod.POST)
    public ResponseEntity<?> registerUser(@RequestBody UserRegister userRegister) {
    	String body = userDAO.saveUserRegister(userRegister);
    	
    	if(body.contains(":"))
    	{	
    		String username = body.split(":")[0];
    		String password = body.split(":")[1];
    		final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(body.split(":")[0]);
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    	}
        return ResponseEntity.ok(new JwtAuthenticationResponse(body));
    }
    @RequestMapping(value = "/app/user/shaq/{id}", method = RequestMethod.GET)
    public String getSha1(@PathVariable("id") String password) {
    	return Utils.BCryptPasswordEncoder(Utils.Sha1(password));
    }
    @RequestMapping(value = "/app/user/forgotPasword", method = RequestMethod.POST)
    public ResponseEntity<?> forgotPasword(@RequestBody User user) {
    	String body = userDAO.forgotPassword(user);
    	System.out.println("Vao ham forgotPassword");
    	if(body.contains(":"))
    	{	
    		String username = body.split(":")[0];
    		String password = body.split(":")[1];
    		final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(body.split(":")[0]);
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    	}
        return ResponseEntity.ok(new JwtAuthenticationResponse(body));
    }
}
