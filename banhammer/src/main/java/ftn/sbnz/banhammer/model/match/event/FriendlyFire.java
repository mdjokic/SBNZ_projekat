package ftn.sbnz.banhammer.model.match.event;

import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
@Getter
@Setter
public class FriendlyFire {

    Long matchId;

    public FriendlyFire(){}

    public FriendlyFire(Long matchId){
        this.matchId = matchId;
    }

}
