package ftn.sbnz.banhammer.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
@Getter
@Setter
@Entity
@Table(name="matches")
public class MatchInfo {
    @Id
    @GeneratedValue(generator = "matches_seq_gen")
    @SequenceGenerator(name = "matches_seq_gen", sequenceName = "matches_id_seq", initialValue = 1, allocationSize = 1)
    public Long id;

    public String userId;

    public Boolean finished;

    public String chatLog;

    public Date timestamp;

    @Enumerated(EnumType.STRING)
    public Report report;

    public Double kdRatio;

    public Boolean potentialFeeding;

    @Enumerated(EnumType.STRING)
    public ThreatLevel threatLevel;

    @Enumerated(EnumType.STRING)
    public Punishment punishment;

    public MatchInfo(){
        this.chatLog = "";
        this.report = Report.NONE;
        this.potentialFeeding = false;
        this.punishment = Punishment.NONE;
        this.threatLevel = ThreatLevel.NONE;
    }

    public MatchInfo(Long id, String userId, boolean finished, Report report, Double kdRatio){
        this.id = id;
        this.userId = userId;
        this.finished = finished;
        this.report = report;
        this.chatLog = "";
        this.kdRatio = kdRatio;
        this.potentialFeeding = false;
        this.punishment = Punishment.NONE;
        this.threatLevel = ThreatLevel.NONE;
    }

    public MatchInfo(Long id, String userId, boolean finished, Report report){
        this.id = id;
        this.userId = userId;
        this.finished = finished;
        this.report = report;
        this.chatLog = "";
        this.kdRatio = 1.0;
        this.potentialFeeding = false;
        this.punishment = Punishment.NONE;
        this.threatLevel = ThreatLevel.NONE;
    }

    public MatchInfo(String userId, boolean finished,
                     String chatLog, Date timestamp, Report report, Double kdRatio) {
        this.userId = userId;
        this.finished = finished;
        this.chatLog = chatLog;
        this.report = report;
        this.kdRatio = kdRatio;
        this.potentialFeeding = false;
        this.punishment = Punishment.NONE;
        this.threatLevel = ThreatLevel.NONE;
    }

    public MatchInfo(Long id, String userId, boolean finished,
                     String chatLog, Date timestamp, Report report, Double kdRatio) {
        this.id = id;
        this.userId = userId;
        this.finished = finished;
        this.chatLog = chatLog;
        this.report = report;
        this.kdRatio = kdRatio;
        this.potentialFeeding = false;
        this.punishment = Punishment.NONE;
        this.threatLevel = ThreatLevel.NONE;
    }

    public MatchInfo(Long id, String userId, boolean finished, Punishment punishment, Double kdRatio) {
        this.id = id;
        this.userId = userId;
        this.finished = finished;
        this.kdRatio = kdRatio;
        this.punishment = punishment;
    }

    @Override
    public String toString() {
        String dateISO = ZonedDateTime.now().format( DateTimeFormatter.ISO_OFFSET_DATE_TIME );
        return "MatchInfo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", finished=" + finished +
                ", chatLog='" + chatLog + '\'' +
                ", timestamp=" + dateISO +
                ", report=" + report +
                ", kdRatio=" + kdRatio +
                ", potentialFeeding=" + potentialFeeding +
                ", threatLevel=" + threatLevel +
                ", punishment=" + punishment +
                '}';
    }

    public int getFinishedInt(){
        return (this.finished) ? 1 : 0;
    }

    public Date minusMonths(int months){
        Date newDate = new Date();
        newDate = DateUtils.addMonths(newDate, months * (-1));
        return newDate;
    }



}
