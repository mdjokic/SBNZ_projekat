package ftn.sbnz.banhammer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(generator = "users_seq_gen")
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "users_id_seq", initialValue = 1, allocationSize = 1)
    public Long id;

    @Column(nullable = false, unique = true)
    public String username;

    public String password;

    @Enumerated(EnumType.STRING)
    public ThreatLevel threatLevel;

    @Enumerated(EnumType.STRING)
    public Punishment punishment;

    public User(){
        this.threatLevel = ThreatLevel.NONE;
        this.punishment = Punishment.NONE;
    }
}
