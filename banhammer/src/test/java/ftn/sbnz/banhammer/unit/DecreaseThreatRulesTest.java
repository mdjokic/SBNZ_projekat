package ftn.sbnz.banhammer.unit;

import ftn.sbnz.banhammer.model.*;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DecreaseThreatRulesTest {

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
    public void HighThreatDecrease(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.HIGH);
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo5 = new MatchInfo(5L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo6 = new MatchInfo(6L, matchUser.getUsername(), true, Report.NONE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);
        MatchEvent matchEvent5 = new MatchEvent(matchInfo5);
        MatchEvent matchEvent6 = new MatchEvent(matchInfo6);

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

        // insert fifth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent5);
        kieSession.fireAllRules();

        // insert sixth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent6);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.MEDIUM);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
    }

    @Test
    public void MediumThreatDecrease(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.MEDIUM);
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo5 = new MatchInfo(5L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo6 = new MatchInfo(6L, matchUser.getUsername(), true, Report.NONE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);
        MatchEvent matchEvent5 = new MatchEvent(matchInfo5);
        MatchEvent matchEvent6 = new MatchEvent(matchInfo6);

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

        // insert fifth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent5);
        kieSession.fireAllRules();

        // insert sixth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent6);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
    }

    @Test
    public void LowThreatDecrease(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.MEDIUM);
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo5 = new MatchInfo(5L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo6 = new MatchInfo(6L, matchUser.getUsername(), true, Report.NONE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);
        MatchEvent matchEvent5 = new MatchEvent(matchInfo5);
        MatchEvent matchEvent6 = new MatchEvent(matchInfo6);


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

        // insert fifth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent5);
        kieSession.fireAllRules();

        // insert sixth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent6);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
    }

    @Test
    public void NoneThreatDecrease(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.LOW);
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo5 = new MatchInfo(5L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo6 = new MatchInfo(6L, matchUser.getUsername(), true, Report.NONE);

        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);
        MatchEvent matchEvent5 = new MatchEvent(matchInfo5);
        MatchEvent matchEvent6 = new MatchEvent(matchInfo6);

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

        // insert fifth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent5);
        kieSession.fireAllRules();

        // insert sixth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent6);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
    }

    @Test
    public void NoThreatDecreaseReportInMatchHistory() {
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.HIGH);
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchInfo matchInfo5 = new MatchInfo(5L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo6 = new MatchInfo(6L, matchUser.getUsername(), true, Report.NONE);

        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);
        MatchEvent matchEvent5 = new MatchEvent(matchInfo5);
        MatchEvent matchEvent6 = new MatchEvent(matchInfo6);

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
        kieSession.fireAllRules();

        // insert fifth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent5);
        kieSession.fireAllRules();

        // insert sixth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent6);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(1);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.HIGH);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
    }

    @Test
    public void NoThreatDecreaseFourMatchesPlayed(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.HIGH);
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo5 = new MatchInfo(5L, matchUser.getUsername(), true, Report.NONE);

        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);
        MatchEvent matchEvent5 = new MatchEvent(matchInfo5);

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
        kieSession.fireAllRules();

        // insert fifth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent5);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(1);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.HIGH);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
    }

    @Test
    public void DecreasingLowestThreat() {
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.NONE);
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo5 = new MatchInfo(5L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo6 = new MatchInfo(6L, matchUser.getUsername(), true, Report.NONE);

        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);
        MatchEvent matchEvent5 = new MatchEvent(matchInfo5);
        MatchEvent matchEvent6 = new MatchEvent(matchInfo6);

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
        kieSession.fireAllRules();

        // insert fifth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent5);
        kieSession.fireAllRules();

        // insert sixth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent6);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
    }
}
