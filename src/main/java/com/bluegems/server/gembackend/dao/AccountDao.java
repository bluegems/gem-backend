package com.bluegems.server.gembackend.dao;

import com.bluegems.server.gembackend.entity.AccountEntity;
import com.bluegems.server.gembackend.exception.graphql.GemGraphQLErrorExtensions;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Component
public class AccountDao {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AccountEntity findAccountById(UUID accountId) {
        Optional<AccountEntity> foundAccount = accountRepository.findById(accountId);
        if (foundAccount.isEmpty()) {
            log.warn("Could not find account with ID : {}", accountId);
            throw new ThrowableGemGraphQLException(
                    "Account with the specified ID not found",
                    GemGraphQLErrorExtensions.builder().invalidField("accountId").build()
            );
        }
        return foundAccount.get();
    }

    public AccountEntity findAccountByEmail(String email) {
        Optional<AccountEntity> foundAccount = accountRepository.findByEmail(email);
        if (foundAccount.isEmpty()) {
            log.warn("Could not find account with email : {}", email);
            throw new ThrowableGemGraphQLException(
                    "Account with the specified email not found",
                    GemGraphQLErrorExtensions.builder().invalidField("email").build()
            );
        }
        return foundAccount.get();
    }

    public AccountEntity createAccount(String email, String password) {
        AccountEntity accountEntity = AccountEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        return accountRepository.saveAndFlush(accountEntity);
    }

    public AccountEntity updateAccount(String email, String password) {
        Optional<AccountEntity> foundAccount = accountRepository.findByEmail(email);
        if (foundAccount.isEmpty()) {
            log.warn("Could not find account with email : {}", email);
            throw new ThrowableGemGraphQLException(
                    "Account with the specified email not found",
                    GemGraphQLErrorExtensions.builder().invalidField("email").build()
            );
        }
        AccountEntity accountEntity = foundAccount.get();
        accountEntity.setPassword(password);
        return accountRepository.saveAndFlush(accountEntity);
    }
}
