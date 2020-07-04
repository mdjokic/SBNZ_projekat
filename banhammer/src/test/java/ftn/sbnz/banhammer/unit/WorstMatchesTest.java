package ftn.sbnz.banhammer.unit;

import ftn.sbnz.banhammer.model.*;
import org.drools.core.time.SessionPseudoClock;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WorstMatchesTest {

    private KieSession createKieSession(){
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.newKieContainer(ks.newReleaseId("sbnz.integracija", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
        KieSession kieSession = kContainer.newKieSession("Tim4KSession");
        kieSession.getAgenda().getAgendaGroup("worst_matches").setFocus();
        return kieSession;
    }

    private User createUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("John");
        return user;
    }

    @Test
    public void WorstMatches(){
        KieSession kieSession = createKieSession();
        SessionPseudoClock clock = kieSession.getSessionClock();
        User matchUser = createUser();
        MatchInfo matchInfo1 = new MatchInfo(1L, matchUser.getUsername(), true, Punishment.NONE, 5.0);
        MatchInfo matchInfo2 = new MatchInfo(2L, matchUser.getUsername(), true, Punishment.PERMANENT_SUSPENSION, 5.0);
        MatchInfo matchInfo3 = new MatchInfo(3L, matchUser.getUsername(), true, Punishment.WARNING, 5.0);
        Matches matches = new Matches();

        clock.advanceTime(1, TimeUnit.SECONDS);
        matchInfo1.setTimestamp(new Date(clock.getCurrentTime()));
        kieSession.insert(matchInfo1);
        clock.advanceTime(1, TimeUnit.SECONDS);
        matchInfo2.setTimestamp(new Date(clock.getCurrentTime()));
        kieSession.insert(matchInfo2);
        clock.advanceTime(1, TimeUnit.SECONDS);
        matchInfo3.setTimestamp(new Date(clock.getCurrentTime()));
        kieSession.insert(matchInfo3);
        kieSession.insert(matches);
        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(3);
        assertThat(matches.matchesList.size()).isEqualTo(3);
        assertThat(matches.matchesList.get(0).getId()).isEqualTo(2L);
        assertThat(matches.matchesList.get(1).getId()).isEqualTo(3L);
        assertThat(matches.matchesList.get(2).getId()).isEqualTo(1L);
    }
}
