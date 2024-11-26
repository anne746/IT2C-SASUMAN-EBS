package imp;

import main.config;
import java.util.Scanner;
import java.time.LocalDate;
import imp.Bills;

public class Payment {
    config conf = new config();
    Bills bl = new Bills();
    public void managePayments(Scanner scan) {
        int opt;
        do {
            System.out.println("\n\t=== Payment Management ===\n");
            System.out.println("1. Add Payment");
            System.out.println("2. View Payments");
            System.out.println("3. Go Back");

            System.out.print("\nEnter Option: ");
            opt = scan.nextInt();
            scan.nextLine(); // Consume newline

            switch (opt) {
                case 1:
                    addPayment(scan);
                    break;
                case 2:
                    viewPayments(scan);
                    break;
                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid Option. Please try again.");
            }
        } while (opt != 3);
    }

    public void addPayment(Scanner scan) {
        System.out.println("\n\t\t=== ADD PAYMENT ===\n");
        bl.viewAllBills(); // Display all bills to choose from

        // Validate Bill ID
        int billId;
        do {
            System.out.print("Enter Bill ID: ");
            billId = scan.nextInt();
            scan.nextLine(); // Consume newline

            // Check if the bill exists
            if (conf.doesIDExist("tbl_bill", billId)) {
                System.out.println("Invalid Bill ID. Please try again.");
            } else {
                // Check if the bill is already paid
                String statusQuery = "SELECT status FROM tbl_bill WHERE b_id = ?";
                String status = (String) conf.getSingleValue(statusQuery, billId);

                if ("Paid".equalsIgnoreCase(status)) {
                    System.out.println("This bill has already been paid. You cannot pay it again.");
                    return; // Exit the method to prevent double payment
                } else {
                    break; // Proceed if the bill is pending
                }
            }
        } while (true);

        // Retrieve the amount due from the bill
        String amountDueQuery = "SELECT amount_due FROM tbl_bill WHERE b_id = ?";
        Object result = conf.getSingleValue(amountDueQuery, billId);

        // Convert result to double (handles both Integer and Double)
        double amountDue = 0.0;
        if (result instanceof Integer) {
            amountDue = ((Integer) result).doubleValue();
        } else if (result instanceof Double) {
            amountDue = (Double) result;
        } else {
            
            return; // Exit if unexpected result type
        }

        // Input for Amount Paid and Payment Method
        System.out.print("Enter Amount Paid: ");
        double amountPaid = scan.nextDouble();
        scan.nextLine(); // Consume newline

        System.out.print("Enter Payment Method (Cash, Card, etc.): ");
        String paymentMethod = scan.nextLine();

        // Auto-generate current date
        String paymentDate = LocalDate.now().toString();

        // Check if the amount paid is enough
        if (amountPaid < amountDue) {
            // Update the amount due by subtracting the paid amount
            amountDue -= amountPaid;

            // Insert partial payment into tbl_payment
            String sql = "INSERT INTO tbl_payment (bill_id, amount_paid, payment_date, payment_method) VALUES (?, ?, ?, ?)";
            conf.addRecord(sql, billId, amountPaid, paymentDate, paymentMethod);

            // Update the remaining amount_due for the bill
            String updateAmountDueSQL = "UPDATE tbl_bill SET amount_due = ? WHERE b_id = ?";
            conf.updateRecord(updateAmountDueSQL, amountDue, billId);

            System.out.println("Partial payment added successfully. Remaining amount due: " + amountDue);
        } else {
            // Insert full payment into tbl_payment
            String sql = "INSERT INTO tbl_payment (bill_id, amount_paid, payment_date, payment_method) VALUES (?, ?, ?, ?)";
            conf.addRecord(sql, billId, amountPaid, paymentDate, paymentMethod);

            // Update the bill status to "Paid" if fully paid
            String updateBillStatusSQL = "UPDATE tbl_bill SET status = 'Paid', amount_due = 0 WHERE b_id = ?";
            conf.updateRecord(updateBillStatusSQL, billId);

            System.out.println("Payment added successfully, and bill status updated to 'Paid'!");
        }
    }



    public void viewPayments(Scanner scan) {
        int opt;
        do {
            System.out.println("\n\t\t=== PAYMENT LIST ===\n");
            System.out.println("1. View All Payments");
            System.out.println("2. View Payments for Specific Bill ID");
            System.out.println("3. Go Back");
            System.out.print("\nEnter Option: ");
            opt = scan.nextInt();
            scan.nextLine(); // Consume newline

            switch (opt) {
                case 1:
                    // View all payments
                    String queryAll = "SELECT payment_id, bill_id, amount_paid, payment_date, payment_method FROM tbl_payment";
                    String[] headersAll = {"Payment ID", "Bill ID", "Amount Paid", "Payment Date", "Payment Method"};
                    String[] columnsAll = {"payment_id", "bill_id", "amount_paid", "payment_date", "payment_method"};
                    conf.viewRecords(queryAll, headersAll, columnsAll);
                    break;
                case 2:
                    // View payments for a specific bill ID
                    bl.viewAllBills();
                    System.out.print("Enter Bill ID: ");
                    int billId = scan.nextInt();
                    scan.nextLine(); // Consume newline

                    // Validate Bill ID (check if it exists in the tbl_bill)
                    if (conf.doesIDExist("tbl_bill", billId)) {
                        System.out.println("Bill ID doesn't exist. Please try again.");
                        System.out.print("Enter Bill ID: ");
                         billId = scan.nextInt();
                        break;
                    }

                    // Fetch bill details for the given Bill ID
                    
                    String[] billDetails = conf.getBillDetails(billId); // Assuming a similar method for fetching bill details

                    if (billDetails != null) {
                        System.out.println("\n--------------------------------------");
                        System.out.println("        Bill Details:");
                        System.out.println("--------------------------------------");
                        System.out.println("Bill ID: " + billDetails[0]);
                        System.out.println("Amount Due: " + billDetails[1]);
                        System.out.println("Due Date: " + billDetails[2]);
                        System.out.println("Units Consumed: " + billDetails[3]);
                        System.out.println("Bill Date: " + billDetails[4]);
                        System.out.println("Status: " + billDetails[5]);
                    } else {
                        System.out.println("Unable to fetch bill details.");
                        return;
                    }

                    // Display payment information for this specific bill ID
                    String[] headersSpecific = {"Payment ID", "Amount Paid", "Payment Date", "Payment Method"};
                    String[] columnsSpecific = {"payment_id", "amount_paid", "payment_date", "payment_method"};
                    String querySpecific = "SELECT payment_id, amount_paid, payment_date, payment_method FROM tbl_payment WHERE bill_id = ?";
                    conf.viewRecords(querySpecific, headersSpecific, columnsSpecific, billId); // Pass billId for specific query
                    break;
                case 3:
                    System.out.println("Returning to Payment Management Menu...");
                    return; // Exit the method and go back to the previous menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (opt != 3);
    }

}
