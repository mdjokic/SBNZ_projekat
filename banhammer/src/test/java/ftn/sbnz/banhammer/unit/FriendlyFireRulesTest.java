package ftn.sbnz.banhammer.unit;

import ftn.sbnz.banhammer.model.*;
import ftn.sbnz.banhammer.model.match.event.FriendlyFire;
import ftn.sbnz.banhammer.model.match.event.PlayerFriendlyFire;
import org.drools.core.time.SessionPseudoClock;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FriendlyFireRulesTest {

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
    public void ReportedForFriendlyFireAnalysisTrue(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchEvent);
        kieSession.insert(matchUser);
        kieSession.fireAllRules();

        Long matchId = 2L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);

        MatchInfo matchInfo2 = new MatchInfo(matchId, matchUser.getUsername(), true, Report.FRIENDLY_FIRE);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        kieSession.insert(matchEvent2);
        kieSession.insert(matchUser);
        rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(4);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.WARNING);
    }

    @Test
    public void ReportedForFriendlyFireAnalysisFalse(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchEvent);
        kieSession.insert(matchUser);
        kieSession.fireAllRules();

        Long matchId = 2L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire f1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(f1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire f2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(f2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf.isProvoked()).isEqualTo(true);

        MatchInfo matchInfo2 = new MatchInfo(matchId, matchUser.getUsername(), true, Report.FRIENDLY_FIRE);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        kieSession.insert(matchEvent2);
        kieSession.insert(matchUser);
        rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
    }

    @Test
    public void ReportedForFriendlyFireNoReportInMatchHistory(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        Long matchId = 1L;
        MatchInfo matchInfo2 = new MatchInfo(matchId, matchUser.getUsername(), true, Report.FRIENDLY_FIRE);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        kieSession.insert(matchEvent2);
        kieSession.insert(matchUser);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(1);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
    }

    @Test
    public void LowThreatPunish(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.LOW);

        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchEvent);
        kieSession.insert(matchUser);
        kieSession.fireAllRules();

        Long matchId = 2L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);

        MatchInfo matchInfo2 = new MatchInfo(matchId, matchUser.getUsername(), true, Report.FRIENDLY_FIRE);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        kieSession.insert(matchEvent2);
        kieSession.insert(matchUser);
        rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(4);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.MEDIUM);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.SUSPENSION_3_DAYS);
    }

    @Test
    public void MediumThreatPunish(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.MEDIUM);

        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchEvent);
        kieSession.insert(matchUser);
        kieSession.fireAllRules();

        Long matchId = 2L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);

        MatchInfo matchInfo2 = new MatchInfo(matchId, matchUser.getUsername(), true, Report.FRIENDLY_FIRE);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        kieSession.insert(matchEvent2);
        kieSession.insert(matchUser);
        rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(4);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.HIGH);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.SUSPENSION_7_DAYS);
    }


    @Test
    public void HighThreatPunish(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.HIGH);

        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchEvent);
        kieSession.insert(matchUser);
        kieSession.fireAllRules();

        Long matchId = 2L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);

        MatchInfo matchInfo2 = new MatchInfo(matchId, matchUser.getUsername(), true, Report.FRIENDLY_FIRE);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        kieSession.insert(matchEvent2);
        kieSession.insert(matchUser);
        rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(4);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.HIGH);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.PERMANENT_SUSPENSION);
    }

    @Test
    public void priorityTest(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchEvent);
        kieSession.insert(matchUser);
        kieSession.fireAllRules();

        Long matchId = 2L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);

        MatchInfo matchInfo2 = new MatchInfo(matchId, matchUser.getUsername(), false, Report.FRIENDLY_FIRE);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        kieSession.insert(matchEvent2);
        kieSession.insert(matchUser);
        rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(4);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.WARNING);
    }

}
