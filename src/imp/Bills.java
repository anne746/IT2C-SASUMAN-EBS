package imp;

import main.config;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Bills {
    config conf = new config();
    Customers cs = new Customers();
    // Main method to manage bills
    public void manageBills(Scanner scan) {
        int opt;
        do {
            try {
                // Display menu for bill management
                System.out.println("\n\t=== Bill Management ===\n");
                System.out.println("1. View All Bills\n"
                        + "2. Add a Bill\n"
                        + "3. Remove a Bill\n"
                        + "4. Edit a Bill\n"
                        + "5. Go back..");

                System.out.print("\nEnter Option: ");
                opt = scan.nextInt();
                scan.nextLine();  // Consume the newline character

                // Handle different options based on user input
                switch (opt) {
                    case 1:
                        System.out.println("------------------------------------------------------------------");
                        viewBills(scan); // View all bills
                        break;
                    case 2:
                        System.out.println("------------------------------------------------------------------");
                        addBill(scan); // Add a new bill
                        break;
                    case 3:
                        System.out.println("------------------------------------------------------------------");
                        deleteBill(scan); // Delete a bill
                        break;
                    case 4:
                        System.out.println("------------------------------------------------------------------");
                        editBill(scan); // Edit an existing bill
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
                opt = -1; // Reset option to an invalid state
            }
        } while (opt != 5); // Exit when option 5 (Go back) is selected
    }

    public void viewBills(Scanner scan) {
       

        int option;
        do {
            System.out.println("\n\t\t\t     === VIEW BILLS FOR A CUSTOMER ===\n");
            System.out.println("1. View bills for a specific customer");
            System.out.println("2. View all bills");
            System.out.println("3. Go back");
            System.out.print("\nEnter your choice: ");
            option = scan.nextInt();
            scan.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    viewBillsForCustomer(scan); // Existing code for a specific customer
                    break;
                case 2:
                    viewAllBills(); // New method for viewing all bills
                    break;
                case 3:
                    System.out.println("Going back...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (option != 3);
    }

    // Existing method for specific customer
    private void viewBillsForCustomer(Scanner scan) {
        cs.viewCustomers("SELECT * FROM tbl_customers");    
        int c_id;
        do {
            System.out.print("Enter Customer ID: ");
            c_id = scan.nextInt();
            if (!conf.doesIDExist("tbl_customers", c_id)) {
                System.out.println("Customer ID doesn't exist. Please try again.");
            }
        } while (!conf.doesIDExist("tbl_customers", c_id));

        // Fetch customer details
        String customerQuery = "SELECT c_fname, c_lname, c_address, meter_number FROM tbl_customers WHERE c_id = ?";
        String[] customerDetails = conf.getCustomerDetails(customerQuery, c_id);

        if (customerDetails != null) {
            System.out.println("\nCustomer Details:");
            System.out.println("Name: " + customerDetails[0] + " " + customerDetails[1]);
            System.out.println("Address: " + customerDetails[2]);
            System.out.println("Meter Number: " + customerDetails[3]);
        } else {
            System.out.println("Unable to fetch customer details.");
            return;
        }

        // Display bill information
        String[] headers = {"Bill ID", "Amount Due", "Due Date", "Units Consumed", "Bill Date", "Status"};
        String[] columns = {"b_id", "amount_due", "due_date", "units_consumed", "bill_date", "status"};
        String query = "SELECT b_id, amount_due, due_date, units_consumed, bill_date, status FROM tbl_bill WHERE c_id = ?";

        conf.viewRecords(query, headers, columns, c_id);
    }

    // New method for viewing all bills
    public void viewAllBills() {
        System.out.println("\n\t\t\t     === ALL BILLS ===\n");

        String[] headers = {"Bill ID", "Customer ID", "Amount Due", "Due Date", "Units Consumed", "Bill Date", "Status"};
        String[] columns = {"b_id", "c_id", "amount_due", "due_date", "units_consumed", "bill_date", "status"};
        String query = "SELECT b_id, c_id, amount_due, due_date, units_consumed, bill_date, status FROM tbl_bill";

        conf.viewRecords(query, headers, columns);
    }

    public void addBill(Scanner scan) {
        System.out.println("\n\t\t=== ADD A NEW BILL ===\n");
        cs.viewCustomers("SELECT * FROM tbl_customers");
        System.out.print("Enter Customer ID: ");
        int c_id = scan.nextInt();
        scan.nextLine(); // Consume newline

        if (!conf.doesIDExist("tbl_customers", c_id)) {
            System.out.println("Customer ID doesn't exist. Please enter a valid ID.");
            return;
        }

        System.out.print("Amount Due: ");
        double amountDue = scan.nextDouble();

        System.out.print("Units Consumed: ");
        int unitsConsumed = scan.nextInt();
        scan.nextLine(); // Consume newline

        System.out.print("Bill Date (YYYY-MM-DD): ");
        String billDate = scan.nextLine();

        System.out.print("Due Date (YYYY-MM-DD): ");
        String dueDate = scan.nextLine();

        // SQL query excluding the status field
        String sql = "INSERT INTO tbl_bill (c_id, amount_due, units_consumed, bill_date, due_date) VALUES (?, ?, ?, ?, ?)";

        // Insert the bill into the database
        conf.addRecord(sql, c_id, amountDue, unitsConsumed, billDate, dueDate);

        System.out.println("Bill added successfully with status 'Pending'.");
    }


    // Method to delete a bill
    public void deleteBill(Scanner scan) {
        System.out.println("\n\t\t=== REMOVE A BILL ===\n");
         viewAllBills();

        System.out.print("Enter Bill ID to delete: ");
        int b_id = scan.nextInt();

        String sql = "DELETE FROM tbl_bill WHERE b_id = ?";
        conf.deleteRecord(sql, b_id);
    }

    // Method to edit a bill
    public void editBill(Scanner scan) {
        System.out.println("\n\t\t=== EDIT A BILL ===\n");
        viewAllBills();
        int b_id;
        do {
            System.out.print("\nEnter Bill ID: ");
            b_id = scan.nextInt();
            if (!conf.doesIDExist("tbl_bill", b_id)) {
                System.out.println("Bill ID doesn't exist.");
            }
        } while (!conf.doesIDExist("tbl_bill", b_id));
        scan.nextLine();  // Consume newline

        // Gather updated bill details
        System.out.print("New Amount Due: ");
        double amount_due = scan.nextDouble();

        System.out.print("New Units Consumed: ");
        double units_consumed = scan.nextDouble();
        scan.nextLine();  // Consume newline

        System.out.print("New Due Date (YYYY-MM-DD): ");
        String due_date = scan.nextLine();

        System.out.print("New Bill Date (YYYY-MM-DD): ");
        String bill_date = scan.nextLine();

        String sql = "UPDATE tbl_bill SET amount_due = ?, due_date = ?, units_consumed = ?, bill_date = ? WHERE b_id = ?";
        conf.updateRecord(sql, amount_due, due_date, units_consumed, bill_date, b_id);
    }
    
    
}
