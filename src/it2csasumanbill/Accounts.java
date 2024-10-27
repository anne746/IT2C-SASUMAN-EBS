package it2csasumanbill;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Accounts {
    config conf = new config();

    public void manageAccounts(Scanner scan) {
        int opt;
        do {    
            try {
                System.out.println("\n\t=== Account Management ===\n");
                System.out.println("1. View All Accounts\n"
                        + "2. Add an Account\n"
                        + "3. Remove an Account\n"
                        + "4. Edit an Account\n"
                        + "5. Go back..");

                System.out.print("\nEnter Option: ");
                opt = scan.nextInt();
                scan.nextLine();

                switch (opt) {
                    case 1:
                        System.out.println("------------------------------------------------------------------");
                        viewAccounts("SELECT * FROM tbl_accounts");
                        break;
                    case 2:
                        System.out.println("------------------------------------------------------------------");
                        addAccount(scan);
                        break;
                    case 3:
                        System.out.println("------------------------------------------------------------------");
                        deleteAccount(scan);
                        break;
                    case 4:
                        System.out.println("------------------------------------------------------------------");
                        editAccount(scan);
                        break;           
                    case 5:
                        System.out.println("\nGoing back to Main Menu...");
                        System.out.println("------------------------------------------------------------------");
                        break;
                    default:
                        System.out.println("Invalid Option.");
                }
                System.out.println("\n");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scan.nextLine(); 
                opt = -1; 
            }
        } while (opt != 5);
    }

    public void viewAccounts(String query) { 
        System.out.println("\n\t\t\t     === ACCOUNT LIST ===\n");
        String[] Headers = {"Account ID", "Customer ID", "Energy Consumption (kWh)"};
        String[] Columns = {"id", "c_id", "unit_kwh"};
        conf.viewRecords(query, Headers, Columns);
    }

    public void addAccount(Scanner scan) {
        System.out.println("\n\t\t=== ADD A NEW ACCOUNT ===\n");
        
        int c_id;
        do{
            System.out.print("\nNew Customer ID: ");
            c_id = scan.nextInt();
            if(!conf.doesIDExist("tbl_customers", c_id)){
                System.out.println("Customer ID Doesn't Exist.");
            }
        }while(!conf.doesIDExist("tbl_customers", c_id));
        
        System.out.print("Energy Consumption (kWh): ");
        double unit_kwh = scan.nextDouble();
        
        String sql = "INSERT INTO tbl_accounts (c_id, unit_kwh) VALUES (?, ?)";
        conf.addRecord(sql, c_id, unit_kwh);
    }

    public void deleteAccount(Scanner scan) {
        System.out.println("\n\t\t=== REMOVE AN ACCOUNT ===\n");
        
        System.out.print("Enter Account ID to delete: ");
        int id = scan.nextInt();
        
        String sql = "DELETE FROM tbl_accounts WHERE id = ?";
        conf.deleteRecord(sql, id);
    }

    public void editAccount(Scanner scan) {
        System.out.println("\n\t\t=== EDIT AN ACCOUNT ===\n");
        
        int id;
        do{
            System.out.print("\nEnter Account ID: ");
            id = scan.nextInt();
            if(!conf.doesIDExist("tbl_accounts", id)){
                System.out.println("Account ID Doesn't Exist.");
            }
        }while(!conf.doesIDExist("tbl_accounts", id));
        scan.nextLine();
        
        System.out.println("\nEnter Updated Account Details:");
        
        int c_id;
        do{
            System.out.print("New Customer ID: ");
            c_id = scan.nextInt();
            if(!conf.doesIDExist("tbl_customers", c_id)){
                System.out.println("Customer ID Doesn't Exist.");
            }
        }while(!conf.doesIDExist("tbl_customers", c_id));
        
        System.out.print("New Energy Consumption (kWh): ");
        double unit_kwh = scan.nextDouble();
        
        String sql = "UPDATE tbl_accounts SET c_id = ?, unit_kwh = ? WHERE id = ?";
        conf.updateRecord(sql, c_id, unit_kwh, id);
    }
}
