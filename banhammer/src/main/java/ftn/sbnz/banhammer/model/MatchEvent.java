package ftn.sbnz.banhammer.model;

import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
public class MatchEvent {
    private MatchInfo matchInfo;

    public MatchEvent(){
        super();
    }

    public MatchEvent(MatchInfo matchInfo){
        this.matchInfo = matchInfo;
    }

    public MatchInfo getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(MatchInfo matchInfo) {
        this.matchInfo = matchInfo;
    }
}
