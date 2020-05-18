package ftn.sbnz.banhammer.repository;

import ftn.sbnz.banhammer.model.MatchInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {
    List<MatchInfo> findAllByUserIdOrderByTimestampDesc(String userId);

    List<MatchInfo> findTop5ByUserIdOrderByTimestampDesc(String userId);

    List<MatchInfo> findTop20ByUserIdOrderByTimestampDesc(String userId);
}
