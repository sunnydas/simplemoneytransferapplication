package com.transfer.money.repository.dataaccess;

import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.service.domain.UserDomainObject;
import com.transfer.money.service.ServiceResponse;

public interface UserDAO {

    public ServiceResponse createNewUser(UserDomainObject userDomainObject) throws MoneyTransferException;

    public ServiceResponse getUserDetails(long id) throws MoneyTransferException;

}
