package ftn.sbnz.banhammer.service;

import ftn.sbnz.banhammer.model.Admin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    Admin findOne(Long id);

    Admin findOne(String username);

    List<Admin> findAll();

    Admin save(Admin admin);

    Admin register(Admin admin);

}
