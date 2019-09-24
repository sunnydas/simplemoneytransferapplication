package com.transfer.money.repository.dataaccess.factory;

import com.transfer.money.repository.dataaccess.impl.MoneyTFAccountDao;
import com.transfer.money.repository.dataaccess.AccountDao;

public class MoneyTFAccountDaoFactory {

    public static AccountDao getDAO(MoneyTFDAOType moneyTFDAOType){
        if(moneyTFDAOType.equals(MoneyTFDAOType.ACCOUNT)){
            return new MoneyTFAccountDao();
        }
        else{
            throw new IllegalArgumentException("Invalid dao type = " + moneyTFDAOType);
        }
    }

}
