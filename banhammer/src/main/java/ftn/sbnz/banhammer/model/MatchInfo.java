package ftn.sbnz.banhammer.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


@Getter
@Setter
public class MatchInfo {
    private String id;
    private String userId;
    private boolean finished;
    private ArrayList<String> chatLog;
    private Date timestamp;
    private Report report;

    public MatchInfo(){

    }

    public MatchInfo(String userId, boolean finished,
                     ArrayList<String> chatLog, Date timestamp, Report report) {
        this.userId = userId;
        this.finished = finished;
        this.chatLog = chatLog;
        this.timestamp = timestamp;
        this.report = report;
    }

    public MatchInfo(String id, String userId, boolean finished,
                     ArrayList<String> chatLog, Date timestamp, Report report) {
        this.id = id;
        this.userId = userId;
        this.finished = finished;
        this.chatLog = chatLog;
        this.timestamp = timestamp;
        this.report = report;
    }

    @Override
    public String toString() {
        String dateISO = ZonedDateTime.now().format( DateTimeFormatter.ISO_OFFSET_DATE_TIME );

        return "MatchInfo{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", finished=" + finished +
                ", chatLog=" + chatLog +
                ", timestamp=" + dateISO +
                ", report=" + report +
                '}';
    }
}
