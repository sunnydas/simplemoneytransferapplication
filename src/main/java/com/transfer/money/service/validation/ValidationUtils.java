package com.transfer.money.service.validation;

import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.service.domain.AccountDomainObject;
import com.transfer.money.service.domain.Transfer;

import java.math.BigDecimal;
import java.util.Currency;

public class ValidationUtils {

    public static void validateTransfer(Transfer transfer, AccountDomainObject sourceAccountDetails, AccountDomainObject targetAccountDetails) throws MoneyTransferException {
        if(sourceAccountDetails == null || targetAccountDetails == null){
            throw new MoneyTransferException("Invalid source or target ",ExceptionCode.VALIDATION_ERROR);
        }
        if(transfer.getTransferAmount().compareTo(new BigDecimal("1")) < 0){
            throw new MoneyTransferException("Invalid transfer amount = " + transfer.getTransferAmount(),
                    ExceptionCode.VALIDATION_ERROR);
        }
        if(sourceAccountDetails.equals(targetAccountDetails)){
            throw new MoneyTransferException("Source and target are same = " + sourceAccountDetails,
                    ExceptionCode.VALIDATION_ERROR);
        }
        if (sourceAccountDetails.getBalance().compareTo(transfer.getTransferAmount()) < 0) {
            String errMessage = "Insufficient balance for source " + sourceAccountDetails.getAccountName()
                    + " available balance = " + sourceAccountDetails.getBalance()
                    + " , required balance should be greater or equal to " + transfer.getTransferAmount();
            throw new MoneyTransferException(errMessage, ExceptionCode.VALIDATION_ERROR);
        }
        if(sourceAccountDetails.getCurrencyCode() == null
                || targetAccountDetails.getCurrencyCode() == null
                || !sourceAccountDetails.getCurrencyCode()
                .equals(targetAccountDetails.getCurrencyCode())){
            String errMessage = "The currency code of source " + sourceAccountDetails.getAccountName()
                    + " code = " + sourceAccountDetails.getCurrencyCode()
                    + " , does not match target account " + targetAccountDetails.getAccountName() + "" +
                    " code = " + targetAccountDetails.getCurrencyCode();
            throw new MoneyTransferException(errMessage,ExceptionCode.VALIDATION_ERROR);
        }
        validateCurrencyCode(sourceAccountDetails);
        validateCurrencyCode(targetAccountDetails);
    }

    public static void validateCurrencyCode(AccountDomainObject accountDomainObject)throws MoneyTransferException{
        if(accountDomainObject != null){
            try {
                Currency.getInstance(accountDomainObject.getCurrencyCode());
            } catch (IllegalArgumentException e){
                throw new MoneyTransferException(e.getMessage(),ExceptionCode.VALIDATION_ERROR);
            }
        } else {
            throw new MoneyTransferException("Invalid account domaon object",ExceptionCode.VALIDATION_ERROR);
        }

    }

}
