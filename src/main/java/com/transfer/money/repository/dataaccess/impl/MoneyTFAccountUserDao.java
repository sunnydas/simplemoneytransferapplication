package com.transfer.money.repository.dataaccess.impl;

import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.repository.dataaccess.UserDAO;
import com.transfer.money.repository.dataaccess.db.DBFacade;
import com.transfer.money.repository.dataaccess.db.DbFactory;
import com.transfer.money.repository.dataaccess.db.DbType;
import com.transfer.money.repository.domain.AccountUser;
import com.transfer.money.service.ServiceResponse;
import com.transfer.money.service.domain.UserDomainObject;
import org.h2.jdbc.JdbcSQLDataException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoneyTFAccountUserDao implements UserDAO {

    private static Logger logger = LoggerFactory.getLogger(MoneyTFAccountUserDao.class);

    private DBFacade dbFacade;

    public static final String insertUserSQL = "INSERT INTO ACCOUNT_USER (" +
            " user_name, first_name, last_name, email_address , dob ,address )" +
            " values (?,?,?,?,?,?)";


    public static final String getUserIdSQL = "select id from ACCOUNT_USER where user_name = (?)";

    public static final String getUserFromIdSQL = "select * from ACCOUNT_USER where id = (?)";

    public MoneyTFAccountUserDao(){
        this.dbFacade = DbFactory.getFacade(DbType.H2);
    }

    public ServiceResponse createNewUser(UserDomainObject userDomainObject) throws MoneyTransferException {
        if(userDomainObject != null) {
            Connection conn = null;
            PreparedStatement insertPreparedStatement = null;
            boolean error = false;
            try {
                conn = dbFacade.getConnection();
                logger.info("Creating user = " + userDomainObject);
                if (conn != null) {
                    insertPreparedStatement = dbFacade.generatePreparedStatement(conn, insertUserSQL);
                    insertAccountUser(userDomainObject, insertPreparedStatement, dbFacade);
                    userDomainObject.setId(getUserIdFromDB(userDomainObject.getUserName(), dbFacade));
                    logger.info("User creation complete.");
                }
            } catch (SQLException e) {
                return getRespForSQLError(e);
            } catch (MoneyTransferException e){
                throw e;
            } catch (Exception e){
                getRespForGeneralError(error, e, "Unable to create user");
            }finally {
                dbFacade.cleanupBeforeExit(error, conn, insertPreparedStatement);
            }
        }
        return  userDomainObject;
    }

    private ServiceResponse getRespForGeneralError(boolean error, Exception e, String s) throws MoneyTransferException {
        String errMessage = s;
        error = true;
        logger.error(errMessage, e);
        throw new MoneyTransferException(errMessage, e, ExceptionCode.GENERAL_ERROR);
    }

    private ServiceResponse getRespForSQLError(SQLException e) throws MoneyTransferException {
        String errMessage = "Unable to create user";
        logger.error(errMessage, e);
        if(e instanceof JdbcSQLIntegrityConstraintViolationException || e instanceof JdbcSQLDataException){
            throw new MoneyTransferException(errMessage,e, ExceptionCode.VALIDATION_ERROR);
        }
        throw new MoneyTransferException(errMessage, e, ExceptionCode.SQL_ERROR);
    }

    @Override
    public ServiceResponse getUserDetails(long id) throws MoneyTransferException {
        Connection conn = null;
        PreparedStatement getUserDetPreparedStatement = null;
        ServiceResponse serviceResponse = null;
        logger.info("Getting user details for id = " + id);
        boolean error = false;
        if(id >= 0){
            try {
                conn = dbFacade.getConnection();
                if(conn != null) {
                    if (conn != null) {
                        getUserDetPreparedStatement = dbFacade.generatePreparedStatement(conn,getUserFromIdSQL);
                        if(getUserDetPreparedStatement != null) {
                            getUserDetPreparedStatement.setLong(1, id);
                            ResultSet resultSet = dbFacade.executeQuery(getUserDetPreparedStatement);
                            if (resultSet != null && resultSet.next()) {
                                serviceResponse = populateAccountUserDet(resultSet);
                            }
                            else{
                                throw new MoneyTransferException("User = " + id + " not found ",
                                        ExceptionCode.NOT_FOUND );
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                String errMessage = "Unable to get user";
                error = true;
                logger.error(errMessage, e);
                if(e instanceof JdbcSQLIntegrityConstraintViolationException){
                    throw new MoneyTransferException(errMessage,e,ExceptionCode.VALIDATION_ERROR);
                }
                throw new MoneyTransferException(errMessage, e, ExceptionCode.SQL_ERROR);
            } catch (MoneyTransferException e){
                throw e;
            }catch (Exception e){
                getRespForGeneralError(error, e, "Unable to get user");
            }finally {
                dbFacade.cleanupBeforeExit(error, conn, getUserDetPreparedStatement);
            }
        }

        return serviceResponse;
    }

    public ServiceResponse populateAccountUserDet(ResultSet resultSet) throws SQLException, MoneyTransferException {
        ServiceResponse serviceResponse;
        AccountUser accountUser = new AccountUser();
        accountUser.setId(resultSet.getLong("id"));
        accountUser.setUserName(resultSet.getString("user_name"));
        accountUser.setAddress(resultSet.getString("address"));
        accountUser.setEmailAddress(resultSet.getString("email_address"));
        accountUser.setDob(resultSet.getDate("dob"));
        accountUser.setFirstName(resultSet.getString("first_name"));
        accountUser.setLastName(resultSet.getString("last_name"));
        if(accountUser.getId() < 0){
            throw new MoneyTransferException("User not found",ExceptionCode.NOT_FOUND);
        }
        serviceResponse = accountUser;
        return serviceResponse;
    }

    private long getUserIdFromDB(String userName,DBFacade dbFacade) throws MoneyTransferException {
        long userId = -1;
        if(userName != null){
            logger.info("Getting user id for username = " + userName);
            PreparedStatement selectUserIdPreparedStatement = null;
            boolean error= false;
            Connection conn = null;
            try{
                conn = dbFacade.getConnection();
                if(conn != null) {
                    selectUserIdPreparedStatement = dbFacade.generatePreparedStatement(conn,getUserIdSQL);
                    selectUserIdPreparedStatement.setString(1, userName);
                    ResultSet resultSet = dbFacade.executeQuery(selectUserIdPreparedStatement);
                    if (resultSet != null && resultSet.next()) {
                        userId = resultSet.getLong(1);
                        logger.info("Parsed user id = " + userId);
                    }
                }
            } catch (SQLException e) {
                String errMessage = "Unable to get user";
                error = true;
                logger.error(errMessage, e);
                if(e instanceof JdbcSQLIntegrityConstraintViolationException){
                    throw new MoneyTransferException(errMessage,e,ExceptionCode.VALIDATION_ERROR);
                }
                throw new MoneyTransferException(errMessage, e, ExceptionCode.SQL_ERROR);
            }catch (MoneyTransferException e){
                throw e;
            } catch (Exception e){
                getRespForGeneralError(error, e, "Unable to get user");
            }finally {
                dbFacade.cleanupBeforeExit(error, conn, selectUserIdPreparedStatement);
            }
        }
        return userId;
    }

    private void insertAccountUser(UserDomainObject userDomainObject,
                                   PreparedStatement insertPreparedStatement
    ,DBFacade dbFacade) throws Exception {
        insertPreparedStatement.setString(1, userDomainObject.getUserName());
        insertPreparedStatement.setString(2, userDomainObject.getFirstName());
        insertPreparedStatement.setString(3, userDomainObject.getLastName());
        insertPreparedStatement.setString(4, userDomainObject.getEmailAddress());
        insertPreparedStatement.setDate(5, userDomainObject.getDob());
        insertPreparedStatement.setString(6, userDomainObject.getAddress());
        dbFacade.executeUpdate(insertPreparedStatement);
    }
}
