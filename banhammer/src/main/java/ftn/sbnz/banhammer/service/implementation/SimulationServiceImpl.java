package ftn.sbnz.banhammer.service.implementation;

import ftn.sbnz.banhammer.model.MatchInfo;
import ftn.sbnz.banhammer.model.Report;
import ftn.sbnz.banhammer.service.SimulationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@Service
public class SimulationServiceImpl implements SimulationService {



    @Override
    public Runnable simulateMatchEvent(String userId, int finishedChance, int noReportChance) {
        return () -> {
            System.out.println(getRandomMatchInfo(userId, finishedChance, noReportChance));
            System.out.println();
        };
    }

    private MatchInfo getRandomMatchInfo(String userId, int finishedChance, int noReportChance) {
        Random random = new Random();

        boolean randomFinished;
        Report randomReport;

        if (userId == null){
            userId = "1"; // TODO: calculate this randomly
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
                new ArrayList<>(), new Date(), randomReport);

        return matchInfo;
    }
}
