package main;

import imp.Bills;
import imp.Customers;
import imp.Payment;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainF {
    static Scanner scan = new Scanner(System.in);

    static Customers cusConf = new Customers();
    static Bills accConf = new Bills();
    static Payment py = new Payment();
    public static void main(String[] args) {
        int opt;
        do {
            try {
                System.out.println("\n\t=== Electric Billing System ===\n");
                System.out.println("1. Customers\n"
                        + "2. Bills\n"
                        + "3. Payment\n"
                        + "4. Exit");

                System.out.print("\nEnter Option: ");
                opt = scan.nextInt();
                scan.nextLine();
                System.out.println("");

                switch (opt) {
                    case 1:
                        System.out.println("------------------------------------------------------------------");
                        cusConf.manageCustomers(scan);
                        break;

                    case 2:
                        System.out.println("------------------------------------------------------------------");
                        accConf.manageBills(scan);
                        break;

                    case 3:
                        System.out.println("------------------------------------------------------------------");
                        py.managePayments(scan);
                        break;

                    case 4:
                        System.out.println("Exiting...");
                        System.out.println("------------------------------------------------------------------");
                        break;

                    default:
                        System.out.println("Invalid Option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scan.nextLine();
                opt = -1;
            }
        } while (opt != 4);
    }

    
}
