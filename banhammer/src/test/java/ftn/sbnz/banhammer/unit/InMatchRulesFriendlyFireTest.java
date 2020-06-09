package ftn.sbnz.banhammer.unit;

import ftn.sbnz.banhammer.model.match.event.FriendlyFire;
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

public class InMatchRulesFriendlyFireTest {

    private KieSession createKieSession(){
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.newKieContainer(ks.newReleaseId("sbnz.integracija", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
        return kContainer.newKieSession("Tim4KSession");
    }

    @Test
    public void TwoFriendlyFiresAndOnePlayerFire() {
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf.isProvoked()).isEqualTo(true);
    }

    @Test
    public void TwoFriendlyFiresAndOnePlayerFlame() {
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf.isProvoked()).isEqualTo(true);
    }

    @Test
    public void BadIntervalBetweenTwoFriendlyFires() {
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(6, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);
    }


    @Test
    public void BadIntervalBetweenFriendlyFireAndPlayerFriendlyFlame(){
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(5, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);
    }

    @Test
    public void TwoFriendlyFireAndTwoPlayerFlames(){
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf1 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf2 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf1.isProvoked()).isEqualTo(true);
        assertThat(pf2.isProvoked()).isEqualTo(true);
    }

    @Test
    public void TwoFriendlyFireAndTwoPlayerFriendlyFires(){
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf1 = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf2 = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf1.isProvoked()).isEqualTo(true);
        assertThat(pf2.isProvoked()).isEqualTo(true);
    }

    @Test
    public void TwoFriendlyFireAndPlayerFriendlyAndPlayerFlame(){
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf1 = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf2 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf1.isProvoked()).isEqualTo(true);
        assertThat(pf2.isProvoked()).isEqualTo(true);
    }

    @Test
    public void TwoFriendlyFireAndPlayerFlameAndPlayerFriendlyFire(){
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf1 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf2 = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf1.isProvoked()).isEqualTo(true);
        assertThat(pf2.isProvoked()).isEqualTo(true);
    }

    @Test
    public void TwoFriendlyFireAndThreePlayerFlames(){
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf1 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf2 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf3= new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf3);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf1.isProvoked()).isEqualTo(true);
        assertThat(pf2.isProvoked()).isEqualTo(true);
        assertThat(pf3.isProvoked()).isEqualTo(false);
    }

    @Test
    public void TwoFriendlyFireAndThreePlayerFriendlyFires(){
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf1 = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf2 = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf3= new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf3);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf1.isProvoked()).isEqualTo(true);
        assertThat(pf2.isProvoked()).isEqualTo(true);
        assertThat(pf3.isProvoked()).isEqualTo(false);
    }

    @Test
    public void FriendlyFirePlayerFlameFriendlyFirePlayerFlame(){
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff1 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf1 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf1);

        clock.advanceTime(1, TimeUnit.SECONDS);
        FriendlyFire ff2 = new FriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(ff2);

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf2 = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf2);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf1.isProvoked()).isEqualTo(false);
        assertThat(pf2.isProvoked()).isEqualTo(true);
    }


    @Test
    public void OnePlayerFlame() {
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFlame pf = new PlayerFlame(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);
    }

    @Test
    public void OnePlayerFriendlyFire() {
        KieSession kieSession = createKieSession();
        Long matchId = 1L;
        SessionPseudoClock clock = kieSession.getSessionClock();

        clock.advanceTime(1, TimeUnit.SECONDS);
        PlayerFriendlyFire pf = new PlayerFriendlyFire(UUID.randomUUID(), matchId);
        kieSession.insert(pf);

        clock.advanceTime(4, TimeUnit.SECONDS);

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(0);
        assertThat(pf.isProvoked()).isEqualTo(false);
    }
}
