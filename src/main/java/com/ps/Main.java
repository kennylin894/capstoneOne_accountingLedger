package com.ps;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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
            System.out.println("[X] Exit");
            mainMenuCommand = menuScanner.nextLine();
            switch (mainMenuCommand.toUpperCase())
            {
                case "D":
                    HelperMethods.addDepositMenuOptions();
                    break;
                case "P":
                    HelperMethods.makePaymentMenuOptions();
                    break;
                case "L":
                    HelperMethods.ledgerMenuOptions();
                    break;
                default:
            }
        } while (!mainMenuCommand.equalsIgnoreCase("x"));
        System.out.println("Good Bye");
    }
}