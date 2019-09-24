package com.transfer.money.repository.dataaccess.db;

public class DbFactory {

    public static DBFacade getFacade(DbType dbType){
        if(dbType != null && dbType.equals(DbType.H2)){
            return new H2DbFacade();
        } else{
            throw new IllegalArgumentException("Invalid dbType = " + dbType);
        }
    }

}
