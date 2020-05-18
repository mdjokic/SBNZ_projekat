package ftn.sbnz.banhammer.repository;

import ftn.sbnz.banhammer.model.MatchInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchInfoRepository extends JpaRepository<MatchInfo, Long> {
    List<MatchInfo> findAllByUserIdOrderByTimestampDesc(String userId);

    List<MatchInfo> findTop5ByUserIdOrderByTimestampDesc(String userId);

    List<MatchInfo> findTop20ByUserIdOrderByTimestampDesc(String userId);

    @Modifying
    @Query(value = "DELETE from matches", nativeQuery = true)
    void removeAll();
}
