package ftn.sbnz.banhammer.web.controller;

import ftn.sbnz.banhammer.service.implementation.SimulationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ScheduledFuture;

@RestController
public class MatchController {
    public static final long FIXED_RATE = 1000;

    @Autowired
    TaskScheduler taskScheduler;

    @Autowired
    SimulationServiceImpl simulationService;

    ScheduledFuture<?> scheduledFuture;

    @RequestMapping(method = RequestMethod.GET, path = "api/start")
    ResponseEntity<Void> start() {
        if(scheduledFuture != null && !scheduledFuture.isCancelled()){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        scheduledFuture = taskScheduler.scheduleAtFixedRate(
                simulationService.simulateMatchEvent(null, 90, 70), FIXED_RATE);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "api/stop")
    ResponseEntity<Void> stop() {
        try {
            scheduledFuture.cancel(false);
        }catch (NullPointerException e){}
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
