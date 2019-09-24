package com.transfer.money.service;

import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.service.domain.UserDomainObject;

public interface UserService {


    public ServiceResponse createNewUser(UserDomainObject userDomainObject) throws MoneyTransferException;


    public ServiceResponse getUser(long userId) throws MoneyTransferException;



}
