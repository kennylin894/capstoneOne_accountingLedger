package com.ps;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class HelperMethods {
    public static void ledgerMenuOptions() {
        String ledgerMenu;
        System.out.println("Welcome to the ledger");
        System.out.println("=====================");
        do {
            System.out.println("Please choose an option:");
            System.out.println("[A] All - (Display all enteries)");
            System.out.println("[D] Deposits - (Display only the entries that are deposits into the account");
            System.out.println("[P] Payments - (Display only the negative entries or payments)");
            System.out.println("[R] Reports - (Search for reports)");
            System.out.println("[H] Home - (Return Home)");
            ledgerMenu = Main.stringScanner.nextLine();
        } while (!ledgerMenu.equals("H"));
    }

    public static void addDepositMenuOptions() {

    }

    public static void makePaymentMenuOptions() {

    }

    public static ArrayList<Transaction> readTransactionFile() {
        ArrayList<Transaction> listOfTransactions = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("transaction.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                listOfTransactions.add(new Transaction(date,time,description,vendor,amount));
            }
            reader.close();
        }
        catch (Exception e) {
            System.out.println("File doesnt exist");
        }
        return listOfTransactions;
    }

    public void writeToFile(Transaction transaction) {
        try {
            FileWriter writer = new FileWriter("transaction.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(Transaction.toFormatData(transaction));
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }

    }

}
