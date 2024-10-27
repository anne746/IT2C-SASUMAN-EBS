package it2csasumanbill;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Customers {
    config conf = new config();

    public void manageCustomers(Scanner scan) {
        int opt;
        do {    
            try {
                System.out.println("\n\t=== Customer Management ===\n");
                System.out.println("1. View All Customers\n"
                        + "2. Add a Customer\n"
                        + "3. Remove a Customer\n"
                        + "4. Edit a Customer\n"
                        + "5. Go back..");

                System.out.print("\nEnter Option: ");
                opt = scan.nextInt();
                scan.nextLine();

                switch (opt) {
                    case 1:
                        System.out.println("------------------------------------------------------------------");
                        viewCustomers("SELECT * FROM tbl_customers");
                        break;
                    case 2:
                        System.out.println("------------------------------------------------------------------");
                        addCustomer(scan);
                        break;
                    case 3:
                        System.out.println("------------------------------------------------------------------");
                        deleteCustomer(scan);
                        break;
                    case 4:
                        System.out.println("------------------------------------------------------------------");
                        editCustomer(scan);
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

    public void viewCustomers(String query) { 
        System.out.println("\n\t\t\t\t\t   === CUSTOMER LIST ===\n");
        String[] Headers = {"Customer ID", "First Name", "Last Name", "Address"};
        String[] Columns = {"id", "c_fname", "c_lname", "c_address"};
        conf.viewRecords(query, Headers, Columns);
    }

    public void addCustomer(Scanner scan) {
        System.out.println("\n\t\t=== ADD A NEW CUSTOMER ===\n");
        
        System.out.print("First Name: ");
        String fname = scan.nextLine();
        
        System.out.print("Last Name: ");
        String lname = scan.nextLine();
        
        System.out.print("Address: ");
        String address = scan.nextLine();
        
        String sql = "INSERT INTO tbl_customers (c_fname, c_lname, c_address) VALUES (?, ?, ?)";
        conf.addRecord(sql, fname, lname, address);
    }

    public void deleteCustomer(Scanner scan) {
        System.out.println("\n\t\t=== REMOVE A CUSTOMER ===\n");
        
        System.out.print("Enter Customer ID to delete: ");
        int id = scan.nextInt();
        
        String sql = "DELETE FROM tbl_customers WHERE id = ?";
        conf.deleteRecord(sql, id);
    }

    public void editCustomer(Scanner scan) {
        System.out.println("\n\t\t=== EDIT A CUSTOMER ===\n");
        
        int id;
        do{
            System.out.print("\nEnter Customer ID: ");
            id = scan.nextInt();
            if(!conf.doesIDExist("tbl_customers", id)){
                System.out.println("Customer ID Doesn't Exist.");
            }
        }while(!conf.doesIDExist("tbl_customers", id));
        scan.nextLine();
        
        System.out.println("\nEnter Updated Customer Details:");
        
        System.out.print("New First Name: ");
        String fname = scan.nextLine();
        
        System.out.print("New Last Name: ");
        String lname = scan.nextLine();
        
        System.out.print("New Address: ");
        String address = scan.nextLine();
        
        String sql = "UPDATE tbl_customers SET c_fname = ?, c_lname = ?, c_address = ? WHERE id = ?";
        conf.updateRecord(sql, fname, lname, address, id);
    }
}
