package ftn.sbnz.banhammer;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
public class BanhammerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanhammerApplication.class, args);
	}

	@Bean
	public KieSession kieSession() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.newKieContainer(ks.newReleaseId("sbnz.integracija", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10_000);
		return kContainer.newKieSession("Tim4KSession");
	}

	@Bean
	TaskScheduler threadPoolTaskScheduler() {
		return new ThreadPoolTaskScheduler();
	}


}
