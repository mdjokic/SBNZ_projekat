package ftn.sbnz.banhammer.model;

import java.util.ArrayList;
import java.util.Date;

public class MatchInfo {
    private String id;
    private String userId;
    private boolean finished;
    private ArrayList<String> chatLog;
    private Date timestamp;

    public MatchInfo(){

    }

    public MatchInfo(String id, String userId, boolean finished, ArrayList<String> chatLog, Date timestamp){
        this.id = id;
        this.userId = userId;
        this.finished = finished;
        this.chatLog = chatLog;
        this.timestamp = timestamp;
    }
}
