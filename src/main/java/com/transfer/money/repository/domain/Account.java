package com.transfer.money.repository.domain;

import com.transfer.money.service.domain.AccountDomainObject;
import com.transfer.money.service.ServiceResponse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class Account implements Serializable, AccountDomainObject, ServiceResponse {

    private static final long SerialVersionUID = 12l;

    private long id = -1;

    private long userId;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", branchCode='" + branchCode + '\'' +
                ", accountName='" + accountName + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                '}';
    }

    private String branchCode;

    private String accountName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                userId == account.userId &&
                Objects.equals(branchCode, account.branchCode) &&
                Objects.equals(accountName, account.accountName) &&
                Objects.equals(currencyCode, account.currencyCode) &&
                Objects.equals(balance, account.balance) &&
                Objects.equals(accountType, account.accountType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, branchCode, accountName, currencyCode, balance, accountType);
    }

    @Override
    public String getAccountName() {
        return accountName;
    }

    @Override
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    private String currencyCode;

    private BigDecimal balance;

    private String accountType;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
