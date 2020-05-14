package ftn.sbnz.banhammer.repository;

import ftn.sbnz.banhammer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findUserByUsername(String username);

}
