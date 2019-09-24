package com.transfer.money.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.transfer.money.config.DBInitializer;
import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.ExceptionHandler;
import com.transfer.money.repository.domain.Account;
import com.transfer.money.repository.domain.AccountUser;
import com.transfer.money.repository.domain.MoneyTransferTx;
import com.transfer.money.service.*;
import com.transfer.money.service.factory.AccountServiceFactory;
import com.transfer.money.service.factory.TransferServiceFactory;
import com.transfer.money.service.factory.UserServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class RESTRouter {

    private static Logger logger = LoggerFactory.getLogger(RESTRouter.class);
    private static String dateFormat = "yyyy-MM-dd";

    public static void initRouters() throws Exception {
        logger.info("Initializing routers");
        DBInitializer.createTables();
        post("/moneytfapp/accountusers", (request, response) ->
                {
                    response.type("application/json");
                    Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
                    ServiceResponse serviceResponse = null;
                    try {
                        AccountUser accountUser = gson.fromJson(request.body(), AccountUser.class);
                        UserService accountUserUserService = UserServiceFactory.getServiceInstance(ServiceType.ACCOUNTUSER);
                        serviceResponse  = accountUserUserService.createNewUser(accountUser);
                        response.status(201);
                    } catch (Exception e){
                        logger.error("Error caught during creating account user",e);
                        return getErrorResponse(response, gson, e);
                    }
                    return gson.toJson(serviceResponse);
                }
        );
        get("/moneytfapp/accountusers/:id", (request, response) ->
                {
                    response.type("application/json");
                    Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
                    ServiceResponse serviceResponse = null;
                    try {
                        UserService accountUserUserService = UserServiceFactory.getServiceInstance(ServiceType.ACCOUNTUSER);
                        serviceResponse = accountUserUserService.getUser(Long.parseLong(request.
                                params("id")));
                        response.status(200);
                    } catch (Exception e){
                        logger.error("Error fetching user details",e);
                        return getErrorResponse(response, gson, e);
                    }
                    return gson.toJson(serviceResponse);
                }
        );
        post("/moneytfapp/account", (request, response) ->
                {
                    response.type("application/json");
                    Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
                    ServiceResponse serviceResponse = null;
                    try {
                        Account account = gson.fromJson(request.body(), Account.class);
                        AccountService accountService = AccountServiceFactory.getServiceInstance(ServiceType.ACCOUNT);
                        serviceResponse  = accountService.createNewSync(account);
                        response.status(201);
                    } catch (Exception e){
                        logger.error("Error caught during creating account",e);
                        return getErrorResponse(response, gson, e);
                    }
                    return gson.toJson(serviceResponse);
                }
        );
        put("/moneytfapp/account", (request, response) ->
                {
                    response.type("application/json");
                    Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
                    ServiceResponse serviceResponse = null;
                    try {
                        Account account = gson.fromJson(request.body(), Account.class);
                        AccountService accountService = AccountServiceFactory.getServiceInstance(ServiceType.ACCOUNT);
                        serviceResponse  = accountService.updateAccountDetailsForBalanceSync(account);
                        response.status(200);
                    } catch (Exception e){
                        logger.error("Error caught during creating account",e);
                        return getErrorResponse(response, gson, e);
                    }
                    return gson.toJson(serviceResponse);
                }
        );
        get("/moneytfapp/account/:id", (request, response) ->
                {
                    response.type("application/json");
                    Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
                    ServiceResponse serviceResponse = null;
                    try {
                        AccountService accountService = AccountServiceFactory.getServiceInstance(ServiceType.ACCOUNT);
                        serviceResponse  = accountService.getAccountDetailsSync(Long.parseLong(request.
                                params("id")));
                        response.status(200);
                    } catch (Exception e){
                        logger.error("Error caught during creating account",e);
                        return getErrorResponse(response, gson, e);
                    }
                    return gson.toJson(serviceResponse);
                }
        );
        post("/moneytfapp/transfer", (request, response) ->
                {
                    response.type("application/json");
                    Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
                    ServiceResponse serviceResponse = null;
                    try {
                        MoneyTransferTx transferTx = gson.fromJson(request.body(), MoneyTransferTx.class);
                        TransferService transferService = TransferServiceFactory.getTransferService(ServiceType.TRANSFER);
                        serviceResponse  = transferService.transfer(transferTx);
                        response.status(202);
                    } catch (Exception e){
                        logger.error("Error caught during processing transfer",e);
                        return getErrorResponse(response, gson, e);
                    }
                    return gson.toJson(serviceResponse);
                }
        );
    }

    private static Object getErrorResponse(Response response, Gson gson, Exception e) {
        response.status(500);
        ExceptionHandler exceptionHandler = new ExceptionHandler(e);
        if(exceptionHandler.getExceptionCode().equals(ExceptionCode.VALIDATION_ERROR)){
            response.status(400);
        } else if(exceptionHandler.getExceptionCode().equals(ExceptionCode.NOT_FOUND)){
            response.status(404);
        }
        return gson.toJson(exceptionHandler);
    }


}
