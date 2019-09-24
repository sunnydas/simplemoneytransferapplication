package com.transfer.money.test.lock;

import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.repository.dataaccess.impl.MoneyTFAccountDao;
import com.transfer.money.service.domain.AccountDomainObject;
import com.transfer.money.service.impl.CxAccountService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class AccountServiceLockTest {

    private CxAccountService account;

    @Mock
    private AccountDomainObject accDomainObject;

    private MoneyTFAccountDao moneyTFAccount;


    @Before
    public void setup() throws MoneyTransferException {
        account = new CxAccountService();
        moneyTFAccount = Mockito.mock(MoneyTFAccountDao.class);
    }

    @Test
    public void testAccountcreate() throws MoneyTransferException {
        CxAccountService spied = Mockito.spy(account);
        try {
            spied.createNewSync(accDomainObject);
        } catch (Exception e){
            //ignore
        }
        Mockito.verify(spied,Mockito.times(1)).lockAccountTx();
        Mockito.verify(spied,Mockito.times(1)).unlockAccountTx();
    }

    @Test
    public void testGetAccDetails() throws MoneyTransferException {
        CxAccountService spied = Mockito.spy(account);
        try {
            spied.getAccountDetailsInt(0l);
        } catch (Exception e){
            //ignore
        }
        Mockito.verify(spied,Mockito.times(0)).lockAccountTx();
        Mockito.verify(spied,Mockito.times(0)).unlockAccountTx();
    }

    @Test
    public void testGetAccDetailsAlt() throws MoneyTransferException {
        CxAccountService spied = Mockito.spy(account);
        try {
            spied.getAccountDetailsSync(0l);
        } catch (Exception e){
            //ignore
        }
        Mockito.verify(spied,Mockito.times(1)).lockAccountTx();
        Mockito.verify(spied,Mockito.times(1)).unlockAccountTx();
    }

    @Test
    public void testUpdateAccDetailsAlt() throws MoneyTransferException {
        CxAccountService spied = Mockito.spy(account);
        try {
            spied.updateAccountDetailsForBalance(null);
        } catch (Exception e){
            //ignore
        }
        Mockito.verify(spied,Mockito.times(0)).lockAccountTx();
        Mockito.verify(spied,Mockito.times(0)).unlockAccountTx();
    }

    @Test
    public void testUpdateAccDetails() throws MoneyTransferException {
        CxAccountService spied = Mockito.spy(account);
        try {
            spied.updateAccountDetailsForBalanceSync(null);
        } catch (Exception e){
            //ignore
        }
        Mockito.verify(spied,Mockito.times(1)).lockAccountTx();
        Mockito.verify(spied,Mockito.times(1)).unlockAccountTx();
    }


}
