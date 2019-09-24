package com.transfer.money.service.impl;

import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.locks.LockManager;
import com.transfer.money.repository.dataaccess.UserDAO;
import com.transfer.money.repository.dataaccess.factory.MoneyTFDAOType;
import com.transfer.money.repository.dataaccess.factory.MoneyTFUserDaoFactory;
import com.transfer.money.service.ServiceResponse;
import com.transfer.money.service.domain.UserDomainObject;
import com.transfer.money.service.UserService;

public class AccountUserService implements UserService {


    public ServiceResponse createNewUser(UserDomainObject userDomainObject) throws MoneyTransferException {
        UserDAO userDAO = MoneyTFUserDaoFactory.getDAO(MoneyTFDAOType.ACCOUNTUSER);
        try {
            lockUserDomain();
            return userDAO.createNewUser(userDomainObject);
        }finally {
            unlockUserDomain();
        }
    }

    public void lockUserDomain() {
        LockManager.getLockManager().lockAccountUserTx();
    }

    public ServiceResponse getUser(long id) throws MoneyTransferException {
        UserDAO userDAO = MoneyTFUserDaoFactory.getDAO(MoneyTFDAOType.ACCOUNTUSER);
        try {
            lockUserDomain();
            return userDAO.getUserDetails(id);
        }finally {
            unlockUserDomain();
        }
    }

    public void unlockUserDomain() {
        LockManager.getLockManager().unLockAccountUserTx();
    }
}
