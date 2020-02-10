package com.xiling.ddui.bean;

public class AccountInfo {

    /**
     * id : 1
     * balance : 500000000
     * balanceGrow : 0
     * incomeTotal : 10
     * growValue : 20.5
     * growValueTotle : 500.5
     */

    private int id;
    private double balance;
    private int balanceGrow;
    private int incomeTotal;
    private double growValue;
    private double growValueTotle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalanceGrow() {
        return balanceGrow;
    }

    public void setBalanceGrow(int balanceGrow) {
        this.balanceGrow = balanceGrow;
    }

    public int getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(int incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public double getGrowValue() {
        return growValue;
    }

    public void setGrowValue(double growValue) {
        this.growValue = growValue;
    }

    public double getGrowValueTotle() {
        return growValueTotle;
    }

    public void setGrowValueTotle(double growValueTotle) {
        this.growValueTotle = growValueTotle;
    }
}
