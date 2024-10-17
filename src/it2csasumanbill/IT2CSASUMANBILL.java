package it2csasumanbill;

import java.util.InputMismatchException;
import java.util.Scanner;

public class IT2CSASUMANBILL {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        IT2CSASUMANBILL bill = new IT2CSASUMANBILL();
        
        String response = "";
        int action;
        
        do {
            System.out.println("\n1. ADD");
            System.out.println("2. VIEW");
            System.out.println("3. UPDATE");
            System.out.println("4. DELETE");
            System.out.println("5. EXIT");
            
            try {
                System.out.print("\nEnter Action: ");
                action = sc.nextInt();

                switch (action) {
                    case 1:
                        bill.addCustomer();
                        break;
                    case 2:
                        bill.viewCustomer();
                        break;
                    case 3:
                        bill.updateCustomer();
                        break;
                    case 4:
                        bill.deleteCustomer();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }

                if (action != 5) {
                    System.out.print("\nContinue (YES/NO): ");
                    response = sc.next();
                } else {
                    response = "no";
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                sc.nextLine();
                action = -1;
            }
            
        } while (response.equalsIgnoreCase("yes"));
        
        System.out.println("Thank you. See you again.");
    }
    
    private void addCustomer() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("\nEnter First Name: ");
        String fname = sc.nextLine();
        
        System.out.print("Enter Last Name: ");
        String lname = sc.nextLine();
        
        System.out.print("Enter Address: ");
        String address = sc.nextLine();
        
        System.out.print("Enter Account UNIQUE ID: ");
        String accid = sc.nextLine();
        
        System.out.print("Units Consumed (kWh): ");
        String kwh = sc.nextLine();
        System.out.println("");
        
        String sql = "INSERT INTO tbl_customer (c_fname, c_lname, c_address, c_accid, c_unitKWH) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, fname, lname, address, accid, kwh);
        
    }

    private void viewCustomer() {
        System.out.println("");
        String aqry = "SELECT * FROM tbl_customer";
        String[] headers = {"ID", "First Name", "Last Name", "Address", "Acc Unique ID", "Units (kWh)"};
        String[] columns = {"id", "c_fname", "c_lname", "c_address", "c_accid", "c_unitKWH"};
        
        config conf = new config();
        conf.viewRecords(aqry, 25, headers, columns);
    }

    private void updateCustomer() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("\nEnter Customer ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (!conf.doesIDExist("tbl_customer", id)) {
            System.out.println("Customer ID doesn't exist.");
            return;
        }
        
        System.out.println("Updating details for Customer ID: " + id);
        
        System.out.print("\nNew First Name: ");
        String fname = sc.nextLine();

        System.out.print("New Last Name: ");
        String lname = sc.nextLine();
        
        System.out.print("New Address: ");
        String address = sc.nextLine();
        
        System.out.print("New Account UNIQUE ID: ");
        String accid = sc.nextLine();
        
        System.out.print("New Units Consumed (kWh): ");
        String kwh = sc.nextLine();
        
        System.out.println("");
        String sql = "UPDATE tbl_customer SET c_fname = ?, c_lname = ?, c_address = ?, c_accid = ?, c_unitKWH = ? WHERE id = ?";
        conf.updateRecord(sql, fname, lname, address, accid, kwh, id);
        
    }

    private void deleteCustomer() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        System.out.print("\nEnter Customer ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();
        
        if (!conf.doesIDExist("tbl_customer", id)) {
            System.out.println("Customer ID doesn't exist.");
            return;
        }
        
        String sql = "DELETE FROM tbl_customer WHERE id = ?";
        conf.deleteRecord(sql, id);
        
    }
}
