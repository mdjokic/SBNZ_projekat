package ftn.sbnz.banhammer.service.implementation;

import ftn.sbnz.banhammer.model.*;
import ftn.sbnz.banhammer.model.match.event.*;
import ftn.sbnz.banhammer.repository.MatchInfoRepository;
import ftn.sbnz.banhammer.repository.UserRepository;
import ftn.sbnz.banhammer.service.MatchService;
import ftn.sbnz.banhammer.web.dto.MatchDTO;
import org.drools.core.time.SessionPseudoClock;
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

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MatchInfoRepository matchInfoRepository;

    @Autowired
    KieSession kieSession;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final int  matchLength = 16;

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


    @Override
    public Runnable simulateMatchEvent(String userId, int finishedChance, int noReportChance) {
        return () -> {
            MatchInfo matchInfo = new MatchInfo();
            String matchUserId = getMatchUser(userId);
            matchInfo.setUserId(matchUserId);
            User user = userRepository.findUserByUsername(matchUserId).get();

            if(user.getPunishment() == Punishment.PERMANENT_SUSPENSION){
                return;
            }
            matchInfo = matchInfoRepository.save(matchInfo);
            MatchInfo randomMatchInfo = getRandomMatchInfo(matchInfo, finishedChance, noReportChance);
            simulateMatchPlaying(matchInfo);
            kieSession.fireAllRules();

            randomMatchInfo = matchInfoRepository.save(randomMatchInfo);

            ChatLogAnalyzer  chatLogAnalyzer = new ChatLogAnalyzer();

            kieSession.insert(new MatchEvent(randomMatchInfo));
            kieSession.insert(user);
            kieSession.insert(chatLogAnalyzer);
            kieSession.fireAllRules();
            Collection<User> users = (Collection<User>) kieSession.getObjects(new ClassObjectFilter(User.class));
            Iterator<User> iter = users.iterator();
            while(iter.hasNext()){
                user = iter.next();
                if (user.getUsername().equals(randomMatchInfo.getUserId())){
                    break;
                }
            }

            MatchDTO matchDTO = new MatchDTO(randomMatchInfo, user);
            simpMessagingTemplate.convertAndSend("/topic/messages", matchDTO);

            userRepository.save(user);
            matchInfoRepository.save(randomMatchInfo);
            System.out.println();
        };
    }

    private void simulateMatchPlaying(MatchInfo matchInfo){
        long matchId = matchInfo.getId();
        System.out.println("Match simulation");
        System.out.println("====================================================");
        SessionPseudoClock clock = kieSession.getSessionClock();
        long currentTime = clock.getCurrentTime();
        long eventTime = new Date().getTime();
        clock.advanceTime( eventTime - currentTime, TimeUnit.MILLISECONDS);
        Random random = new Random();

        switch (matchInfo.report){
            case FRIENDLY_FIRE:
                if(random.nextBoolean()){
                    unprovoked_ff(matchId, clock);
                }else{
                    provoked_ff(matchId, clock);
                }
                break;
            case OFFENSIVE_LANGUAGE:
                if(random.nextBoolean()){
                    unprovoked_flame(matchId, clock);
                }else{
                    provoked_flame(matchId, clock);
                }
                break;
            case HATE_SPEECH:
                if(random.nextBoolean()){
                    unprovoked_hate(matchId, clock);
                }else{
                    clock.advanceTime(1, TimeUnit.SECONDS);
                }
                break;
        }

        System.out.println("====================================================");
        clock.advanceTime(4, TimeUnit.SECONDS);

    }

    private void provoked_ff(long matchId, SessionPseudoClock clock){
        System.out.println("Provoked Player Friendly fire");
        for (int i = 0; i < 3; i++) {
            clock.advanceTime(1, TimeUnit.SECONDS);
            kieSession.insert(new Ping(UUID.randomUUID(), matchId));
        }
        for (int i = 0; i < 2; i++) {
            clock.advanceTime(1, TimeUnit.SECONDS);
            kieSession.insert(new PlayerFriendlyFire(UUID.randomUUID(), matchId));
        }
    }

    private void provoked_flame(long matchId, SessionPseudoClock clock){
        System.out.println("Provoked Player flame");
        for (int i = 0; i < 2; i++) {
            clock.advanceTime(1, TimeUnit.SECONDS);
            kieSession.insert(new FriendlyFire(UUID.randomUUID(), matchId));
        }
        for (int i = 0; i < 2; i++) {
            clock.advanceTime(1, TimeUnit.SECONDS);
            kieSession.insert(new PlayerFlame(UUID.randomUUID(), matchId));
        }
    }

    private void unprovoked_ff(long matchId, SessionPseudoClock clock){
        System.out.println("Unprovoked Player Friendly fire");
        for (int i = 0; i < 3; i++) {
            clock.advanceTime(1, TimeUnit.SECONDS);
            kieSession.insert(new PlayerFriendlyFire(UUID.randomUUID(), matchId));
        }
    }

    private void unprovoked_hate(long matchId, SessionPseudoClock clock){
        System.out.println("Unprovoked Player hate");
        for (int i = 0; i < 2; i++) {
            clock.advanceTime(1, TimeUnit.SECONDS);
            kieSession.insert(new PlayerHate(UUID.randomUUID(), matchId));
        }
    }

    private void unprovoked_flame(long matchId, SessionPseudoClock clock){
        System.out.println("Unprovoked Player flame");
        for (int i = 0; i < 3; i++) {
            clock.advanceTime(1, TimeUnit.SECONDS);
            kieSession.insert(new PlayerFlame(UUID.randomUUID(), matchId));
        }
    }

    private String getMatchUser(String userId){
        if (userId == null){
            User u = userRepository.getRandom();
            userId = u.username;
        }else {
            Optional<User> u = userRepository.findUserByUsername(userId);
            if(u.isEmpty()){
                User u2 = userRepository.getRandom();
                userId = u2.username;
            }else {
                userId = u.get().username;
            }
        }
        return userId;
    }

    private MatchInfo getRandomMatchInfo(MatchInfo matchInfo, int finishedChance, int noReportChance) {
        Random random = new Random();

        boolean randomFinished;
        Report randomReport;

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

        matchInfo.setFinished(randomFinished);
        matchInfo.setReport(randomReport);

        matchInfo.setKdRatio(random.nextDouble() * 10);

        return matchInfo;
    }

    @Override
    @Transactional
    public void reset() {
        List<User> users = userRepository.findAll();

        for (User u: users) {
            u.setPunishment(Punishment.NONE);
            u.setThreatLevel(ThreatLevel.NONE);
            userRepository.save(u);
        }

        matchInfoRepository.removeAll();
    }


}
