package com.ps;

public class Ledger {

    public static void ledgerMenuOptions()
    {
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
}
