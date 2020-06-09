package ftn.sbnz.banhammer.model;

import lombok.Getter;
import lombok.Setter;
import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
@Setter
@Getter
public class MatchEvent {
    private MatchInfo matchInfo;

    private boolean handled;

    public MatchEvent(){
        super();
        this.handled = false;
    }

    public MatchEvent(MatchInfo matchInfo){
        this.matchInfo = matchInfo;
        this.handled = false;
    }

}
