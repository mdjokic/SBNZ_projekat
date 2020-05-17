package ftn.sbnz.banhammer.web.dto;

import ftn.sbnz.banhammer.model.Punishment;
import ftn.sbnz.banhammer.model.Report;

import java.util.Date;

public class MatchDTO {

    private Long id;

    private String usernameId;

    private boolean finished;

    private String chatLog;

    private Date timestamp;

    private Report report;

    private Punishment punishment;
}
