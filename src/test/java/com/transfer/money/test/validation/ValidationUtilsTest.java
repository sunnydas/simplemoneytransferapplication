package com.transfer.money.test.validation;

import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.repository.domain.Account;
import com.transfer.money.repository.domain.MoneyTransferTx;
import com.transfer.money.service.domain.AccountDomainObject;
import com.transfer.money.service.domain.Transfer;
import com.transfer.money.service.validation.ValidationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ValidationUtilsTest {

    @Test
    public void validateCurrencyCodeValid() throws MoneyTransferException {
        String inr = "INR";
        AccountDomainObject accountDomainObject = new Account();
        accountDomainObject.setCurrencyCode(inr);
        ValidationUtils.validateCurrencyCode(accountDomainObject);
        Assert.assertTrue(true);
    }

    @Test
    public void validateCurrencyCodeInValid() throws MoneyTransferException {
        String invalid = "XKCD";
        AccountDomainObject accountDomainObject = new Account();
        accountDomainObject.setCurrencyCode(invalid);
        try {
            ValidationUtils.validateCurrencyCode(accountDomainObject);
        } catch (MoneyTransferException e){
            Assert.assertTrue(true);
        }
    }


    @Test
    public void validateTransfer() throws MoneyTransferException {
        AccountDomainObject source = new Account();
        source.setCurrencyCode("INR");
        source.setBalance(new BigDecimal("100"));
        source.setAccountName("src");
        source.setUserId(1);
        AccountDomainObject target = new Account();
        target.setCurrencyCode("INR");
        target.setBalance(new BigDecimal("200"));
        Transfer transfer = new MoneyTransferTx();
        transfer.setTransferAmount(new BigDecimal("10"));
        transfer.setSourceAccountId(source.getId());
        transfer.setTargetAccountId(target.getId());
        ValidationUtils.validateTransfer(transfer,source,target);
        Assert.assertTrue(true);
    }


    @Test
    public void validateTransferInvalidTarget() throws MoneyTransferException {
        AccountDomainObject source = new Account();
        source.setCurrencyCode("INR");
        source.setBalance(new BigDecimal("100"));
        source.setAccountName("src");
        source.setUserId(1);
        AccountDomainObject target = null;
        Transfer transfer = new MoneyTransferTx();
        transfer.setTransferAmount(new BigDecimal("10"));
        transfer.setSourceAccountId(source.getId());
        try {
            ValidationUtils.validateTransfer(transfer, source, target);
        } catch (MoneyTransferException e){
            Assert.assertTrue(true);
        }
    }


    @Test
    public void validateTransferInvalidSource() throws MoneyTransferException {
        AccountDomainObject source = null;
        AccountDomainObject target = new Account();
        target.setCurrencyCode("INR");
        target.setBalance(new BigDecimal("200"));
        Transfer transfer = new MoneyTransferTx();
        transfer.setTransferAmount(new BigDecimal("10"));
        transfer.setTargetAccountId(target.getId());
        try {
            ValidationUtils.validateTransfer(transfer, source, target);
        } catch (MoneyTransferException e){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateTransferInsuffBalance() throws MoneyTransferException {
        AccountDomainObject source = new Account();
        source.setCurrencyCode("INR");
        source.setBalance(new BigDecimal("10"));
        source.setAccountName("src");
        source.setUserId(1);
        AccountDomainObject target = new Account();
        target.setCurrencyCode("INR");
        target.setBalance(new BigDecimal("200"));
        Transfer transfer = new MoneyTransferTx();
        transfer.setTransferAmount(new BigDecimal("20"));
        transfer.setSourceAccountId(source.getId());
        transfer.setTargetAccountId(target.getId());
        try {
            ValidationUtils.validateTransfer(transfer, source, target);
        } catch (MoneyTransferException e){
            Assert.assertTrue(e.getExceptionCode().equals(ExceptionCode.VALIDATION_ERROR));
        }
    }

    @Test
    public void validateTransferIncompatibleCurrencyCodes() throws MoneyTransferException {
        AccountDomainObject source = new Account();
        source.setCurrencyCode("INR");
        source.setBalance(new BigDecimal("100"));
        source.setAccountName("src");
        source.setUserId(1);
        AccountDomainObject target = new Account();
        target.setCurrencyCode("USD");
        target.setBalance(new BigDecimal("200"));
        Transfer transfer = new MoneyTransferTx();
        transfer.setTransferAmount(new BigDecimal("10"));
        transfer.setSourceAccountId(source.getId());
        transfer.setTargetAccountId(target.getId());
        try {
            ValidationUtils.validateTransfer(transfer, source, target);
        } catch (MoneyTransferException e){
            Assert.assertTrue(e.getExceptionCode().equals(ExceptionCode.VALIDATION_ERROR));
        }
    }

    @Test
    public void validateTransferSourceAndTargetAreSame() throws MoneyTransferException {
        AccountDomainObject source = new Account();
        source.setCurrencyCode("INR");
        source.setBalance(new BigDecimal("100"));
        source.setAccountName("src");
        source.setUserId(1);
        Transfer transfer = new MoneyTransferTx();
        transfer.setTransferAmount(new BigDecimal("10"));
        transfer.setSourceAccountId(source.getId());
        transfer.setTargetAccountId(source.getId());
        try {
            ValidationUtils.validateTransfer(transfer, source, source);
        } catch (MoneyTransferException e){
            Assert.assertTrue(e.getExceptionCode().equals(ExceptionCode.VALIDATION_ERROR));
        }
    }

    @Test
    public void validateTransferInvalidTxAmount() throws MoneyTransferException {
        AccountDomainObject source = new Account();
        source.setCurrencyCode("INR");
        source.setBalance(new BigDecimal("100"));
        source.setAccountName("src");
        source.setUserId(1);
        Transfer transfer = new MoneyTransferTx();
        transfer.setTransferAmount(new BigDecimal("-10"));
        transfer.setSourceAccountId(source.getId());
        transfer.setTargetAccountId(source.getId());
        try {
            ValidationUtils.validateTransfer(transfer, source, source);
        } catch (MoneyTransferException e){
            Assert.assertTrue(e.getExceptionCode().equals(ExceptionCode.VALIDATION_ERROR));
        }
    }
}
