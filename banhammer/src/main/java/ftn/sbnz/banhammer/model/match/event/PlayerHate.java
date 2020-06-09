package ftn.sbnz.banhammer.model.match.event;

import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
@Getter
@Setter
public class PlayerHate {

    Long matchId;

    public PlayerHate(){}

    public PlayerHate(Long matchId){
        this.matchId = matchId;
    }
}
