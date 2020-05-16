package ftn.sbnz.banhammer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name="matches")
public class MatchInfo {
    @Id
    @GeneratedValue(generator = "matches_seq_gen")
    @SequenceGenerator(name = "matches_seq_gen", sequenceName = "matches_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    private String userId;
    private boolean finished;
    private String chatLog;
    private Date timestamp;
    @Enumerated(EnumType.STRING)
    private Report report;

    public MatchInfo(){

    }

    public MatchInfo(String userId, boolean finished,
                     String chatLog, Date timestamp, Report report) {
        this.userId = userId;
        this.finished = finished;
        this.chatLog = chatLog;
        this.timestamp = timestamp;
        this.report = report;
    }

    public MatchInfo(Long id, String userId, boolean finished,
                     String chatLog, Date timestamp, Report report) {
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
