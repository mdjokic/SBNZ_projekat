package ftn.sbnz.banhammer.service.implementation;

import ftn.sbnz.banhammer.model.Admin;
import ftn.sbnz.banhammer.model.Enums.Role;
import ftn.sbnz.banhammer.model.User;
import ftn.sbnz.banhammer.service.AdminService;
import ftn.sbnz.banhammer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException  {
        User user = userService.findOne(s);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(user != null){
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.ROLE_USER.toString()));
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    grantedAuthorities);
        }

        Admin admin = adminService.findOne(s);
        if(admin != null){
            grantedAuthorities.add(new SimpleGrantedAuthority(Role.ROLE_ADMIN.toString()));
            return new org.springframework.security.core.userdetails.User(
                    admin.getUsername(),
                    admin.getPassword(),
                    grantedAuthorities);
        }
        throw new UsernameNotFoundException (String.format("No user found with username %s", s));
    }
}
