package ftn.sbnz.banhammer.repository;

import ftn.sbnz.banhammer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findUserByUsername(String username);

    @Query(nativeQuery=true, value="SELECT *  FROM users ORDER BY random() LIMIT 1")
    public User getRandom();

}
