package ftn.sbnz.banhammer.service;

import ftn.sbnz.banhammer.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User findOne(Long id);

    User findOne(String username);

    List<User> findAll();

    User save(User user);

    User register(User user);

}
