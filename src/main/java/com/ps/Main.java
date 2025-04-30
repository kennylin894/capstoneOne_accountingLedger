package com.ps;

import java.util.Scanner;

public class Main {
    static Scanner intScanner = new Scanner(System.in);
    static Scanner stringScanner = new Scanner(System.in);
    static Scanner menuScanner = new Scanner(System.in);

    public static void main(String[] args) {
        String mainMenuCommand;
        do {
            System.out.println("Welcome to the Banking Home Screen");
            System.out.println("==================================");
            System.out.println("Please choose an option:");
            System.out.println("[D] Add Deposit");
            System.out.println("[P] Make Payment (Debit)");
            System.out.println("[L] Ledger");
            //self added
            System.out.println("[S] Summary");
            System.out.println("[X] Exit");
            mainMenuCommand = menuScanner.nextLine();
            switch (mainMenuCommand.toUpperCase()) {
                case "D":
                    HelperMethods.addDepositMenuOptions();
                    System.out.println();
                    break;
                case "P":
                    HelperMethods.makePaymentMenuOptions();
                    System.out.println();
                    break;
                case "L":
                    HelperMethods.ledgerMenuOptions();
                    System.out.println();
                    break;
                case "S":
                    HelperMethods.summaryMenuOptions();
                    break;
                default:
            }
        } while (!mainMenuCommand.equalsIgnoreCase("x"));
        intScanner.close();
        stringScanner.close();
        menuScanner.close();
        System.out.println("Thanks for using our Accounting Ledger Application!");
    }
}