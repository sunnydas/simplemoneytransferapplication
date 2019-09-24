package com.transfer.money.service;

import com.transfer.money.exception.MoneyTransferException;
import com.transfer.money.service.domain.Transfer;

public interface TransferService {

    public ServiceResponse transfer(Transfer transfer) throws MoneyTransferException;

}
