package ftn.sbnz.banhammer.web.controller;

import ftn.sbnz.banhammer.model.MatchInfo;
import ftn.sbnz.banhammer.model.User;
import ftn.sbnz.banhammer.service.MatchService;
import ftn.sbnz.banhammer.service.UserService;
import ftn.sbnz.banhammer.service.implementation.MatchServiceImpl;
import ftn.sbnz.banhammer.web.dto.UserDTO;
import ftn.sbnz.banhammer.web.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MatchService matchService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody UserInfo userInfo){
        User user = new User();
        user.setUsername(userInfo.getUsername());
        user.setPassword(userInfo.getPassword());
        userService.register(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers(){
        List<User> users = userService.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User u: users) {
            userDTOS.add(new UserDTO(u));
        }
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        User user = userService.findOne(id);

        if (user == null){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }

        List<MatchInfo> matches = matchService.findAllByUserId(user.username);
        UserDTO userDTO = new UserDTO(user, matches);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
