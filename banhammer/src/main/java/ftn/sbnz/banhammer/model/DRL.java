package ftn.sbnz.banhammer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DRL {
    @Id
    @GeneratedValue(generator = "drl_seq_gen")
    @SequenceGenerator(name = "drl_seq_gen", sequenceName = "drl_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    private String name;
    private String path;
}
