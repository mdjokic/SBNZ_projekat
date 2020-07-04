package ftn.sbnz.banhammer.unit;

import ftn.sbnz.banhammer.model.*;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FeedingRulesTest {
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
    public void FeedingDetectedTest(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE, 10.0);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE, 10.0);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE, 2.0);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);

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
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchEvent3.isFeedingHandled()).isTrue();
        assertThat(matchInfo3.isPotentialFeeding()).isTrue();
    }

    @Test
    public void FeedingFalseTest(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE, 10.0);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE, 10.0);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Report.NONE, 11.0);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);
        MatchEvent matchEvent2 = new MatchEvent(matchInfo2);
        MatchEvent matchEvent3 = new MatchEvent(matchInfo3);

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
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchEvent3.isFeedingHandled()).isTrue();
        assertThat(matchInfo3.isPotentialFeeding()).isFalse();
    }

    @Test
    public void OneMatchKDTest(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE, 10.0);
        MatchEvent matchEvent1 = new MatchEvent(matchInfo1);

        // insert first match
        kieSession.insert(matchUser);
        kieSession.insert(matchEvent1);
        int rulesActivated = kieSession.fireAllRules();

        assertThat(rulesActivated).isEqualTo(1);
        assertThat(matchEvent1.isFeedingHandled()).isTrue();
        assertThat(matchInfo1.isPotentialFeeding()).isFalse();
    }

    @Test
    public void TwoMatchKDTest(){
        KieSession kieSession = createKieSession();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Report.NONE, 10.0);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Report.NONE, 10.0);
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

        assertThat(rulesActivated).isEqualTo(2);
        assertThat(matchEvent2.isFeedingHandled()).isTrue();
        assertThat(matchInfo2.isPotentialFeeding()).isFalse();
    }

}
