package ftn.sbnz.banhammer.unit;

import ftn.sbnz.banhammer.model.*;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HateRulesTest {
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
    public void noReportsForHate(){
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
    public void reportForHateEmptyMatchHistory(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo = new MatchInfo(1L, matchUser.getUsername(), true, Report.HATE_SPEECH);
        MatchEvent matchEvent = new MatchEvent(matchInfo);
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }

    @Test
    public void reportForHateGoodMatchHistory(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.HATE_SPEECH);
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
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.NONE);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.NONE);
    }

    @Test
    public void reportForHateAndReportInLastFiveMatchesAndChatLogTrue(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.HATE_SPEECH);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert chat log analysis
        ChatLogAnalyzer chatLogAnalyzer = new ChatLogAnalyzer();
        chatLogAnalyzer.setHate(true);
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

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(4);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.SUSPENSION_7_DAYS);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.LOW);
    }

    @Test
    public void reportForFlameAndReportInLastFiveMatchesAndChatLogFalse(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();

        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.LEAVING_THE_GAME);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE);
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.HATE_SPEECH);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert chat log analysis
        ChatLogAnalyzer chatLogAnalyzer = new ChatLogAnalyzer();
        chatLogAnalyzer.setHate(false);
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

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(2);
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
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), true, Report.HATE_SPEECH);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert chat log analysis
        ChatLogAnalyzer chatLogAnalyzer = new ChatLogAnalyzer();
        chatLogAnalyzer.setHate(true);
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

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(4);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.PERMANENT_SUSPENSION);
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
        MatchInfo matchInfo4 = new MatchInfo(4L, matchUser.getUsername(), false, Report.HATE_SPEECH);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);
        MatchEvent matchEvent4 = new MatchEvent(matchInfo4);

        // insert chat log analysis
        ChatLogAnalyzer chatLogAnalyzer = new ChatLogAnalyzer();
        chatLogAnalyzer.setHate(true);
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

        // insert fourth match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent4);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(4);
        assertThat(matchUser.getPunishment()).isEqualTo(Punishment.PERMANENT_SUSPENSION);
        assertThat(matchUser.getThreatLevel()).isEqualTo(ThreatLevel.MEDIUM);
    }
}
