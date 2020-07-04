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
    private boolean feedingHandled;

    public MatchEvent(){
        super();
        this.handled = false;
        this.feedingHandled = false;
    }

    public MatchEvent(MatchInfo matchInfo){
        this.matchInfo = matchInfo;
        this.handled = false;
        this.feedingHandled = false;
    }

}
