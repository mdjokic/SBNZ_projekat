package ftn.sbnz.banhammer.model;


import java.util.ArrayList;
import java.util.List;

public class Matches {

    public List<MatchInfo> matchesList = new ArrayList<>();

    public Matches(){ }

    public int getListSize(){
        return matchesList.size();
    }

}
