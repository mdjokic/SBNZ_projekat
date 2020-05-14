package ftn.sbnz.banhammer.repository;

import ftn.sbnz.banhammer.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    public Optional<Admin> findAdminByUsername(String username);

}
