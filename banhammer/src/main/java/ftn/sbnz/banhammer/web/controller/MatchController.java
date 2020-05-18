package ftn.sbnz.banhammer.web.controller;

import ftn.sbnz.banhammer.model.MatchInfo;
import ftn.sbnz.banhammer.service.MatchService;
import ftn.sbnz.banhammer.service.implementation.MatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin()
public class MatchController {

    public static final long FIXED_RATE = 3000;

    @Autowired
    MatchService matchService;

    @Qualifier("threadPoolTaskScheduler")
    @Autowired
    TaskScheduler taskScheduler;

    @Autowired
    MatchServiceImpl simulationService;

    ScheduledFuture<?> scheduledFuture;

    @MessageMapping("/start")
    ResponseEntity<Void> start() {
        if(scheduledFuture != null && !scheduledFuture.isCancelled()){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        scheduledFuture = taskScheduler.scheduleAtFixedRate(
                simulationService.simulateMatchEvent(null, 90, 50), FIXED_RATE);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @MessageMapping("/stop")
    @SendTo("/topic/messages")
    ResponseEntity<Void> stop() {
        try {
            scheduledFuture.cancel(false);
        }catch (NullPointerException e){}
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping("/matches")
    public List<MatchInfo> tenLatestMatches(){
        return matchService.findLatest();
    }

}
