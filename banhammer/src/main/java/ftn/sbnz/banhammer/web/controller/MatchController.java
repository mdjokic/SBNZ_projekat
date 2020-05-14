package ftn.sbnz.banhammer.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

@RestController
public class MatchController {
    public static final long FIXED_RATE = 1000;

    @Autowired
    TaskScheduler taskScheduler;

    ScheduledFuture<?> scheduledFuture;

    @RequestMapping(method = RequestMethod.GET, path = "api/start")
    ResponseEntity<Void> start() {
        scheduledFuture = taskScheduler.scheduleAtFixedRate(printHour(), FIXED_RATE);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "api/stop")
    ResponseEntity<Void> stop() {
        scheduledFuture.cancel(false);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    private Runnable printHour() {
        return () -> System.out.println("Hello " + Instant.now().toEpochMilli());
    }

}
