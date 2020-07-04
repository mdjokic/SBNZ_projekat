package ftn.sbnz.banhammer.unit;

import ftn.sbnz.banhammer.model.*;
import org.drools.core.time.SessionPseudoClock;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportRuleTest {

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

    private User createUser2(){
        User user = new User();
        user.setId(2L);
        user.setUsername("Alex");
        return user;
    }

    private User createUser3(){
        User user = new User();
        user.setId(3L);
        user.setUsername("Peter");
        return user;
    }

    @Test
    public void Report1Test() {
        KieSession kieSession = createKieSession();
        kieSession.getAgenda().getAgendaGroup("match_report_1").setFocus();
        SessionPseudoClock clock = kieSession.getSessionClock();
        long currentTime = clock.getCurrentTime();
        long eventTime = new Date().getTime();
        clock.advanceTime( eventTime - currentTime, TimeUnit.MILLISECONDS);
        clock.advanceTime(-95, TimeUnit.DAYS);

        //user1
        User matchUser = createUser();
        MatchInfo matchInfo11 = new MatchInfo(1L, matchUser.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(1, TimeUnit.SECONDS);
        matchInfo11.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo12 = new MatchInfo(2L, matchUser.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(4, TimeUnit.DAYS);
        matchInfo12.setTimestamp(new Date(clock.getCurrentTime()));



        MatchInfo matchInfo13 = new MatchInfo(3L, matchUser.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo13.setTimestamp(new Date(clock.getCurrentTime()));



        MatchInfo matchInfo14 = new MatchInfo(4L, matchUser.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo14.setTimestamp(new Date(clock.getCurrentTime()));



        //user2
        User matchUser2 = createUser2();
        MatchInfo matchInfo21 = new MatchInfo(5L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(1, TimeUnit.SECONDS);
        matchInfo21.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo22 = new MatchInfo(6L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo22.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo23 = new MatchInfo(7L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo23.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo24 = new MatchInfo(8L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(4, TimeUnit.DAYS);
        matchInfo24.setTimestamp(new Date(clock.getCurrentTime()));

        MatchInfo matchInfo25 = new MatchInfo(9L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(4, TimeUnit.DAYS);
        matchInfo25.setTimestamp(new Date(clock.getCurrentTime()));


        ReportUsers users = new ReportUsers();

        kieSession.insert(users);
        kieSession.insert(matchUser);
        kieSession.insert(matchUser2);

        //user 1 matches
        kieSession.insert(matchInfo11);
        kieSession.insert(matchInfo12);
        kieSession.insert(matchInfo13);
        kieSession.insert(matchInfo14);

        //user 2 matches
        kieSession.insert(matchInfo21);
        kieSession.insert(matchInfo22);
        kieSession.insert(matchInfo23);
        kieSession.insert(matchInfo24);
        kieSession.insert(matchInfo25);


        int firedRules = kieSession.fireAllRules();
        assertThat(firedRules).isEqualTo(1);
        assertThat(users.users.size()).isEqualTo(1);
    }

    @Test
    public void Report2Test() {
        KieSession kieSession = createKieSession();
        kieSession.getAgenda().getAgendaGroup("match_report_2").setFocus();
        SessionPseudoClock clock = kieSession.getSessionClock();
        long currentTime = clock.getCurrentTime();
        long eventTime = new Date().getTime();
        clock.advanceTime( eventTime - currentTime, TimeUnit.MILLISECONDS);
        clock.advanceTime(-34, TimeUnit.DAYS);

        //user1
        User matchUser = createUser();
        MatchInfo matchInfo11 = new MatchInfo(1L, matchUser.getUsername(), true, Punishment.WARNING, 5.0);
        matchInfo11.setThreatLevel(ThreatLevel.NONE);
        clock.advanceTime(1, TimeUnit.SECONDS);
        matchInfo11.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo12 = new MatchInfo(2L, matchUser.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(4, TimeUnit.DAYS);
        matchInfo12.setTimestamp(new Date(clock.getCurrentTime()));



        MatchInfo matchInfo13 = new MatchInfo(3L, matchUser.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo13.setTimestamp(new Date(clock.getCurrentTime()));



        MatchInfo matchInfo14 = new MatchInfo(4L, matchUser.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo14.setThreatLevel(ThreatLevel.HIGH);
        matchInfo14.setTimestamp(new Date(clock.getCurrentTime()));



        //user2
        User matchUser2 = createUser2();
        MatchInfo matchInfo21 = new MatchInfo(5L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        matchInfo21.setThreatLevel(ThreatLevel.NONE);
        clock.advanceTime(1, TimeUnit.SECONDS);
        matchInfo21.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo22 = new MatchInfo(6L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo22.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo23 = new MatchInfo(7L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo23.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo24 = new MatchInfo(8L, matchUser2.getUsername(), true, Punishment.NONE, 5.0);
        clock.advanceTime(4, TimeUnit.DAYS);
        matchInfo24.setTimestamp(new Date(clock.getCurrentTime()));

        MatchInfo matchInfo25 = new MatchInfo(9L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(4, TimeUnit.DAYS);
        matchInfo25.setThreatLevel(ThreatLevel.HIGH);
        matchInfo25.setTimestamp(new Date(clock.getCurrentTime()));


        ReportUsers users = new ReportUsers();

        kieSession.insert(users);
        kieSession.insert(matchUser);
        kieSession.insert(matchUser2);

        //user 1 matches
        kieSession.insert(matchInfo11);
        kieSession.insert(matchInfo12);
        kieSession.insert(matchInfo13);
        kieSession.insert(matchInfo14);

        //user 2 matches
        kieSession.insert(matchInfo21);
        kieSession.insert(matchInfo22);
        kieSession.insert(matchInfo23);
        kieSession.insert(matchInfo24);
        kieSession.insert(matchInfo25);


        int firedRules = kieSession.fireAllRules();
        assertThat(firedRules).isEqualTo(1);
        assertThat(users.users.size()).isEqualTo(1);
    }

    @Test
    public void Report3Test() {
        KieSession kieSession = createKieSession();
        kieSession.getAgenda().getAgendaGroup("match_report_3").setFocus();
        SessionPseudoClock clock = kieSession.getSessionClock();
        long currentTime = clock.getCurrentTime();
        long eventTime = new Date().getTime();
        clock.advanceTime( eventTime - currentTime, TimeUnit.MILLISECONDS);
        clock.advanceTime(-34, TimeUnit.DAYS);

        //user1
        User matchUser = createUser();
        MatchInfo matchInfo11 = new MatchInfo(1L, matchUser.getUsername(), true, Punishment.NONE, 5.0);
        matchInfo11.setThreatLevel(ThreatLevel.HIGH);
        clock.advanceTime(1, TimeUnit.SECONDS);
        matchInfo11.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo12 = new MatchInfo(2L, matchUser.getUsername(), true, Punishment.NONE, 5.0);
        clock.advanceTime(4, TimeUnit.DAYS);
        matchInfo12.setTimestamp(new Date(clock.getCurrentTime()));



        MatchInfo matchInfo13 = new MatchInfo(3L, matchUser.getUsername(), true, Punishment.NONE, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo13.setTimestamp(new Date(clock.getCurrentTime()));



        MatchInfo matchInfo14 = new MatchInfo(4L, matchUser.getUsername(), true, Punishment.NONE, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo14.setThreatLevel(ThreatLevel.NONE);
        matchInfo14.setTimestamp(new Date(clock.getCurrentTime()));



        //user2
        User matchUser2 = createUser2();
        MatchInfo matchInfo21 = new MatchInfo(5L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        matchInfo21.setThreatLevel(ThreatLevel.HIGH);
        clock.advanceTime(1, TimeUnit.SECONDS);
        matchInfo21.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo22 = new MatchInfo(6L, matchUser2.getUsername(), true, Punishment.NONE, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo22.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo23 = new MatchInfo(7L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(5, TimeUnit.DAYS);
        matchInfo23.setTimestamp(new Date(clock.getCurrentTime()));


        MatchInfo matchInfo24 = new MatchInfo(8L, matchUser2.getUsername(), true, Punishment.NONE, 5.0);
        clock.advanceTime(4, TimeUnit.DAYS);
        matchInfo24.setTimestamp(new Date(clock.getCurrentTime()));

        MatchInfo matchInfo25 = new MatchInfo(9L, matchUser2.getUsername(), true, Punishment.WARNING, 5.0);
        clock.advanceTime(4, TimeUnit.DAYS);
        matchInfo25.setThreatLevel(ThreatLevel.NONE);
        matchInfo25.setTimestamp(new Date(clock.getCurrentTime()));


        ReportUsers users = new ReportUsers();

        kieSession.insert(users);
        kieSession.insert(matchUser);
        kieSession.insert(matchUser2);

        //user 1 matches
        kieSession.insert(matchInfo11);
        kieSession.insert(matchInfo12);
        kieSession.insert(matchInfo13);
        kieSession.insert(matchInfo14);

        //user 2 matches
        kieSession.insert(matchInfo21);
        kieSession.insert(matchInfo22);
        kieSession.insert(matchInfo23);
        kieSession.insert(matchInfo24);
        kieSession.insert(matchInfo25);


        int firedRules = kieSession.fireAllRules();
        assertThat(firedRules).isEqualTo(1);
        assertThat(users.users.size()).isEqualTo(1);
    }
}
