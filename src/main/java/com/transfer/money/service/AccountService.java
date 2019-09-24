package com.transfer.money.service;

import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.service.domain.AccountDomainObject;

public interface AccountService {

    public ServiceResponse createNewSync(AccountDomainObject accountDomainObject) throws MoneyTransferException;

    public ServiceResponse getAccountDetailsSync(long id) throws MoneyTransferException;

    public AccountDomainObject getAccountDetailsInt(long id)throws MoneyTransferException;

    public AccountDomainObject updateAccountDetailsForBalance(AccountDomainObject accountDomainObject)
            throws MoneyTransferException;

    public ServiceResponse updateAccountDetailsForBalanceSync(AccountDomainObject accountDomainObject)
            throws MoneyTransferException;

}
