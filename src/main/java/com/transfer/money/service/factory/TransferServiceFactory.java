package com.transfer.money.service.factory;

import com.transfer.money.service.ServiceType;
import com.transfer.money.service.TransferService;
import com.transfer.money.service.impl.MoneyTransferService;

public  class TransferServiceFactory {


    public static TransferService getTransferService(ServiceType serviceType){
        if(serviceType != null && serviceType.equals(ServiceType.TRANSFER)){
            return new MoneyTransferService();
        } else{
            throw new IllegalArgumentException("Invalid service type " + serviceType);
        }
    }


}
