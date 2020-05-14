package ftn.sbnz.banhammer.service.implementation;

import ftn.sbnz.banhammer.model.User;
import ftn.sbnz.banhammer.repository.UserRepository;
import ftn.sbnz.banhammer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findOne(Long id){
        Optional<User> user =  userRepository.findById(id);
        return user.orElse(null);
    }

    public User findOne(String username){
        Optional<User> user =  userRepository.findUserByUsername(username);
        return user.orElse(null);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User register(User user){
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return this.save(user);
    }
}
