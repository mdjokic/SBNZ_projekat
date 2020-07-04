package ftn.sbnz.banhammer.web.dto;

import ftn.sbnz.banhammer.model.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MatchDTO {

    private Long id;

    private String usernameId;

    private Boolean finished;

    private String chatLog;

    private Date timestamp;

    private Report report;

    private Punishment punishment;

    private ThreatLevel threatLevel;

    private double kd;

    public MatchDTO(){}

    public MatchDTO(MatchInfo matchInfo, User user){
        this.id = matchInfo.getId();
        this.usernameId = user.getUsername();
        this.finished = matchInfo.getFinished();
        this.chatLog = matchInfo.getChatLog();
        this.timestamp = matchInfo.getTimestamp();
        this.report = matchInfo.getReport();
        this.punishment = matchInfo.getPunishment();
        this.threatLevel = matchInfo.getThreatLevel();
        this.kd = matchInfo.getKdRatio();

    }
}
