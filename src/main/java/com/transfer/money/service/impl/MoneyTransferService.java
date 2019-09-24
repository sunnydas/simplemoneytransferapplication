package com.transfer.money.service.impl;

import com.transfer.money.exception.ExceptionCode;
import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.locks.LockManager;
import com.transfer.money.service.*;
import com.transfer.money.service.domain.AccountDomainObject;
import com.transfer.money.service.domain.Transfer;
import com.transfer.money.service.factory.AccountServiceFactory;
import com.transfer.money.service.validation.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class MoneyTransferService implements TransferService {

    private static Logger logger = LoggerFactory.getLogger(MoneyTransferService.class);

    @Override
    public ServiceResponse transfer(Transfer transfer) throws MoneyTransferException {
        ServiceResponse serviceResponse = null;
        if(transfer != null){
            try {
                lockTransferTx();
                AccountService accountService = AccountServiceFactory.getServiceInstance(ServiceType.ACCOUNT);
                AccountDomainObject sourceAccountDetails = accountService.
                        getAccountDetailsInt(transfer.getSourceAccountId());
                AccountDomainObject targetAccountDetails = accountService.getAccountDetailsInt(transfer.getTargetAccountId());
                if (sourceAccountDetails != null && targetAccountDetails != null) {
                    ValidationUtils.validateTransfer(transfer, sourceAccountDetails, targetAccountDetails);
                    targetAccountDetails.setBalance(targetAccountDetails.getBalance().add(transfer.getTransferAmount()));
                    sourceAccountDetails.setBalance(sourceAccountDetails.getBalance().subtract(transfer.getTransferAmount()));
                    accountService.updateAccountDetailsForBalance(sourceAccountDetails);
                    accountService.updateAccountDetailsForBalance(targetAccountDetails);

                    transfer.setStatus(Status.SUCCESS.toString());
                    transfer.setDateOfTransfer(Calendar.getInstance().getTime());
                    logger.info("Tranfer between source " + sourceAccountDetails.getAccountName()
                            + " to target = " + targetAccountDetails.getAccountName() + " for amount = "
                            + transfer.getTransferAmount()
                            + " is successful.");
                    serviceResponse = transfer;
                } else {
                    String errMessage = "Invalid source or target account for " + transfer;
                    throw new MoneyTransferException(errMessage,
                            ExceptionCode.VALIDATION_ERROR);
                }
            }finally {
                unlockTransferTx();
            }
        }
        return serviceResponse;
    }

    public void unlockTransferTx() {
        LockManager.getLockManager().unlockAccountTx();
    }

    public void lockTransferTx() {
        LockManager.getLockManager().lockAccountTx();
    }


}
