package com.transfer.money.config;

import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.repository.dataaccess.db.DBFacade;
import com.transfer.money.repository.dataaccess.db.DbFactory;
import com.transfer.money.repository.dataaccess.db.DbType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class DBInitializer {

    private static Logger logger = LoggerFactory.getLogger(DBInitializer.class);

    public static void createTables()throws Exception{
        DBFacade dbFacade = DbFactory.getFacade(DbType.H2);
        Connection conn = dbFacade.getConnection();
        if(conn != null) {
            try {
                conn = dbFacade.getConnection();
                if (conn != null) {
                    logger.info("Creating tables for money transfer app ..");
                    dbFacade.createTables(conn);
                    logger.info("Table creation complete");
                }
            } catch (Exception e) {
                logger.error("Unable to create initial tables,CRUD operations will not succeed",e);
                throw e;
            } finally {
                dbFacade.closeSilently(conn);
            }
        } else{
            throw new MoneyTransferException("Connection could not be fetched", ExceptionCode.GENERAL_ERROR);
        }

    }

}
