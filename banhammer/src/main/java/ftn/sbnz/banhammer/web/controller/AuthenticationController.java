package ftn.sbnz.banhammer.web.controller;

import ftn.sbnz.banhammer.model.Admin;
import ftn.sbnz.banhammer.model.Enums.Role;
import ftn.sbnz.banhammer.model.User;
import ftn.sbnz.banhammer.security.TokenUtils;
import ftn.sbnz.banhammer.service.AdminService;
import ftn.sbnz.banhammer.service.UserService;
import ftn.sbnz.banhammer.web.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    TokenUtils tokenUtils;

    @Validated
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> authentication(@Valid @RequestBody UserInfo userInfo){
        try {
            String token = tryToAuthenticate(userInfo);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Login failed; Invalid user email or password", HttpStatus.BAD_REQUEST);
        }
    }

    private String tryToAuthenticate(UserInfo userInfo){
        User user = userService.findOne(userInfo.getUsername());
        if(user != null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), userInfo.getPassword());
            Authentication authentication = authenticationManager.authenticate(token);
            UserDetails details = userDetailsService.loadUserByUsername(user.getUsername());
            return tokenUtils.generateToken(details, Role.ROLE_USER);
        }
        Admin admin = adminService.findOne(userInfo.getUsername());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                admin.getUsername(), userInfo.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        UserDetails details = userDetailsService.loadUserByUsername(admin.getUsername());
        return tokenUtils.generateToken(details, Role.ROLE_ADMIN);
    }

}
