package ftn.sbnz.banhammer.web.controller;

import ftn.sbnz.banhammer.service.implementation.SimulationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping(value = "/api")
public class MatchController {
    public static final long FIXED_RATE = 1000;

    @Autowired
    TaskScheduler taskScheduler;

    @Autowired
    SimulationServiceImpl simulationService;

    ScheduledFuture<?> scheduledFuture;

    @RequestMapping(method = RequestMethod.GET, path = "/start")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> start() {
        if(scheduledFuture != null && !scheduledFuture.isCancelled()){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        scheduledFuture = taskScheduler.scheduleAtFixedRate(
                simulationService.simulateMatchEvent(null, 90, 70), FIXED_RATE);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/stop")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> stop() {
        try {
            scheduledFuture.cancel(false);
        }catch (NullPointerException e){}
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
