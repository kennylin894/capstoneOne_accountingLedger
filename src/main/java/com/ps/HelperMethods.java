package com.ps;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class HelperMethods {
    static LocalDate today = LocalDate.now();
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
            System.out.println("Welcome to the ledger screen.");
            System.out.println("=====================");
            System.out.println("Please choose an option:");
            System.out.println("[A] All - (Display all enteries)");
            System.out.println("[D] Deposits - (Display only the entries that are deposits into the account)");
            System.out.println("[P] Payments - (Display only the negative entries or payments)");
            System.out.println("[R] Reports - (Search for reports/Custom Search)");
            System.out.println("[H] Home - (Return Home)");
            ledgerMenu = Main.stringScanner.nextLine();
            //user wants to see all transactions
            if (ledgerMenu.equalsIgnoreCase("a")) {
                System.out.println("These are all the transactions. (Newest on top)");
                System.out.println("===============================================");
                for (Transaction transaction : readTransactionFile()) {
                    System.out.println(toFormatData(transaction));
                }
                System.out.println();
                //user only wants to see deposits
            } else if (ledgerMenu.equalsIgnoreCase("d")) {
                System.out.println("These are all the deposits. (Newest on top)");
                System.out.println("=======================================");
                for (Transaction transaction : readTransactionFile()) {
                    if (transaction.getAmount() > 0) {
                        System.out.println(toFormatData(transaction));
                    }
                }
                System.out.println();
                //user wants to see all payments
            } else if (ledgerMenu.equalsIgnoreCase("p")) {
                System.out.println("These are all the payments. (Newest on top)");
                System.out.println("===============================================");
                for (Transaction transaction : readTransactionFile()) {
                    if (transaction.getAmount() < 0) {
                        System.out.println(toFormatData(transaction));
                    }
                }
                System.out.println();
            } else if (ledgerMenu.equalsIgnoreCase("r")) {
                int reportMenuInput;
                do {
                    System.out.println("Please choose a filtering option.");
                    System.out.println("=================================");
                    System.out.println("[1] Month to Date");
                    System.out.println("[2] Previous Month");
                    System.out.println("[3] Year to Date");
                    System.out.println("[4] Previous Year");
                    System.out.println("[5] Vendor");
                    System.out.println("[6] Custom Search");
                    System.out.println("[0] Back to ledger");
                    reportMenuInput = Main.intScanner.nextInt();
                    switch (reportMenuInput) {
                        //show from start of this month -> todayv
                        case 1:
                            LocalDate firstDayOfMonth = today.withDayOfMonth(1);
                            System.out.println("Here are the reports from start of the month to today.");
                            System.out.println("=====================================================");
                            boolean found1 = false;
                            for (Transaction transaction : readTransactionFile()) {
                                if (!transaction.getDate().isBefore(firstDayOfMonth) && !transaction.getDate().isAfter(today)) {
                                    System.out.println(toFormatData(transaction));
                                    found1 = true;
                                }
                            }
                            if (!found1) {
                                System.out.println("There are no reports found.");
                            }
                            System.out.println();
                            break;
                        //prev month to date
                        case 2:
                            LocalDate firstDayPrevMonth = today.minusDays(1).withDayOfMonth(1);
                            LocalDate lastDayPrevMonth = today.withDayOfMonth(1).minusDays(1);
                            System.out.println("Here are the reports from previous month to today.");
                            System.out.println("============================================");
                            boolean found2 = false;
                            for (Transaction transaction : readTransactionFile()) {
                                if (!transaction.getDate().isBefore(firstDayPrevMonth) && !transaction.getDate().isAfter(lastDayPrevMonth)) {
                                    System.out.println(toFormatData(transaction));
                                    found2 = true;
                                }
                            }
                            if (!found2) {
                                System.out.println("There are no reports found.");
                            }
                            System.out.println();
                            break;
                        //Year to date
                        case 3:
                            LocalDate startOfYear = today.withDayOfYear(1);
                            System.out.println("These are all the reports from year to date.");
                            System.out.println("============================================");
                            boolean found3 = false;
                            for (Transaction transaction : readTransactionFile()) {
                                if (!transaction.getDate().isBefore(startOfYear) && !transaction.getDate().isAfter(today)) {
                                    System.out.println(toFormatData(transaction));
                                    found3 = true;
                                }
                            }
                            if (!found3) {
                                System.out.println("There are no reports found.");
                            }
                            System.out.println();
                            break;
                        //prev year
                        case 4:
                            int previousYear = today.getYear() - 1;
                            LocalDate startOfPrevYear = LocalDate.of(previousYear, 1, 1);
                            LocalDate endOfPrevYear = LocalDate.of(previousYear, 12, 31);
                            System.out.println("These are all the reports from the previous year.");
                            System.out.println("================================================");
                            boolean found4 = false;
                            for (Transaction transaction : readTransactionFile()) {
                                if (!transaction.getDate().isBefore(startOfPrevYear) && !transaction.getDate().isAfter(endOfPrevYear)) {
                                    System.out.println(toFormatData(transaction));
                                    found4 = true;
                                }
                            }
                            if (!found4) {
                                System.out.println("There are no reports found.");
                            }
                            System.out.println();
                            break;
                        //vendor search
                        case 5:
                            System.out.println("Please enter the vendor: ");
                            System.out.println("========================");
                            String vendor = Main.stringScanner.nextLine();
                            System.out.println("These are all reports under the vendor: \"" + vendor + "\".");
                            System.out.println("========================================================");
                            boolean found5 = false;
                            for (Transaction transaction : readTransactionFile()) {
                                if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                                    System.out.println(toFormatData(transaction));
                                    found5 = true;
                                }
                            }
                            if (!found5) {
                                System.out.println("There are no reports from vendor: \"" + vendor + "\" found.");

                            }
                            System.out.println();
                            break;
                        //custom search
                        case 6:
                            customSearchReportMenu();
                            break;
                    }
                } while (reportMenuInput != 0);
            } else if (!ledgerMenu.equalsIgnoreCase("h")) {
                System.out.println("Error, bad input try again.");
            }
        } while (!ledgerMenu.equalsIgnoreCase("H"));
    }

    public static void customSearchReportMenu() {
        System.out.println("Welcome to custom search.");
        System.out.println("=========================");
        System.out.println("Enter values you want to filter.");
        System.out.println("If you dont want to enter a value, please press enter.");
        System.out.println("It will skip that filter.");
        System.out.println();
        System.out.println("[1] Start date (YYYY-MM-DD)");
        String sdate = Main.stringScanner.nextLine();
        LocalDate startDate = null;
        if(!sdate.isBlank())
        {
            startDate = LocalDate.parse(sdate);
        }
        System.out.println("[2] End date (YYYY-MM-DD)");
        String edate = Main.stringScanner.nextLine();
        LocalDate endDate = null;
        if(!edate.isBlank())
        {
            endDate = LocalDate.parse(edate);
        }
        System.out.println("[3] Description");
        String despcription = Main.stringScanner.nextLine();
        System.out.println("[4] Vendor");
        String vendor = Main.stringScanner.nextLine();
        System.out.println("[5] Amount");
        String samount = Main.stringScanner.nextLine();
        System.out.println("These are all the records found.");
        System.out.println("=================================");
        int count = 0;
        for(Transaction transaction: readTransactionFile())
        {
            boolean matches = true;
            if(startDate != null && transaction.getDate().isBefore(startDate))
            {
                matches = false;
            }
            if(endDate != null && transaction.getDate().isAfter(endDate))
            {
                matches = false;
            }
            if(!despcription.isBlank() && !transaction.getDescription().equalsIgnoreCase(despcription))
            {
                matches = false;
            }
            if(!vendor.isBlank() && !transaction.getVendor().equalsIgnoreCase(vendor))
            {
                matches = false;
            }
            if (!samount.isBlank()) {
                try {
                    double amount = Double.parseDouble(samount);
                    if (transaction.getAmount() != amount) {
                        matches = false;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount input.");
                }
            }
            if(matches)
            {
                System.out.println(toFormatData(transaction));
                count++;
            }
        }
        if(count == 0)
        {
            System.out.println("No records were found.");

        }
        System.out.println();
    }

    public static void addDepositMenuOptions() {
        System.out.println("Welcome to the deposit screen.");
        System.out.println("==============================");
        System.out.println("Enter deposit description: ");
        String description = Main.stringScanner.nextLine();
        System.out.println("Enter the vendor: ");
        String vendor = Main.stringScanner.nextLine();
        System.out.println("Enter the total amount: ");
        String amount = Main.stringScanner.nextLine();
        while (Double.parseDouble(amount) <= 0) {
            System.out.println("Invalid deposit, please try again.");
            System.out.println("Enter the total amount: ");
            amount = Main.stringScanner.nextLine();
            System.out.println();
        }
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Transaction transaction = new Transaction(date, time, description, vendor, Double.parseDouble(amount));
        writeToFile(transaction);
        System.out.println("Your deposit has been successfully processed.");
        System.out.println();
    }

    public static void makePaymentMenuOptions() {
        System.out.println("Welcome to the payment screen.");
        System.out.println("==============================");
        System.out.println("Enter payment description: ");
        String description = Main.stringScanner.nextLine();
        System.out.println("Enter the vendor: ");
        String vendor = Main.stringScanner.nextLine();
        System.out.println("Enter the total amount: ");
        String amount = Main.stringScanner.nextLine();
        if (Double.parseDouble(amount) > 0) {
            Double strAmount;
            strAmount = Double.parseDouble(amount) * -1;
            amount = String.valueOf(strAmount);
        }
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        Transaction transaction = new Transaction(date, time, description, vendor, Double.parseDouble(amount));
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
