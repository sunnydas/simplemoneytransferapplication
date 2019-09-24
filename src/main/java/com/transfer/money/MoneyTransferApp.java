package com.transfer.money;

import com.transfer.money.app.RESTRouter;
import com.transfer.money.config.DBInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class MoneyTransferApp {

    private static Logger logger = LoggerFactory.getLogger(MoneyTransferApp.class);

    public static void initApp() throws Exception {
        RESTRouter.initRouters();
    }


    public static void main(String[] args) throws Exception {
        logger.info("Starting money transfer application.");
        initApp();
    }

}
