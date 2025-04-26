package com.ps;

import javax.naming.ldap.spi.LdapDnsProvider;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static String toFormatData(Transaction transaction)
    {
        StringBuilder data = new StringBuilder();
        data.append(transaction.getDate()).append(" ");
        data.append(transaction.getTime()).append(" | ");
        data.append(transaction.getDescription()).append(" | ");
        data.append(transaction.getVendor()).append(" | $");
        data.append(transaction.getAmount());
        return data.toString();
    }
}
