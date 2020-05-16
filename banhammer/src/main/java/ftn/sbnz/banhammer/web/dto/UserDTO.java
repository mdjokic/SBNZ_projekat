package ftn.sbnz.banhammer.web.dto;

import ftn.sbnz.banhammer.model.MatchInfo;
import ftn.sbnz.banhammer.model.Punishment;
import ftn.sbnz.banhammer.model.ThreatLevel;
import ftn.sbnz.banhammer.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class UserDTO {
    private Long id;

    private String username;

    private ThreatLevel threatLevel;

    private Punishment punishment;

    private List<MatchInfo> matches = new ArrayList<>();

    public UserDTO (){

    }

    public UserDTO(User u){
        this.id = u.id;
        this.username = u.username;
        this.threatLevel = u.threatLevel;
        this.punishment = u.punishment;
    }

    public UserDTO(User u, List<MatchInfo> matches){
        this.id = u.id;
        this.username = u.username;
        this.threatLevel = u.threatLevel;
        this.punishment = u.punishment;
        this.matches = matches;
    }
}
