package com.transfer.money.repository.dataaccess.factory;

import com.transfer.money.repository.dataaccess.impl.MoneyTFAccountUserDao;
import com.transfer.money.repository.dataaccess.UserDAO;

public class MoneyTFUserDaoFactory {

    public static UserDAO getDAO(MoneyTFDAOType moneyTFDAOType){
        if(moneyTFDAOType.equals(MoneyTFDAOType.ACCOUNTUSER)){
            return new MoneyTFAccountUserDao();
        }
        else{
            throw new IllegalArgumentException("Invalid dao type = " + moneyTFDAOType);
        }
    }

}
