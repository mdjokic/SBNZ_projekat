package ftn.sbnz.banhammer.model.match.event;

import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
@Getter
@Setter
public class Ping {

    Long matchId;

    public Ping(){}

    public Ping(Long matchId){
        this.matchId = matchId;
    }

}
