package com.transfer.money.test.dao;

import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.repository.dataaccess.db.DBFacade;
import com.transfer.money.repository.dataaccess.impl.MoneyTFAccountUserDao;
import com.transfer.money.repository.domain.AccountUser;
import com.transfer.money.service.ServiceResponse;
import com.transfer.money.service.domain.UserDomainObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.*;

public class UserDaoTest {

    private MoneyTFAccountUserDao moneyTFAccountUserDao;

    private DBFacade dbFacade;

    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    @Before
    public void setup() throws Exception {
        moneyTFAccountUserDao = new MoneyTFAccountUserDao();
        dbFacade = Mockito.mock(DBFacade.class);
        connection = Mockito.mock(Connection.class);
        preparedStatement = Mockito.mock(PreparedStatement.class);
        resultSet = Mockito.mock(ResultSet.class);
        moneyTFAccountUserDao.setDbFacade(dbFacade);
        Mockito.when(dbFacade.getConnection()).thenReturn(connection);
        Mockito.when(dbFacade.generatePreparedStatement(Mockito.any(),Mockito.anyString())).
                thenReturn(preparedStatement);
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDomainObject userDomainObject = getUserDomainObject();
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(preparedStatement.execute()).thenReturn(true);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(dbFacade.executeQuery(Mockito.any())).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyInt())).thenReturn(1L);
        ServiceResponse serviceResponse = moneyTFAccountUserDao.createNewUser(userDomainObject);
        Assert.assertNotNull(serviceResponse);
        Assert.assertTrue(serviceResponse instanceof  UserDomainObject);
        Assert.assertEquals(((UserDomainObject) serviceResponse).getId(),1);
    }

    @Test
    public void testCreateUserIdFetchException() throws Exception {
        UserDomainObject userDomainObject = getUserDomainObject();
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(0);
        Mockito.when(preparedStatement.execute()).thenReturn(true);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenThrow(new SQLException());
        Mockito.when(dbFacade.executeQuery(Mockito.any())).thenThrow(new SQLException());
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyInt())).thenReturn(1L);
        try {
            ServiceResponse serviceResponse = moneyTFAccountUserDao.createNewUser(userDomainObject);
            Assert.assertFalse(true);
        } catch (MoneyTransferException e){
            Assert.assertTrue(e.getExceptionCode().equals(ExceptionCode.SQL_ERROR));
        }
    }

    @Test
    public void getUser() throws Exception {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(dbFacade.executeQuery(Mockito.any())).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("dummy");
        ServiceResponse serviceResponse = moneyTFAccountUserDao.getUserDetails(1);
        Assert.assertTrue(serviceResponse instanceof UserDomainObject);
        Assert.assertEquals(((UserDomainObject) serviceResponse).getId(),1);
    }

    @Test
    public void getUserThrowsException() throws Exception {
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(dbFacade.executeQuery(Mockito.any())).thenThrow(new SQLException());
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenReturn(1L);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenReturn("dummy");
        try {
            ServiceResponse serviceResponse = moneyTFAccountUserDao.getUserDetails(1);
            Assert.assertFalse(true);
        } catch (MoneyTransferException e){
            Assert.assertTrue(e.getExceptionCode().equals(ExceptionCode.SQL_ERROR));
        }
    }

    public UserDomainObject getUserDomainObject() {
        UserDomainObject userDomainObject = new AccountUser();
        userDomainObject.setUserName("horus");
        userDomainObject.setEmailAddress("h@dbc.ocm");
        userDomainObject.setDob(new Date(System.currentTimeMillis()));
        userDomainObject.setLastName("chorus");
        userDomainObject.setFirstName("horus");
        userDomainObject.setAddress("somewhere");
        return userDomainObject;
    }

}
