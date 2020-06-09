package ftn.sbnz.banhammer.model.match.event;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;


@Role(Role.Type.EVENT)
@Getter
@Setter
public class FriendlyFire {

    UUID id;

    Long matchId;

    public FriendlyFire(){}

    public FriendlyFire(UUID id, Long matchId){
        this.id = id;
        this.matchId = matchId;
    }

}
