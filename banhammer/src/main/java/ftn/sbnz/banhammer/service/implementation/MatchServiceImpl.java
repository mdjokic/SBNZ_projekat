package ftn.sbnz.banhammer.service.implementation;

import ftn.sbnz.banhammer.model.*;
import ftn.sbnz.banhammer.repository.MatchInfoRepository;
import ftn.sbnz.banhammer.repository.UserRepository;
import ftn.sbnz.banhammer.service.MatchService;
import ftn.sbnz.banhammer.web.dto.MatchDTO;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MatchInfoRepository matchInfoRepository;

    @Autowired
    KieContainer kieContainer;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;


    @Override
    public Runnable simulateMatchEvent(String userId, int finishedChance, int noReportChance) {
        return () -> {
            KieSession kieSession = kieContainer.newKieSession();
            MatchInfo randomMatchInfo = getRandomMatchInfo(userId, finishedChance, noReportChance);
            randomMatchInfo = matchInfoRepository.save(randomMatchInfo);
            User user = userRepository.findUserByUsername(randomMatchInfo.getUserId()).get();

            if(user.getPunishment() == Punishment.PERMANENT_SUSPENSION){
                return;
            }

            ChatLogAnalyzer  chatLogAnalyzer = new ChatLogAnalyzer();

            MatchHistory matchHistory = new MatchHistory(
                    (ArrayList) matchInfoRepository.findTop5ByUserIdOrderByTimestampDesc(user.username));

            kieSession.insert(new MatchEvent(randomMatchInfo));
            kieSession.insert(user);
            kieSession.insert(matchHistory);
            kieSession.insert(chatLogAnalyzer);
            kieSession.fireAllRules();
            Collection<User> users = (Collection<User>) kieSession.getObjects(new ClassObjectFilter(User.class));
            user = users.iterator().next();

            MatchDTO matchDTO = new MatchDTO(randomMatchInfo, user);
            simpMessagingTemplate.convertAndSend("/topic/messages", matchDTO);

            userRepository.save(user);
            matchInfoRepository.save(randomMatchInfo);
            System.out.println();
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
    public List<MatchInfo> findLatest(){
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "timestamp");
        Page<MatchInfo> bottomPage = matchInfoRepository.findAll(pageable);
        return bottomPage.getContent();
    }

    @Override
    public List<MatchInfo> findAllByUserId(String userId) {
        return matchInfoRepository.findTop20ByUserIdOrderByTimestampDesc(userId);
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
