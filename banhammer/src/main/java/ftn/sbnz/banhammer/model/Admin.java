package ftn.sbnz.banhammer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Admin {

    @Id
    @GeneratedValue(generator = "admins_seq_gen")
    @SequenceGenerator(name = "admins_seq_gen", sequenceName = "admins_id_seq", initialValue = 1, allocationSize = 1)
    public Long id;

    @Column(nullable = false, unique = true)
    public String username;

    public String password;
}
