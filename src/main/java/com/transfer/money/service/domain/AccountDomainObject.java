package com.transfer.money.service.domain;

import com.transfer.money.service.ServiceResponse;

import java.math.BigDecimal;

public interface AccountDomainObject extends ServiceResponse {

    public long getId();

    public long getUserId();

    public String getAccountName();

    public String getBranchCode();

    public String getCurrencyCode();

    public BigDecimal getBalance();

    public String getAccountType();

    public void setId(long id);

    public void setUserId(long id);

    public void setAccountName(String accountName);

    public void setBranchCode(String branchCode);

    public void setCurrencyCode(String currencyCode);

    public void setBalance(BigDecimal balance);

    public void setAccountType(String accountType);

}
