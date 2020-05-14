package ftn.sbnz.banhammer.web.controller;

import ftn.sbnz.banhammer.model.User;
import ftn.sbnz.banhammer.service.UserService;
import ftn.sbnz.banhammer.web.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody UserInfo userInfo){
        User user = new User();
        user.setUsername(userInfo.getUsername());
        user.setPassword(userInfo.getPassword());
        userService.register(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
