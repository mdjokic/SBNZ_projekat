package ftn.sbnz.banhammer.service.implementation;

import ftn.sbnz.banhammer.model.Admin;
import ftn.sbnz.banhammer.repository.AdminRepository;
import ftn.sbnz.banhammer.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin findOne(Long id){
        Optional<Admin> admin =  adminRepository.findById(id);
        return admin.orElse(null);
    }

    public Admin findOne(String username){
        Optional<Admin> admin =  adminRepository.findAdminByUsername(username);
        return admin.orElse(null);
    }

    public List<Admin> findAll(){
        return adminRepository.findAll();
    }

    public Admin save(Admin admin){
        return adminRepository.save(admin);
    }

    public Admin register(Admin admin){
        String hashedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(hashedPassword);
        return this.save(admin);
    }

}
