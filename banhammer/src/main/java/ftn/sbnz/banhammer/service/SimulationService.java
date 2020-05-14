package ftn.sbnz.banhammer.service;

import ftn.sbnz.banhammer.model.MatchInfo;

public interface SimulationService {
    public Runnable simulateMatchEvent(String userId, int finishedChance, int noReportChance);
}
