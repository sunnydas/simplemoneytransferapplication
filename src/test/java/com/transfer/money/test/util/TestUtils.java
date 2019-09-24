package com.transfer.money.test.util;

import com.transfer.money.repository.domain.Account;
import com.transfer.money.repository.domain.AccountUser;
import com.transfer.money.service.domain.AccountDomainObject;
import com.transfer.money.service.domain.UserDomainObject;

import java.math.BigDecimal;

public class TestUtils {

    public static final String PORT_KEY = "port";
    public static final String HOST_KEY = "host";
    public static final String ACCOUNT_USERS_PATH = "/moneytfapp/accountusers";
    public static final String ACCOUNT_PATH = "/moneytfapp/account";
    public static final String TRANSFER_PATH = "/moneytfapp/transfer";
    public static final String PROTOCOL = "http://";

    public static UserDomainObject populateUser(){
        UserDomainObject userDomainObject = new AccountUser();
        userDomainObject.setFirstName("brian");
        userDomainObject.setLastName("lara");
        userDomainObject.setEmailAddress("abc@test.com");
        return userDomainObject;
    }

    public static AccountDomainObject populateAccount(){
        AccountDomainObject accountDomainObject = new Account();
        accountDomainObject.setBalance(new BigDecimal("100"));
        accountDomainObject.setBranchCode("BN");
        accountDomainObject.setCurrencyCode("INR");
        accountDomainObject.setAccountType("SAVINGS");
        return accountDomainObject;
    }

}
