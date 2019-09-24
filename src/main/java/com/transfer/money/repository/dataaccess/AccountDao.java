package com.transfer.money.repository.dataaccess;

import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.service.domain.AccountDomainObject;
import com.transfer.money.service.ServiceResponse;

public interface AccountDao {

    public ServiceResponse createNew(AccountDomainObject accountDomainObject)throws MoneyTransferException;

    public ServiceResponse getAccountDetails(long id) throws MoneyTransferException;

    public AccountDomainObject getAccountDetailsInt(long id) throws MoneyTransferException;

    public AccountDomainObject updateAccountBalance(AccountDomainObject accountDomainObject) throws MoneyTransferException;

}
