package ftn.sbnz.banhammer.model.match.event;

import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
@Getter
@Setter
public class PlayerFlame {

    Long matchId;

    boolean provoked;

    public PlayerFlame(){}

    public PlayerFlame(Long matchId){
        this.matchId = matchId;
        this.provoked = false;
    }
}
