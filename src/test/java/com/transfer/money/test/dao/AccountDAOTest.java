package com.transfer.money.test.dao;

import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.repository.dataaccess.db.DBFacade;
import com.transfer.money.repository.dataaccess.impl.MoneyTFAccountDao;
import com.transfer.money.repository.domain.Account;
import com.transfer.money.repository.domain.AccountUser;
import com.transfer.money.service.ServiceResponse;
import com.transfer.money.service.domain.AccountDomainObject;
import com.transfer.money.service.domain.UserDomainObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.*;

public class AccountDAOTest {

    private MoneyTFAccountDao moneyTFAccountDao;

    private DBFacade dbFacade;

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    @Before
    public void setup() throws Exception {
        moneyTFAccountDao = new MoneyTFAccountDao();
        dbFacade = Mockito.mock(DBFacade.class);
        connection = Mockito.mock(Connection.class);
        preparedStatement = Mockito.mock(PreparedStatement.class);
        resultSet = Mockito.mock(ResultSet.class);
        moneyTFAccountDao.setDbFacade(dbFacade);
        Mockito.when(dbFacade.getConnection()).thenReturn(connection);
        Mockito.when(dbFacade.generatePreparedStatement(Mockito.any(),Mockito.anyString())).
                thenReturn(preparedStatement);
    }

    @Test
    public void testCreateAccount() throws Exception {
        AccountDomainObject accountDomainObject = getAccountDomainObject();
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(preparedStatement.execute()).thenReturn(true);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(dbFacade.executeQuery(Mockito.any())).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyInt())).thenReturn(1L);
        ServiceResponse serviceResponse = moneyTFAccountDao.createNew(accountDomainObject);
        Assert.assertNotNull(serviceResponse);
        Assert.assertTrue(serviceResponse instanceof  AccountDomainObject);
        Assert.assertEquals(((AccountDomainObject) serviceResponse).getId(),1);
    }

    @Test
    public void testCreateAccountException() throws Exception {
        AccountDomainObject accountDomainObject = getAccountDomainObject();
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(preparedStatement.execute()).thenReturn(true);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(new SQLException());
        Mockito.when(dbFacade.executeQuery(Mockito.any())).thenThrow(new SQLException());
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyInt())).thenReturn(1L);
        try {
            ServiceResponse serviceResponse = moneyTFAccountDao.createNew(accountDomainObject);
            Assert.assertFalse(true);
        } catch (MoneyTransferException e){
            Assert.assertTrue(e.getExceptionCode().equals(ExceptionCode.SQL_ERROR));
        }
    }

    @Test
    public void getAccount() throws Exception {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(dbFacade.executeQuery(Mockito.any())).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("dummy");
        ServiceResponse serviceResponse = moneyTFAccountDao.getAccountDetails(1);
        Assert.assertTrue(serviceResponse instanceof AccountDomainObject);
        Assert.assertEquals(((AccountDomainObject) serviceResponse).getId(),1);
    }

    @Test
    public void getAccountInt() throws Exception {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(dbFacade.executeQuery(Mockito.any())).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("dummy");
        ServiceResponse serviceResponse = moneyTFAccountDao.getAccountDetailsInt(1);
        Assert.assertTrue(serviceResponse instanceof AccountDomainObject);
        Assert.assertEquals(((AccountDomainObject) serviceResponse).getId(),1);
    }

    @Test
    public void getAccountThrowsException() throws Exception {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(dbFacade.executeQuery(Mockito.any())).thenThrow(new SQLException());
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("dummy");
        try {
            ServiceResponse serviceResponse = moneyTFAccountDao.getAccountDetails(1);
            Assert.assertFalse(true);
        } catch (MoneyTransferException e){
            Assert.assertTrue(e.getExceptionCode().equals(ExceptionCode.SQL_ERROR));
        }
    }

    @Test
    public void updateAccount() throws Exception {
        AccountDomainObject accountDomainObject = getAccountDomainObject();
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(preparedStatement.execute()).thenReturn(true);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(dbFacade.executeQuery(Mockito.any())).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyInt())).thenReturn(1L);
        accountDomainObject.setId(0);
        ServiceResponse serviceResponse = moneyTFAccountDao.updateAccountBalance(accountDomainObject);
        Assert.assertNotNull(serviceResponse);
        Assert.assertTrue(serviceResponse instanceof  AccountDomainObject);
        Assert.assertEquals(((AccountDomainObject) serviceResponse).getId(),1);
    }

    public AccountDomainObject getAccountDomainObject() {
        AccountDomainObject accountDomainObject = new Account();
        accountDomainObject.setBalance(new BigDecimal("100"));
        accountDomainObject.setCurrencyCode("INR");
        accountDomainObject.setUserId(1);
        accountDomainObject.setAccountName("chorus");
        accountDomainObject.setBranchCode("HRI");
        accountDomainObject.setAccountType("SAVINGS");
        return accountDomainObject;
    }

}
