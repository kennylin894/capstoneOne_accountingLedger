package com.ps;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class HelperMethods {
    static Comparator<Transaction> comparator = new Comparator<Transaction>() {
        @Override
        public int compare(Transaction transaction1, Transaction transaction2) {
            String dateTime1 = transaction1.getDate() + " " + transaction1.getTime();
            String dateTime2 = transaction2.getDate() + " " + transaction2.getTime();
            return dateTime2.compareTo(dateTime1);
        }
    };

    public static void ledgerMenuOptions() {
        String ledgerMenu;
        do {
            System.out.println("Welcome to the ledger");
            System.out.println("=====================");
            System.out.println("Please choose an option:");
            System.out.println("[A] All - (Display all enteries)");
            System.out.println("[D] Deposits - (Display only the entries that are deposits into the account)");
            System.out.println("[P] Payments - (Display only the negative entries or payments)");
            System.out.println("[R] Reports - (Search for reports)");
            System.out.println("[H] Home - (Return Home)");
            ledgerMenu = Main.stringScanner.nextLine();
            //user wants to see all transactions
            if (ledgerMenu.equalsIgnoreCase("a")) {
                System.out.println("These are all the transactions. (Newest on top)");
                System.out.println("===============================================");
                for(Transaction transaction: readTransactionFile())
                {
                    System.out.println(toFormatData(transaction));
                }
                System.out.println();
            //user only wants to see deposits
            } else if (ledgerMenu.equalsIgnoreCase("d")) {
                System.out.println("These are all the deposits. (Newest on top)");
                System.out.println("=======================================");
                for(Transaction transaction: readTransactionFile())
                {
                    if(transaction.getAmount() > 0)
                    {
                        System.out.println(toFormatData(transaction));
                    }
                }
                System.out.println();
            //user wants to see all payments
            } else if (ledgerMenu.equalsIgnoreCase("p")) {
                System.out.println("These are all the payments. (Newest on top)");
                System.out.println("===============================================");
                for(Transaction transaction: readTransactionFile())
                {
                    if(transaction.getAmount() < 0)
                    {
                        System.out.println(toFormatData(transaction));
                    }
                }
                System.out.println();
            } else if (ledgerMenu.equalsIgnoreCase("r")) {

            } else if(!ledgerMenu.equalsIgnoreCase("h")){
                System.out.println("Error, bad input try again.");
            }
        } while (!ledgerMenu.equalsIgnoreCase("H"));
    }

    public static void addDepositMenuOptions() {
        System.out.println("Enter deposit description: ");
        String description = Main.stringScanner.nextLine();
        System.out.println("Enter the vendor: ");
        String vendor = Main.stringScanner.nextLine();
        System.out.println("Enter the total amount: ");
        String amount = Main.stringScanner.nextLine();
        while(Double.parseDouble(amount) <= 0)
        {
            System.out.println("Invalid deposit, please try again.");
            System.out.println("Enter the total amount: ");
            amount = Main.stringScanner.nextLine();
            System.out.println();
        }
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Transaction transaction = new Transaction(date,time,description,vendor,Double.parseDouble(amount));
        writeToFile(transaction);
        System.out.println("Your deposit has been successfully processed.");
        System.out.println();
    }

    public static void makePaymentMenuOptions() {
        System.out.println("Enter payment description: ");
        String description = Main.stringScanner.nextLine();
        System.out.println("Enter the vendor: ");
        String vendor = Main.stringScanner.nextLine();
        System.out.println("Enter the total amount: ");
        String amount = Main.stringScanner.nextLine();
        if(Double.parseDouble(amount) > 0)
        {
            Double strAmount;
            strAmount = Double.parseDouble(amount) * -1;
            amount = String.valueOf(strAmount);
        }
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Transaction transaction = new Transaction(date,time,description,vendor,Double.parseDouble(amount));
        writeToFile(transaction);
        System.out.println("Your payment has been successfully processed.");
        System.out.println();
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
                listOfTransactions.add(new Transaction(date, time, description, vendor, amount));
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("File doesnt exist");
        }
        listOfTransactions.sort(comparator);
        return listOfTransactions;
    }

    public static void writeToFile(Transaction transaction) {
        try {
            FileWriter writer = new FileWriter("transaction.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.newLine();
            bufferedWriter.write(toFormatData(transaction));
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    public static String toFormatData(Transaction transaction) {
        StringBuilder data = new StringBuilder();
        data.append(transaction.getDate()).append("|");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        data.append(transaction.getTime().format(timeFormatter)).append("|");
        data.append(transaction.getDescription()).append("|");
        data.append(transaction.getVendor()).append("|");
        data.append(transaction.getAmount());
        return data.toString();
    }
}
