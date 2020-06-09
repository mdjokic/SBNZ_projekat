package ftn.sbnz.banhammer.web.controller;

import ftn.sbnz.banhammer.model.Admin;
import ftn.sbnz.banhammer.model.DRL;
import ftn.sbnz.banhammer.service.DRLService;
import ftn.sbnz.banhammer.web.dto.DRLDto;
import ftn.sbnz.banhammer.web.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "api/drl")
public class CustomRulesController {

    @Autowired
    DRLService drlService;

    // create drl file
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody DRLDto drlDto){
        DRL drl = new DRL();
        drl.setName(drlDto.getName());
        DRL newDrl = drlService.create(drl, drlDto.getText());
        if(newDrl == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    // edit drl file
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody DRLDto drlDto){
        drlService.update(drlDto.getId(), drlDto.getText());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // delete drl file
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        drlService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // get one drl file content
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<String> getContent(@PathVariable Long id){
        DRL drl = drlService.findOne(id);
        return new ResponseEntity<>(drlService.getContent(drl), HttpStatus.OK);
    }

    // get all drl names
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DRL>> get(){
        return new ResponseEntity<>(drlService.findAll(), HttpStatus.OK);
    }
}
