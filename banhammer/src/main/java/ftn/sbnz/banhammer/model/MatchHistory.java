package ftn.sbnz.banhammer.model;

import java.util.ArrayList;

public class MatchHistory {
    private ArrayList<MatchInfo> history = new ArrayList<>();

    public MatchHistory(){

    }

    public MatchHistory(ArrayList<MatchInfo> history){
        this.history = history;
    }

    public ArrayList<MatchInfo> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<MatchInfo> history) {
        this.history = history;
    }
}
