package ftn.sbnz.banhammer.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimulationDTO {

    private String userId;

    private int finishedChance;

    private int noReportChance;

    private int intervalBetweenMatches;

    public SimulationDTO(){}
}
