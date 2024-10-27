package it2csasumanbill;

import java.util.InputMismatchException;
import java.util.Scanner;

public class IT2CSASUMANBILL {
    static Scanner scan = new Scanner(System.in);

    static Customers cusConf = new Customers();
    static Accounts accConf = new Accounts();

    public static void main(String[] args) {
        int opt;
        do {
            try {
                System.out.println("\n\t=== IT2CSA Billing System ===\n");
                System.out.println("1. Customers\n"
                        + "2. Accounts\n"
                        + "3. Reports\n"
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
                        accConf.manageAccounts(scan);
                        break;

                    case 3:
                        System.out.println("------------------------------------------------------------------");
                        generateReport();
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

    private static void generateReport() {
        config conf = new config();

        cusConf.viewCustomers("SELECT * FROM tbl_customers");

        int id;
        do {
            System.out.print("\nEnter Customer ID: ");
            id = scan.nextInt();
            if (!conf.doesIDExist("tbl_customers", id)) {
                System.out.println("Customer ID Doesn't Exist.");
            }
        } while (!conf.doesIDExist("tbl_customers", id));

        String fname = conf.getDataFromID("tbl_customers", id, "c_fname");
        String lname = conf.getDataFromID("tbl_customers", id, "c_lname");
        String name = fname + " " + lname;
        String address = conf.getDataFromID("tbl_customers", id, "c_address");

        System.out.printf("\n%72s\n\n", "=== INDIVIDUAL REPORT ===");
        System.out.println("Customer ID   : " + id); 
        System.out.println("Customer Name : " + name);
        System.out.println("Address       : " + address);
        
        System.out.println("\nAccounts:");
        String sql = "SELECT acc.id, acc.unit_kwh "
                + "FROM tbl_accounts acc "
                + "JOIN tbl_customers cus ON acc.c_id = cus.id "
                + "WHERE cus.id = " + id;
        String[] columnHeaders = {"Account ID", "Energy Consumption (kWh)"};
        String[] columnNames = {"id", "unit_kwh"};
        conf.viewRecords(sql, columnHeaders, columnNames);
    }
}
