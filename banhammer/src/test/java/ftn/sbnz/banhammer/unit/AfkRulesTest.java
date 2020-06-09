package ftn.sbnz.banhammer.unit;

import ftn.sbnz.banhammer.model.*;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class AfkRulesTest {


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

    private User createSecondUser(){
        User user = new User();
        user.setId(2L);
        user.setUsername("Jane");
        return user;
    }

    @Test
    public void matchFinishedAndNoReportsForLeaving(){
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
    public void matchNotFinishedAndNoReportsForLeaving(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), false, Report.NONE);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }

    @Test
    public void matchFinishedAndReportForLeaving(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }

    @Test
    public void matchFinishedAndReportsForLeavingAndReportInLastFiveMatches(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.HATE_SPEECH);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
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
        assertThat(rulesActivated).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.WARNING);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
    }

    @Test
    public void matchNotFinishedAndNoReportsForLeavingAndReportInLastFiveMatches(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.HATE_SPEECH);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), false, Report.NONE);
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
        assertThat(rulesActivated).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.WARNING);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
    }

    @Test
    public void matchNotFinishedAndReportsForLeavingAndReportInLastFiveMatches(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.HATE_SPEECH);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), false, Report.LEAVING_THE_GAME);
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
        assertThat(rulesActivated).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.WARNING);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
    }

    @Test
    public void matchFinishedAndReportsForLeavingAndOldReport(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.HATE_SPEECH);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo5 = new MatchInfo(5L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo6 = new MatchInfo(6L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo7 = new MatchInfo(7L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);
        MatchEvent matchEvent5 = new MatchEvent(matchInfo5);
        MatchEvent matchEvent6 = new MatchEvent(matchInfo6);
        MatchEvent matchEvent7 = new MatchEvent(matchInfo7);


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
        kieSession.fireAllRules();

        // insert seventh match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent7);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }
    @Test
    public void ReportForLeavingAndLeavingInMatchHistory() {
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), false, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.WARNING);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
    }

    @Test
    public void MatchNotFinishedAndLeavingInMatchHistory() {
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), false, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), false, Report.NONE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.WARNING);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
    }

    @Test
    public void LowThreatPunishForLeaving(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.LOW);
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), false, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), false, Report.NONE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.SUSPENSION_3_DAYS);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.MEDIUM);
    }

    @Test
    public void MediumThreatPunishForLeaving(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.MEDIUM);
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), false, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), false, Report.NONE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.SUSPENSION_7_DAYS);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.HIGH);
    }

    @Test
    public void HighThreatPunishForLeaving(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        matchUser.setThreatLevel(ThreatLevel.HIGH);
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), false, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), false, Report.NONE);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent2);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(3);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.PERMANENT_SUSPENSION);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.HIGH);
    }
    @Test
    public void ReportForLeavingMatchHistoryTest(){
        KieSession kieSession = createKieSession();
        User firstMatchUser = createUser(); // John
        User secondMatchUser = createSecondUser(); // Jane

        MatchInfo matchInfo1 = new MatchInfo(1L, firstMatchUser.getUsername(), false, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, secondMatchUser.getUsername(), false, Report.LEAVING_THE_GAME);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);

        // insert first match
        kieSession.insert(firstMatchUser);
        kieSession.insert(matchEvent1);
        kieSession.fireAllRules();

        // insert second match
        kieSession.insert(secondMatchUser);
        kieSession.insert(matchEvent2);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(1);
        assertThat(secondMatchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(secondMatchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }
}
