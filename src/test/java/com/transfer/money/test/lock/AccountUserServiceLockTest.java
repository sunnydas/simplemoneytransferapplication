package com.transfer.money.test.lock;

import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.repository.dataaccess.impl.MoneyTFAccountUserDao;
import com.transfer.money.service.domain.UserDomainObject;
import com.transfer.money.service.impl.AccountUserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class AccountUserServiceLockTest {

    private AccountUserService accountUserService;

    @Mock
    private UserDomainObject userDomainObject;

    private MoneyTFAccountUserDao moneyTFAccountUserDao;


    @Before
    public void setup() throws MoneyTransferException {
        accountUserService = new AccountUserService();
        moneyTFAccountUserDao = Mockito.mock(MoneyTFAccountUserDao.class);
        Mockito.when(moneyTFAccountUserDao.createNewUser(Mockito.anyObject())).thenReturn(userDomainObject);
        Mockito.when(moneyTFAccountUserDao.getUserDetails(Mockito.anyLong())).thenReturn(userDomainObject);
    }

    @Test
    public void testCreateUser() throws MoneyTransferException {
        AccountUserService spied = Mockito.spy(accountUserService);
        spied.createNewUser(userDomainObject);
        Mockito.verify(spied,Mockito.times(1)).lockUserDomain();
        Mockito.verify(spied,Mockito.times(1)).unlockUserDomain();
    }

    @Test
    public void testGetUser() throws MoneyTransferException {
        AccountUserService spied = Mockito.spy(accountUserService);
        try {
            spied.getUser(Long.parseLong("1"));
        }catch (Exception e){
            //ignore
        }
        Mockito.verify(spied,Mockito.times(1)).lockUserDomain();
        Mockito.verify(spied,Mockito.times(1)).unlockUserDomain();
    }

    @Test
    public void testCreateUserException() throws MoneyTransferException {
        AccountUserService spied = Mockito.spy(accountUserService);
        Mockito.when(moneyTFAccountUserDao.createNewUser(Mockito.any())).thenThrow(new MoneyTransferException("",
                ExceptionCode.VALIDATION_ERROR));
        try {
            spied.createNewUser(userDomainObject);
        } catch (Exception e){
            //ignore
        }
        Mockito.verify(spied,Mockito.times(1)).lockUserDomain();
        Mockito.verify(spied,Mockito.times(1)).unlockUserDomain();
    }


}
