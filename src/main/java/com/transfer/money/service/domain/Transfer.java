package com.transfer.money.service.domain;

import com.transfer.money.service.ServiceResponse;

import java.math.BigDecimal;
import java.util.Date;

public interface Transfer extends ServiceResponse {

    public long getSourceAccountId();

    public long getTargetAccountId();

    public BigDecimal getTransferAmount();

    public Date getDateOfTransfer();

    public String getStatus();

    public void setSourceAccountId(long id);

    public void setTargetAccountId(long id);

    public void setTransferAmount(BigDecimal amount);

    public void setDateOfTransfer(Date transferDate);

    public void setStatus(String status);

}
