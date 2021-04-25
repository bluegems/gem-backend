package com.bluegems.server.gembackend.repository;

import com.bluegems.server.gembackend.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findByEmail(String email);
}
