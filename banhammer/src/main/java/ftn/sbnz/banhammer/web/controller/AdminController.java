package ftn.sbnz.banhammer.web.controller;

import ftn.sbnz.banhammer.model.Admin;
import ftn.sbnz.banhammer.service.AdminService;
import ftn.sbnz.banhammer.web.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody UserInfo userInfo){
        Admin admin = new Admin();
        admin.setUsername(userInfo.getUsername());
        admin.setPassword(userInfo.getPassword());
        adminService.register(admin);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
