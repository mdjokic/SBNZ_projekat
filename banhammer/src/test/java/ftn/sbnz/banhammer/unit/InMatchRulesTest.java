package ftn.sbnz.banhammer.unit;

import ftn.sbnz.banhammer.model.User;
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

public class InMatchRulesTest {

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

        int rulesActivated = kieSession.fireAllRules();
        assertThat(rulesActivated).isEqualTo(1);
        assertThat(pf.isProvoked()).isEqualTo(true);
    }
}
