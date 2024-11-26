package imp;

import main.config;
import java.util.InputMismatchException;
import java.util.*;
import java.util.regex.Pattern;

public class Customers {
    config conf = new config();
   
    String query = null;
    private int c_id;
    // Main method for customer management
    public void manageCustomers(Scanner scan) {
        int opt = 0;
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
                scan.nextLine(); // Consume the newline character

                switch (opt) {
                    case 1:
                        System.out.println("------------------------------------------------------------------");
                        viewCustomers("SELECT * FROM tbl_customers"); // View all customers
                        break;
                    case 2:
                        System.out.println("------------------------------------------------------------------");
                        addCustomer(scan); // Add a new customer
                        break;
                    case 3:
                        System.out.println("------------------------------------------------------------------");
                        deleteCustomer(scan); // Remove a customer
                        break;
                    case 4:
                        System.out.println("------------------------------------------------------------------");
                        editCustomer(scan); // Edit customer details
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
                scan.nextLine(); // Clear the invalid input
            }
        } while (opt != 5); // Exit when option 5 (Go back) is selected
    }

        // Method to view all customers
    public void viewCustomers(String query) {
        System.out.println("\n\t\t\t\t\t   === CUSTOMER LIST ===\n");
        String[] headers = {"Customer ID", "First Name", "Last Name", "Address", "Email", "Meter Number"};
        String[] columns = {"c_id", "c_fname", "c_lname", "c_address", "c_email", "meter_number"};

        // Call viewRecords without passing c_id
        conf.viewRecords(query, headers, columns);
    }


    public void addCustomer(Scanner scan) {
        System.out.println("\n\t\t=== ADD A NEW CUSTOMER ===\n");

        System.out.print("First Name: ");
        String fname = scan.nextLine();

        System.out.print("Last Name: ");
        String lname = scan.nextLine();

        System.out.print("Address: ");
        String address = scan.nextLine();

        // Email validation loop
        String email;
        do {
            System.out.print("Email: ");
            email = scan.nextLine();
            if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please enter a valid email (e.g., user@example.com).");
            } else if (conf.recordExists("tbl_customers", "c_email", email)) {
                System.out.println("This email is already in use. Please enter a different email.");
                email = null; // Reset email to re-prompt the user
            }
        } while (email == null);

        // Meter number validation loop
        String meterNumber;
        do {
            System.out.print("Meter Number: ");
            meterNumber = scan.nextLine();
            if (!meterNumber.matches("\\d+")) { // Check if it contains only digits
                System.out.println("Invalid meter number. Please enter numbers only.");
            } else if (conf.recordExists("tbl_customers", "meter_number", meterNumber)) {
                System.out.println("This meter number is already assigned. Please enter a different meter number.");
                meterNumber = null; // Reset to re-prompt the user
            }
        } while (!meterNumber.matches("\\d+"));

        String sql = "INSERT INTO tbl_customers (c_fname, c_lname, c_address, c_email, meter_number) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, fname, lname, address, email, meterNumber);
    }

    // Method to delete a customer
    public void deleteCustomer(Scanner scan) {
        System.out.println("\n\t\t=== REMOVE A CUSTOMER ===\n");
        viewCustomers("SELECT * FROM tbl_customers"); 
        int id = getValidCustomerId(scan);
        String sql = "DELETE FROM tbl_customers WHERE c_id = ?";
        conf.deleteRecord(sql, id);
    }

    // Method to edit a customer's details
    public void editCustomer(Scanner scan) {
        System.out.println("\n\t\t=== EDIT A CUSTOMER ===\n");
        viewCustomers("SELECT * FROM tbl_customers"); 
        int id = getValidCustomerId(scan);

        // Display options to edit or go back
        System.out.println("\nSelect the field to edit:");
        System.out.println("1. First Name\n2. Last Name\n3. Address\n4. Email\n5. Go Back");

        int choice;
        do {
            System.out.print("\nEnter option: ");
            while (!scan.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number between 1 and 5: ");
                scan.next();
            }
            choice = scan.nextInt();
            scan.nextLine(); // Consume newline
        } while (choice < 1 || choice > 5);

        if (choice == 5) {
            System.out.println("Going back to the previous menu...");
            return; // Exit the editCustomer method to go back to the previous menu
        }

        String sql = "";
        switch (choice) {
            case 1:
                System.out.print("Enter new First Name: ");
                String fname = scan.nextLine();
                sql = "UPDATE tbl_customers SET c_fname = ? WHERE c_id = ?";
                conf.updateRecord(sql, fname, id);
                break;
            case 2:
                System.out.print("Enter new Last Name: ");
                String lname = scan.nextLine();
                sql = "UPDATE tbl_customers SET c_lname = ? WHERE c_id = ?";
                conf.updateRecord(sql, lname, id);
                break;
            case 3:
                System.out.print("Enter new Address: ");
                String address = scan.nextLine();
                sql = "UPDATE tbl_customers SET c_address = ? WHERE c_id = ?";
                conf.updateRecord(sql, address, id);
                break;
            case 4:
                String email;
                do {
                    System.out.print("Enter new Email: ");
                    email = scan.nextLine();
                    if (!isValidEmail(email)) {
                        System.out.println("Invalid email format. Please enter a valid email.");
                    } else if (conf.doesFieldExistExcludeId("tbl_customers", "c_email", email, id)) {
                        System.out.println("This email is already in use. Please enter a different email.");
                        email = null;
                    }
                } while (email == null);
                sql = "UPDATE tbl_customers SET c_email = ? WHERE c_id = ?";
                conf.updateRecord(sql, email, id);
                break;
        }

        System.out.println("Customer details updated successfully!");
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailRegex, email);
    }

    // Helper method to get a valid customer ID with checks
    private int getValidCustomerId(Scanner scan) {
        int id;
        do {
            System.out.print("\nEnter Customer ID: ");
            while (!scan.hasNextInt()) {
                System.out.print("Invalid input. Please enter a valid number.");
                scan.next(); // Consume invalid input
            }
            id = scan.nextInt();
            scan.nextLine(); // Consume newline
            if (!conf.doesIDExist("tbl_customers", id)) {
                System.out.println("Customer ID doesn't exist. Please enter a valid ID.");
                id = -1;
            }
        } while (id == -1);
        return id;
    }
    
}
