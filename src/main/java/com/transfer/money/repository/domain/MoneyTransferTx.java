package com.transfer.money.repository.domain;

import com.transfer.money.service.domain.Transfer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class MoneyTransferTx implements Transfer {

    private long sourceAccountId;

    private long targetAccountId;

    @Override
    public String toString() {
        return "MoneyTransferTx{" +
                "sourceAccountId=" + sourceAccountId +
                ", targetAccountId=" + targetAccountId +
                ", transferAmount=" + transferAmount +
                ", dateOfTransfer=" + dateOfTransfer +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyTransferTx that = (MoneyTransferTx) o;
        return sourceAccountId == that.sourceAccountId &&
                targetAccountId == that.targetAccountId &&
                Objects.equals(transferAmount, that.transferAmount) &&
                Objects.equals(dateOfTransfer, that.dateOfTransfer) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceAccountId, targetAccountId, transferAmount, dateOfTransfer, status);
    }

    @Override
    public long getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(long sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    @Override
    public long getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    @Override
    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    @Override
    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Override
    public Date getDateOfTransfer() {
        return dateOfTransfer;
    }

    @Override
    public void setDateOfTransfer(Date dateOfTransfer) {
        this.dateOfTransfer = dateOfTransfer;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    private BigDecimal transferAmount;

    private Date dateOfTransfer;

    private String status;
}
