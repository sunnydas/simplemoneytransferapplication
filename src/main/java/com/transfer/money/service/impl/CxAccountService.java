package com.transfer.money.service.impl;

import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.locks.LockManager;
import com.transfer.money.repository.dataaccess.AccountDao;
import com.transfer.money.repository.dataaccess.factory.MoneyTFAccountDaoFactory;
import com.transfer.money.repository.dataaccess.factory.MoneyTFDAOType;
import com.transfer.money.service.domain.AccountDomainObject;
import com.transfer.money.service.AccountService;
import com.transfer.money.service.ServiceResponse;

public class CxAccountService implements AccountService {

    @Override
    public ServiceResponse createNewSync(AccountDomainObject accountDomainObject) throws MoneyTransferException {
        AccountDao accountDao = MoneyTFAccountDaoFactory.getDAO(MoneyTFDAOType.ACCOUNT);
        try {
            lockAccountTx();
            accountDao.createNew(accountDomainObject);
            return accountDomainObject;
        }finally {
            unlockAccountTx();
        }
    }

    @Override
    public ServiceResponse getAccountDetailsSync(long id) throws MoneyTransferException {
        AccountDao accountDao = MoneyTFAccountDaoFactory.getDAO(MoneyTFDAOType.ACCOUNT);
        try {
            lockAccountTx();
            return accountDao.getAccountDetails(id);
        }finally {
            unlockAccountTx();
        }
    }

    public void unlockAccountTx() {
        LockManager.getLockManager().unlockAccountTx();
    }

    @Override
    public AccountDomainObject getAccountDetailsInt(long id) throws MoneyTransferException {
        AccountDao accountDao = MoneyTFAccountDaoFactory.getDAO(MoneyTFDAOType.ACCOUNT);
        return accountDao.getAccountDetailsInt(id);
    }

    @Override
    public AccountDomainObject updateAccountDetailsForBalance(AccountDomainObject accountDomainObject) throws MoneyTransferException {
        AccountDao accountDao = MoneyTFAccountDaoFactory.getDAO(MoneyTFDAOType.ACCOUNT);
        accountDao.updateAccountBalance(accountDomainObject);
        return accountDomainObject;
    }

    @Override
    public ServiceResponse updateAccountDetailsForBalanceSync(AccountDomainObject accountDomainObject) throws MoneyTransferException {
        try {
            lockAccountTx();
            ServiceResponse serviceResponse = updateAccountDetailsForBalance(accountDomainObject);
            return serviceResponse;
        }finally {
            unlockAccountTx();
        }
    }

    public void lockAccountTx() {
        LockManager.getLockManager().lockAccountTx();
    }
}
