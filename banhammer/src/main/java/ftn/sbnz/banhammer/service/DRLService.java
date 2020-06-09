package ftn.sbnz.banhammer.service;

import ftn.sbnz.banhammer.model.DRL;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DRLService {
    DRL create(DRL drl, String text);

    List<DRL> findAll();

    DRL save(DRL drl);

    void update(Long id, String text);

    DRL findOne(Long id);

    String getContent(DRL drl);

    void delete(Long id);
}
