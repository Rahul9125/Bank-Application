package org.ecommerce.ecommerce.dtos;

import jakarta.validation.constraints.NotNull;

public class FundTransferRequest {
    @NotNull

    private Long fromAccount;
    @NotNull
    private Long toAccount;
    @NotNull
    private double amount;

    public Long getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Long fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Long getToAccount() {
        return toAccount;
    }

    public void setToAccount(Long toAccount) {
        this.toAccount = toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
