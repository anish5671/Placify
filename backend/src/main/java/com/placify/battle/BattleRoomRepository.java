package com.placify.battle;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface BattleRoomRepository extends JpaRepository<BattleRoom, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from BattleRoom r where r.code = :code")
    Optional<BattleRoom> findByCodeForUpdate(@Param("code") String code);

    Optional<BattleRoom> findByCode(String code);
}
