package com.bluegems.server.gembackend.graphql.resolver;


import com.bluegems.server.gembackend.dao.AccountDao;
import com.bluegems.server.gembackend.entity.AccountEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.Account;
import com.bluegems.server.gembackend.graphql.utils.EntityToModel;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class AccountResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private AccountDao accountDao;

    @PreAuthorize("isAuthenticated()")
    public Account getAccount(UUID id) {
        try {
            return EntityToModel.fromAccountEntity(accountDao.findAccountById(id));
        } catch (Exception exception) {
            log.error("Failed to fetch account details", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching account details");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public Account updateAccount(String email, String password) {
        try {
            AccountEntity accountEntity = accountDao.updateAccount(email, password);
            return EntityToModel.fromAccountEntity(accountEntity);
        } catch (Exception exception) {
            log.error("Failed to update account", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while updating account");
        }
    }
}
