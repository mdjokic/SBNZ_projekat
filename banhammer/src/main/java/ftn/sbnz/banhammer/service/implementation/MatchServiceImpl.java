package ftn.sbnz.banhammer.service.implementation;

import ftn.sbnz.banhammer.model.MatchInfo;
import ftn.sbnz.banhammer.model.Report;
import ftn.sbnz.banhammer.model.User;
import ftn.sbnz.banhammer.repository.MatchInfoRepository;
import ftn.sbnz.banhammer.repository.UserRepository;
import ftn.sbnz.banhammer.service.MatchService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MatchInfoRepository matchInfoRepository;

    @Autowired
    KieContainer kieContainer;

    @Override
    public Runnable simulateMatchEvent(String userId, int finishedChance, int noReportChance) {
        return () -> {
            KieSession kieSession = kieContainer.newKieSession();
            MatchInfo randomMatchInfo = getRandomMatchInfo(userId, finishedChance, noReportChance);



            matchInfoRepository.save(randomMatchInfo);
        };
    }

    @Override
    public MatchInfo findOne(Long id) {
        return matchInfoRepository.findById(id).orElse(null);
    }

    @Override
    public List<MatchInfo> findAll() {
        return matchInfoRepository.findAll();
    }

    @Override
    public List<MatchInfo> findAllByUserId(String userId) {
        return matchInfoRepository.findAllByUserIdOrderByTimestampDesc(userId);
    }

    @Override
    public MatchInfo save(MatchInfo matchInfo) {
        return matchInfoRepository.save(matchInfo);
    }

    private MatchInfo getRandomMatchInfo(String userId, int finishedChance, int noReportChance) {
        Random random = new Random();

        boolean randomFinished;
        Report randomReport;

        if (userId == null){
            User u = userRepository.getRandom();
            userId = u.username;
        }

        if(random.nextInt(100) < finishedChance){
            randomFinished = true;
        } else{
            randomFinished = false;
        }

        if(random.nextInt(100) < noReportChance){
            randomReport = Report.NONE;
        } else{
            randomReport = Report.class
                    .getEnumConstants()[random.nextInt(Report.class.getEnumConstants().length - 1) + 1];
        }

        MatchInfo matchInfo = new MatchInfo(userId, randomFinished,
                "", new Date(), randomReport);

        return matchInfo;
    }
}
