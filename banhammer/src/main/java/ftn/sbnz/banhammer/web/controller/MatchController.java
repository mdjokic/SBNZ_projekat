package ftn.sbnz.banhammer.web.controller;

import ftn.sbnz.banhammer.model.MatchInfo;
import ftn.sbnz.banhammer.model.User;
import ftn.sbnz.banhammer.service.MatchService;
import ftn.sbnz.banhammer.service.UserService;
import ftn.sbnz.banhammer.service.implementation.MatchServiceImpl;
import ftn.sbnz.banhammer.web.dto.MatchDTO;
import ftn.sbnz.banhammer.web.dto.SimulationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin()
public class MatchController {


    @Autowired
    MatchService matchService;

    @Qualifier("threadPoolTaskScheduler")
    @Autowired
    TaskScheduler taskScheduler;

    @Autowired
    UserService userService;

    @Autowired
    MatchServiceImpl simulationService;

    ScheduledFuture<?> scheduledFuture;

    @MessageMapping("/start")
    ResponseEntity<Void> start(SimulationDTO simulationDTO) {
        if(scheduledFuture != null && !scheduledFuture.isCancelled()){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        scheduledFuture = taskScheduler.scheduleAtFixedRate(
                simulationService.simulateMatchEvent(simulationDTO.getUserId(), simulationDTO.getFinishedChance(), simulationDTO.getNoReportChance()), simulationDTO.getIntervalBetweenMatches());
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
    public List<MatchDTO> tenLatestMatches() {
        List<MatchInfo> latestMatches =  matchService.findLatest();
        List<MatchDTO> latestMatchesDTO = new ArrayList<>();
        User user = null;
        for (MatchInfo matchInfo: latestMatches) {
            user = userService.findOne(matchInfo.getUserId());
            latestMatchesDTO.add(new MatchDTO(matchInfo, user));

        }
        return latestMatchesDTO;
    }

}
