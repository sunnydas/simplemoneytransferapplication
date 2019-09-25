package com.transfer.money.repository.dataaccess.impl;

import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.repository.dataaccess.AccountDao;
import com.transfer.money.repository.dataaccess.db.DBFacade;
import com.transfer.money.repository.dataaccess.db.DbFactory;
import com.transfer.money.repository.dataaccess.db.DbType;
import com.transfer.money.repository.domain.Account;
import com.transfer.money.service.ServiceResponse;
import com.transfer.money.service.domain.AccountDomainObject;
import org.h2.jdbc.JdbcSQLDataException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoneyTFAccountDao implements AccountDao {

    private static Logger logger = LoggerFactory.getLogger(MoneyTFAccountDao.class);

    public void setDbFacade(DBFacade dbFacade) {
        this.dbFacade = dbFacade;
    }

    private DBFacade dbFacade;

    public static final String insertAccountSQL = "INSERT INTO ACCOUNT (" +
            " user_id, account_name,branch_code, currency_code, balance , account_type )" +
            " values (?,?,?,?,?,?)";

    public static final String updateAccountSQL = "UPDATE  ACCOUNT SET " +
            "  balance = ?  where id = ?";


    public static final String getAccountIdSQL = "select id from ACCOUNT where account_name = (?)";

    public static final String getAccountFromIdSQL = "select * from ACCOUNT where id = (?)";

    public MoneyTFAccountDao(){
        this.dbFacade = DbFactory.getFacade(DbType.H2);
    }

    public ServiceResponse createNew(AccountDomainObject accountDomainObject) throws MoneyTransferException {
        if(accountDomainObject != null) {
            Connection conn = null;
            PreparedStatement insertPreparedStatement = null;
            boolean error = false;
            try {
                conn = dbFacade.getConnection();
                logger.info("Creating account = " + accountDomainObject);
                if (conn != null) {
                    insertPreparedStatement = dbFacade.generatePreparedStatement(conn, insertAccountSQL);
                    insertAccount(accountDomainObject, insertPreparedStatement, dbFacade);
                    accountDomainObject.setId(getAccountIdFromDB(accountDomainObject.getAccountName(), dbFacade));
                    logger.info("account creation complete.");
                }
            } catch (SQLException e) {
                error = true;
                return getRespForSQLErr(e,"Unable to create account");
            }catch (MoneyTransferException e){
                error = true;
                throw e;
             } catch (Exception e){
                error = true;
                getRespForGenException(e, "Unable to create account");
            }finally {
                dbFacade.cleanupBeforeExit(error, conn, insertPreparedStatement);
            }
        }
        return  accountDomainObject;
    }

    private ServiceResponse getRespForGenException(Exception e, String s) throws MoneyTransferException {
        String errMessage = s;
        logger.error(errMessage, e);
        throw new MoneyTransferException(errMessage, e, ExceptionCode.GENERAL_ERROR);
    }

    private ServiceResponse getRespForSQLErr(SQLException e,String errMessage) throws MoneyTransferException {
        logger.error(errMessage, e);
        if (e instanceof JdbcSQLIntegrityConstraintViolationException || e instanceof JdbcSQLDataException) {
            throw new MoneyTransferException(errMessage, e, ExceptionCode.VALIDATION_ERROR);
        }
        throw new MoneyTransferException(errMessage, e, ExceptionCode.SQL_ERROR);
    }

    public ServiceResponse getAccountDetails(long id) throws MoneyTransferException {
        return getAccountDetailsInt(id);
    }


    public AccountDomainObject getAccountDetailsInt(long id) throws MoneyTransferException {
        return getAccountDets(id);
    }

    private AccountDomainObject getAccountDets(long id) throws MoneyTransferException {
        Connection conn = null;
        AccountDomainObject accountDomainObject = null;
        PreparedStatement getAccountDetPreparedStatement = null;
        logger.info("Getting account details for id = " + id);
        boolean error = false;
        if(id >= 0){
            try {
                conn = dbFacade.getConnection();
                if(conn != null) {
                    if (conn != null) {
                        getAccountDetPreparedStatement = dbFacade.generatePreparedStatement(conn,getAccountFromIdSQL);
                        if(getAccountDetPreparedStatement != null) {
                            getAccountDetPreparedStatement.setLong(1, id);
                            ResultSet resultSet = dbFacade.executeQuery(getAccountDetPreparedStatement);
                            if (resultSet != null && resultSet.next()) {
                                accountDomainObject = populateAccount(resultSet);
                            } else{
                                throw new MoneyTransferException("Account for id = "+ id
                                        + " not found.",ExceptionCode.NOT_FOUND);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                error = true;
                getRespForSQLErr(e,"Unable to get account");
            }catch (MoneyTransferException e){
                error = true;
                throw e;
            }catch (Exception e){
                error = true;
                getRespForGenErr(e,"Unable to get account");
            }finally {
                dbFacade.cleanupBeforeExit(error, conn, getAccountDetPreparedStatement);
            }
        }
        return accountDomainObject;
    }

    @Override
    public AccountDomainObject updateAccountBalance(AccountDomainObject accountDomainObject) throws MoneyTransferException {
        if(accountDomainObject != null) {
            Connection conn = null;
            PreparedStatement updatePreparedStatement = null;
            boolean error = false;
            try {
                conn = dbFacade.getConnection();
                logger.info("updating account = " + accountDomainObject);
                if (conn != null) {
                    AccountDomainObject persisted = getAccountDetailsInt(accountDomainObject.getId());
                    if(persisted != null && persisted.getId() == accountDomainObject.getId()) {
                        updatePreparedStatement = dbFacade.generatePreparedStatement(conn, updateAccountSQL);
                        updateAccountForBalance(accountDomainObject, updatePreparedStatement, dbFacade);
                        accountDomainObject.setId(getAccountIdFromDB(accountDomainObject.getAccountName(), dbFacade));
                        logger.info("account updtation complete.");
                    } else{
                        throw new MoneyTransferException("Account for id = " + accountDomainObject.getId()
                                + " not found.",ExceptionCode.NOT_FOUND);
                    }
                }
            } catch (SQLException e) {
                error = true;
                getRespForSQLErr(e,"Unable to update account");
            } catch (MoneyTransferException e){
                error = true;
                throw e;
            } catch (Exception e){
                error = true;
                return getRespForGenErr(e, "Unable to update account");
            }finally {
                dbFacade.cleanupBeforeExit(error, conn, updatePreparedStatement);
            }
        }
        return  accountDomainObject;
    }

    private AccountDomainObject getRespForGenErr(Exception e, String s) throws MoneyTransferException {
        String errMessage = s;
        logger.error(errMessage, e);
        throw new MoneyTransferException(errMessage, e, ExceptionCode.GENERAL_ERROR);
    }

    private Account populateAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getLong("id"));
        account.setUserId(resultSet.getLong("user_id"));
        account.setAccountName(resultSet.getString("account_name"));
        account.setBalance(resultSet.getBigDecimal("balance"));
        account.setBranchCode(resultSet.getString("branch_code"));
        account.setCurrencyCode(resultSet.getString("currency_code"));
        account.setAccountType(resultSet.getString("account_type"));
        return account;
    }

    private long getAccountIdFromDB(String accountName, DBFacade dbFacade) throws MoneyTransferException {
        long accountId = -1;
        Connection conn = null;
        if(accountName != null){
            logger.info("Getting account id for accountname = " + accountName);
            PreparedStatement selectAccountIdPreparedStatement = null;
            boolean error = false;
            try{
                conn = dbFacade.getConnection();
                if(conn != null) {
                    selectAccountIdPreparedStatement = dbFacade.generatePreparedStatement(conn, getAccountIdSQL);
                    selectAccountIdPreparedStatement.setString(1, accountName);
                    ResultSet resultSet = dbFacade.executeQuery(selectAccountIdPreparedStatement);
                    if (resultSet != null && resultSet.next()) {
                        accountId = resultSet.getLong(1);
                        logger.info("Parsed user id = " + accountId);
                    } else{
                        throw new MoneyTransferException("Account for id = " + accountId + "not found",
                                ExceptionCode.NOT_FOUND);
                    }
                }
            } catch (SQLException e) {
                error = true;
                getRespForSQLErr(e,"Unable to get account id");
            }catch (MoneyTransferException e){
                error = true;
                throw e;
            }catch (Exception e){
                error = true;
                getRespForGenErr(e, "Unable to get account id");
            }finally {
                dbFacade.cleanupBeforeExit(error, conn, selectAccountIdPreparedStatement);
            }
        }
        return accountId;
    }


    private void insertAccount(AccountDomainObject accountDomainObject,
                                   PreparedStatement insertPreparedStatement
            ,DBFacade dbFacade) throws Exception {
        insertPreparedStatement.setLong(1, accountDomainObject.getUserId());
        insertPreparedStatement.setString(2, accountDomainObject.getAccountName());
        insertPreparedStatement.setString(3, accountDomainObject.getBranchCode());
        insertPreparedStatement.setString(4, accountDomainObject.getCurrencyCode());
        insertPreparedStatement.setBigDecimal(5, accountDomainObject.getBalance());
        insertPreparedStatement.setString(6, accountDomainObject.getAccountType());
        dbFacade.executeUpdate(insertPreparedStatement);
    }

    private void updateAccountForBalance(AccountDomainObject accountDomainObject,
                                         PreparedStatement updatePreparedStatement
            , DBFacade dbFacade) throws Exception {
        updatePreparedStatement.setBigDecimal(1, accountDomainObject.getBalance());
        updatePreparedStatement.setLong(2, accountDomainObject.getId());
        dbFacade.executeUpdate(updatePreparedStatement);
    }
}
