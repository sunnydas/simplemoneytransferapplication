package com.transfer.money.service.factory;

import com.transfer.money.service.AccountService;
import com.transfer.money.service.ServiceType;
import com.transfer.money.service.impl.CxAccountService;

public class AccountServiceFactory {

    public static AccountService getServiceInstance(ServiceType serviceType){
        if(serviceType.equals(ServiceType.ACCOUNT)){
            return new CxAccountService();
        }
        else{
            throw new IllegalArgumentException("Invalid service type = " + serviceType);
        }
    }

}
