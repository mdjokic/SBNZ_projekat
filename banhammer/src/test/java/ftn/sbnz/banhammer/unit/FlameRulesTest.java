package ftn.sbnz.banhammer.unit;

import ftn.sbnz.banhammer.model.*;
import ftn.sbnz.banhammer.model.match.event.FriendlyFire;
import ftn.sbnz.banhammer.model.match.event.Ping;
import ftn.sbnz.banhammer.model.match.event.PlayerFlame;
import ftn.sbnz.banhammer.model.match.event.PlayerFriendlyFire;
import org.drools.core.time.SessionPseudoClock;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FlameRulesTest {
    private KieSession createKieSession(){
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.newKieContainer(ks.newReleaseId("sbnz.integracija", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
        return kContainer.newKieSession("Tim4KSession");
    }

    private User createUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("John");
        return user;
    }

    @Test
    public void noReportsForFlame(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }

    @Test
    public void reportForFlameEmptyMatchHistory(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), true, Report.OFFENSIVE_LANGUAGE);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }

    @Test
    public void reportForFlameGoodMatchHistory(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.OFFENSIVE_LANGUAGE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        kieSession.fireAllRules();

        // insert third match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent3);
        kieSession.fireAllRules();

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }

    @Test
    public void reportForFlameAndReportInLastFiveMatchesAndChatLogTrue(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.OFFENSIVE_LANGUAGE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        kieSession.fireAllRules();

        // insert third match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent3);
        kieSession.fireAllRules();

        Long matchId = 4L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf2 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);

        clock.advanceTime(4, TimeUnit.SECONDS);

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated2 = kieSession.fireAllRules();
        assertThat(rulesActivated2).isEqualTo(5);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.WARNING);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
    }

    @Test
    public void reportForFlameAndReportInLastFiveMatchesAndChatLogFalse1Uprovoked(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.OFFENSIVE_LANGUAGE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert chat log analysis
        ChatLogAnalyzer chatLogAnalyzer = new ChatLogAnalyzer();
        chatLogAnalyzer.setFlame(true);
        kieSession.insert(chatLogAnalyzer);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        kieSession.fireAllRules();

        // insert third match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent3);
        kieSession.fireAllRules();

        Long matchId = 4L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf2 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);

        clock.advanceTime(4, TimeUnit.SECONDS);

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated2 = kieSession.fireAllRules();
        assertThat(rulesActivated2).isEqualTo(5);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.WARNING);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
    }

    @Test
    public void reportForFlameAndReportInLastFiveMatchesAndChatLogFalseProvokedFriendlyFire(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.OFFENSIVE_LANGUAGE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert chat log analysis
        ChatLogAnalyzer chatLogAnalyzer = new ChatLogAnalyzer();
        chatLogAnalyzer.setFlame(false);
        kieSession.insert(chatLogAnalyzer);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        kieSession.fireAllRules();

        // insert third match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent3);
        kieSession.fireAllRules();

        Long matchId = 4L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire f1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(f1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire f2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(f2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf2 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf.isProvoked()).isEqualTo(true);

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated2 = kieSession.fireAllRules();
        assertThat(rulesActivated2).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }

    @Test
    public void reportForFlameAndReportInLastFiveMatchesAndChatLogFalseProvokedPings(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.OFFENSIVE_LANGUAGE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert chat log analysis
        ChatLogAnalyzer chatLogAnalyzer = new ChatLogAnalyzer();
        chatLogAnalyzer.setFlame(false);
        kieSession.insert(chatLogAnalyzer);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        kieSession.fireAllRules();

        // insert third match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent3);
        kieSession.fireAllRules();

        Long matchId = 4L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        Ping p1 = new Ping(UUID.randomUUID(), matchId);
        kieSession.insert(p1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        Ping p2 = new Ping(UUID.randomUUID(), matchId);
        kieSession.insert(p2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        Ping p3 = new Ping(UUID.randomUUID(), matchId);
        kieSession.insert(p3);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf2 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf.isProvoked()).isEqualTo(true);

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated2 = kieSession.fireAllRules();
        assertThat(rulesActivated2).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }

    @Test
    public void reportForFlameAndReportInLastFiveMatchesAndChatLogFalse1Unprovoked(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.OFFENSIVE_LANGUAGE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert chat log analysis
        ChatLogAnalyzer chatLogAnalyzer = new ChatLogAnalyzer();
        chatLogAnalyzer.setFlame(false);
        kieSession.insert(chatLogAnalyzer);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        kieSession.fireAllRules();

        // insert third match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent3);
        kieSession.fireAllRules();

        Long matchId = 4L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);

        clock.advanceTime(4, TimeUnit.SECONDS);

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated2 = kieSession.fireAllRules();
        assertThat(rulesActivated2).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }

    @Test
    public void increasingThreatLevelTest(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.MEDIUM);

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.OFFENSIVE_LANGUAGE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert chat log analysis
        ChatLogAnalyzer chatLogAnalyzer = new ChatLogAnalyzer();
        chatLogAnalyzer.setFlame(true);
        kieSession.insert(chatLogAnalyzer);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        kieSession.fireAllRules();

        // insert third match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent3);
        kieSession.fireAllRules();

        Long matchId = 4L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf2 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);

        clock.advanceTime(4, TimeUnit.SECONDS);

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated2 = kieSession.fireAllRules();
        assertThat(rulesActivated2).isEqualTo(5);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.CHAT_BAN_7_DAYS);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.HIGH);
    }

    @Test
    public void salienceTestWithConflictingAfk(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.LOW);

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), false, Report.OFFENSIVE_LANGUAGE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert chat log analysis
        ChatLogAnalyzer chatLogAnalyzer = new ChatLogAnalyzer();
        chatLogAnalyzer.setFlame(true);
        kieSession.insert(chatLogAnalyzer);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        kieSession.fireAllRules();

        // insert third match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent3);
        kieSession.fireAllRules();

        Long matchId = 4L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf2 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);

        clock.advanceTime(4, TimeUnit.SECONDS);

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated2 = kieSession.fireAllRules();
        assertThat(rulesActivated2).isEqualTo(5);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.CHAT_BAN_3_DAYS);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.MEDIUM);
    }
}
