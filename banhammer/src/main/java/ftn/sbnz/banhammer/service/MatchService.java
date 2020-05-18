package ftn.sbnz.banhammer.service;

import ftn.sbnz.banhammer.model.Admin;
import ftn.sbnz.banhammer.model.MatchInfo;

import java.util.List;

public interface MatchService {
    Runnable simulateMatchEvent(String userId, int finishedChance, int noReportChance);

    MatchInfo findOne(Long id);

    List<MatchInfo> findAll();

    List<MatchInfo> findLatest();

    List<MatchInfo> findAllByUserId(String userId);

    MatchInfo save(MatchInfo matchInfo);

    void reset();

}
