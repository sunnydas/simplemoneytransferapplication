package com.transfer.money.test.lock;

import com.transfer.money.repository.domain.MoneyTransferTx;
import com.transfer.money.service.impl.MoneyTransferService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MoneyTransferLockTest {

    private MoneyTransferService moneyTransferService;

    @Before
    public void setup(){
        moneyTransferService = new MoneyTransferService();
    }

    @Test
    public void testTx(){
        MoneyTransferService spied = Mockito.spy(moneyTransferService);
        try {
            spied.transfer(new MoneyTransferTx());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mockito.verify(spied,Mockito.times(1)).lockTransferTx();
        Mockito.verify(spied,Mockito.times(1)).unlockTransferTx();
    }


    @Test
    public void testTxNull(){
        MoneyTransferService spied = Mockito.spy(moneyTransferService);
        try {
            spied.transfer(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Mockito.verify(spied,Mockito.times(0)).lockTransferTx();
        Mockito.verify(spied,Mockito.times(0)).unlockTransferTx();
    }
}
