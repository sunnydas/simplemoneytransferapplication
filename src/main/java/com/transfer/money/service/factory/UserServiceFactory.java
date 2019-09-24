package com.transfer.money.service.factory;

import com.transfer.money.service.ServiceType;
import com.transfer.money.service.UserService;
import com.transfer.money.service.impl.AccountUserService;

public class UserServiceFactory {


    public static UserService getServiceInstance(ServiceType serviceType){
        if(serviceType.equals(ServiceType.ACCOUNTUSER)){
            return new AccountUserService();
        }
        else{
            throw new IllegalArgumentException("Invalid service type = " + serviceType);
        }
    }


}
