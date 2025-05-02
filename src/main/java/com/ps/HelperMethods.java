package com.ps;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class HelperMethods {
    static LocalDate today = LocalDate.now();
    static Comparator<Transaction> comparator = new Comparator<Transaction>() {
        //lexicographical matches the chronological order or newest date so this sorts it
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
            System.out.println("=============================");
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
                System.out.println("===========================================");
                for (Transaction transaction : readTransactionFile()) {
                    if (transaction.getAmount() > 0) {
                        System.out.println(toFormatData(transaction));
                    }
                }
                System.out.println();
                //user wants to see all payments
            } else if (ledgerMenu.equalsIgnoreCase("p")) {
                System.out.println("These are all the payments. (Newest on top)");
                System.out.println("===========================================");
                for (Transaction transaction : readTransactionFile()) {
                    if (transaction.getAmount() < 0) {
                        System.out.println(toFormatData(transaction));
                    }
                }
                System.out.println();

            }
            //user wants to see reports
            else if (ledgerMenu.equalsIgnoreCase("r"))
            {
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
                            System.out.println("======================================================");
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
                            System.out.println("==================================================");
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
                            StringBuilder reportResults = new StringBuilder();
                            reportResults.append("These are all reports under the vendor: ");
                            reportResults.append("\"").append(vendor).append("\".");
                            int resultLen = reportResults.toString().length();
                            System.out.println(reportResults);
                            for (int i = 0; i < resultLen; i++) {
                                System.out.print("=");
                            }
                            System.out.println();
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
        if (!sdate.isBlank()) {
            startDate = LocalDate.parse(sdate);
        }
        System.out.println("[2] End date (YYYY-MM-DD)");
        String edate = Main.stringScanner.nextLine();
        LocalDate endDate = null;
        if (!edate.isBlank()) {
            endDate = LocalDate.parse(edate);
        }
        System.out.println("[3] Description");
        String despcription = Main.stringScanner.nextLine();
        System.out.println("[4] Vendor");
        String vendor = Main.stringScanner.nextLine();
        System.out.println("[5] Amount");
        String samount = Main.stringScanner.nextLine();
        System.out.println("These are all the records found.");
        System.out.println("================================");
        int count = 0;
        for (Transaction transaction : readTransactionFile()) {
            /*instead of using a lot of ifs, I made the custom sorting algorithm this way
            basically if the value is not null (user entered a value they want to search by)
            we check that value in that specific category (ex. user enters "amazon" as vendor)*/
            boolean matches = true;
            /*if user entered a start date, and the transaction were looking at HAS a date that is before
            //the date the user entered, it means this transaction were looking at doesnt meet the criteria, so this
            transaction doesn't get printed out*/
            if (startDate != null && transaction.getDate().isBefore(startDate)) {
                matches = false;
            }
            /*if user entered an end date, and if the date is after the end date we know this trans is not filtered to
            the users needs*/
            if (endDate != null && transaction.getDate().isAfter(endDate)) {
                matches = false;
            }
            if (!despcription.isBlank() && !transaction.getDescription().equalsIgnoreCase(despcription)) {
                matches = false;
            }
            if (!vendor.isBlank() && !transaction.getVendor().equalsIgnoreCase(vendor)) {
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
            /* so lets so the user entered only "amazon" as the vendor, it goes down the if statements, it only does the vendor if
            since all the other values were empty since the user skipped them so we dont check them. When we get to the vendor
            check if basically ask first
            1. Did the user give us a value for vendor?
            2. If so does any of the transactions match the vendor amazon?
            Ex.
            2025-04-20|10:30:00|referral bonus|StartupX|300.00
            2025-05-01|15:38:46|Computer|Amazon|1000.0
            The ifs would run through the first transaction and check only the vendor and since the vendor doesnt match
            amazon the first trans is skipped and not printed since it failed the match (if there is one match = false
            we know this isnt what we want)
            Now for the second one it checks the vendor and the name names so it doesnt get flagged as false so matches
            doesnt turn false meaning we print this transaction since it passed all the checks.
            Hopefully this explanation was good or helped in understanding this a little bit.
            */
            if (matches) {
                System.out.println(toFormatData(transaction));
                count++;
            }
        }
        if (count == 0) {
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
        StringBuilder depositMessage = new StringBuilder();
        depositMessage.append("Your deposit of $").append(amount).append(" has been successfully processed.");
        System.out.println(depositMessage);
        for (int i = 0; i < depositMessage.toString().length(); i++) {
            System.out.print("=");
        }
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
        StringBuilder paymentMessage = new StringBuilder();
        paymentMessage.append("Your payment of $").append(amount).append(" has been successfully processed.");
        System.out.println(paymentMessage);
        for (int i = 0; i < paymentMessage.toString().length(); i++) {
            System.out.print("=");
        }
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

    //new selfmade method
    public static void summaryMenuOptions() {
        String summaryMenuOptions;
        do {
            System.out.println("Welcome to the summary dashboard.");
            System.out.println("=================================");
            System.out.println("[T] Total Transactions");
            System.out.println("[D] Sum of deposits");
            System.out.println("[P] Sum of payments");
            System.out.println("[H] Home");
            summaryMenuOptions = Main.stringScanner.nextLine().toUpperCase();
            int totalMenuOptions;
            switch (summaryMenuOptions) {
                case "T":
                    System.out.println("Please choose an option: ");
                    System.out.println("========================");
                    System.out.println("[1] Total number of transactions (In general)");
                    System.out.println("[2] Total number of transactions this month");
                    System.out.println("[3] Total number of transactions this year");
                    System.out.println("[4] Exit");
                    totalMenuOptions = Main.intScanner.nextInt();
                    //total trans in gen
                    if (totalMenuOptions == 1) {
                        int count = 0;
                        for (Transaction transaction : readTransactionFile()) {
                            count++;
                        }
                        System.out.println("There is a total of " + count + " transactions.");
                        System.out.println();
                    }
                    //total trans month
                    else if (totalMenuOptions == 2) {
                        int count = 0;
                        LocalDate now = LocalDate.now();
                        for (Transaction transaction : readTransactionFile()) {
                            if (transaction.getDate().getMonthValue() == now.getMonthValue()) {
                                count++;
                            }
                        }
                        StringBuilder totalTransactionsMonth = new StringBuilder();
                        totalTransactionsMonth.append("There is a total of ").append(count).append(" during the month of ");
                        totalTransactionsMonth.append(now.getMonth().toString()).append(" ").append(now.getYear()).append(".");
                        System.out.println(totalTransactionsMonth);
                        for (int i = 0; i < totalTransactionsMonth.toString().length(); i++) {
                            System.out.print("=");
                        }
                        System.out.println();
                    }
                    //total trans year
                    else if (totalMenuOptions == 3) {
                        int count = 0;
                        LocalDate now = LocalDate.now();
                        for (Transaction transaction : readTransactionFile()) {
                            if (transaction.getDate().getYear() == now.getYear()) {
                                count++;
                            }
                        }
                        StringBuilder totalTransactionsYear = new StringBuilder();
                        totalTransactionsYear.append("There is a total of ").append(count).append(" transactions during the year: ");
                        totalTransactionsYear.append(now.getYear()).append(".");
                        System.out.println(totalTransactionsYear);
                        for (int i = 0; i < totalTransactionsYear.toString().length(); i++) {
                            System.out.print("=");
                        }
                        System.out.println();
                    } else if (totalMenuOptions == 4) {
                        break;
                    }
                    break;
                case "D":
                    System.out.println("Please choose an option: ");
                    System.out.println("========================");
                    System.out.println("[1] Total sum of deposits (In general)");
                    System.out.println("[2] Total sum of deposits this month");
                    System.out.println("[3] Total sum of deposits this year");
                    System.out.println("[4] Exit");
                    totalMenuOptions = Main.intScanner.nextInt();
                    //sum in gen
                    if (totalMenuOptions == 1) {
                        double sum = 0;
                        for (Transaction transaction : readTransactionFile()) {
                            if (transaction.getAmount() > 0) {
                                sum += transaction.getAmount();
                            }
                        }
                        System.out.printf("The total sum of deposits is: $%.2f%n", sum);
                        System.out.println();
                    }
                    //sum in month
                    else if (totalMenuOptions == 2) {
                        double sum = 0;
                        LocalDate now = LocalDate.now();
                        for (Transaction transaction : readTransactionFile()) {
                            if (transaction.getDate().getMonthValue() == now.getMonthValue()) {
                                if (transaction.getDate().getYear() == now.getYear()) {
                                    if (transaction.getAmount() > 0) {
                                        sum += transaction.getAmount();
                                    }
                                }
                            }
                        }
                        StringBuilder totalSumMonth = new StringBuilder();
                        totalSumMonth.append("The total sum of deposits is: $").append((String.format("%.2f", sum))).append(" during the month of ");
                        totalSumMonth.append(now.getMonth().toString()).append(" ").append(now.getYear()).append(".");
                        System.out.println(totalSumMonth);
                        for (int i = 0; i < totalSumMonth.toString().length(); i++) {
                            System.out.print("=");
                        }
                        System.out.println();
                    }
                    //sum in year
                    else if (totalMenuOptions == 3) {
                        double sum = 0;
                        LocalDate now = LocalDate.now();
                        for (Transaction transaction : readTransactionFile()) {
                            if (transaction.getDate().getYear() == now.getYear()) {
                                if (transaction.getAmount() > 0) {
                                    sum += transaction.getAmount();
                                }
                            }
                        }
                        StringBuilder totalSumYear = new StringBuilder();
                        totalSumYear.append("The total sum of deposits is: $").append((String.format("%.2f", sum))).append(" during the year :");
                        totalSumYear.append(now.getYear()).append(".");
                        System.out.println(totalSumYear);
                        for (int i = 0; i < totalSumYear.toString().length(); i++) {
                            System.out.print("=");
                        }
                        System.out.println();
                    }
                    //exit
                    else if (totalMenuOptions == 4) {
                        break;
                    }
                    break;
                case "P":
                    System.out.println("Please choose an option: ");
                    System.out.println("========================");
                    System.out.println("[1] Total payments (In general)");
                    System.out.println("[2] Total payments this month");
                    System.out.println("[3] Total payments this year");
                    System.out.println("[4] Exit");
                    totalMenuOptions = Main.intScanner.nextInt();
                    //sum in gen
                    if (totalMenuOptions == 1) {
                        double sum = 0;
                        for (Transaction transaction : readTransactionFile()) {
                            if (transaction.getAmount() < 0) {
                                sum += transaction.getAmount();
                            }
                        }
                        System.out.printf("The total sum of payments is: $%.2f%n", sum);
                        System.out.println();
                    }
                    //sum in month
                    else if (totalMenuOptions == 2) {
                        double sum = 0;
                        LocalDate now = LocalDate.now();
                        for (Transaction transaction : readTransactionFile()) {
                            if (transaction.getDate().getMonthValue() == now.getMonthValue()) {
                                if (transaction.getDate().getYear() == now.getYear()) {
                                    if (transaction.getAmount() < 0) {
                                        sum += transaction.getAmount();
                                    }
                                }
                            }
                        }
                        StringBuilder totalSumMonth = new StringBuilder();
                        totalSumMonth.append("The total sum of payments is: $").append((String.format("%.2f", sum))).append(" during the month of ");
                        totalSumMonth.append(now.getMonth().toString()).append(" ").append(now.getYear()).append(".");
                        System.out.println(totalSumMonth);
                        for (int i = 0; i < totalSumMonth.toString().length(); i++) {
                            System.out.print("=");
                        }
                        System.out.println();
                    } else if (totalMenuOptions == 3) {
                        double sum = 0;
                        LocalDate now = LocalDate.now();
                        for (Transaction transaction : readTransactionFile()) {
                            if (transaction.getDate().getYear() == now.getYear()) {
                                if (transaction.getAmount() < 0) {
                                    sum += transaction.getAmount();
                                }
                            }
                        }
                        StringBuilder totalSumYear = new StringBuilder();
                        totalSumYear.append("The total sum of payments is: $").append((String.format("%.2f", sum))).append(" during the year :");
                        totalSumYear.append(now.getYear()).append(".");
                        System.out.println(totalSumYear);
                        for (int i = 0; i < totalSumYear.toString().length(); i++) {
                            System.out.print("=");
                        }
                        System.out.println();
                    }
                case "H":
                    return;
                default:
                    System.out.println("Bad input, please try again.");
            }
        }
        while (summaryMenuOptions.equalsIgnoreCase("h"));
    }
}
