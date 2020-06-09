package ftn.sbnz.banhammer.model.match.event;

import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
@Getter
@Setter
public class PlayerFriendlyFire {

    Long matchId;

    boolean provoked;

    public PlayerFriendlyFire(){}

    public PlayerFriendlyFire(Long matchId){
        this.matchId = matchId;
        this.provoked = false;
    }
}
