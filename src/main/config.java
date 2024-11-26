package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class config {
    
    public static Connection connectDB() {
        Connection con = null;
        try {
            
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver          
            con = DriverManager.getConnection("jdbc:sqlite:bill.db"); // Establish connection
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed: " + e);
        }
        
        return con;
    }
    
    // Dynamic view method to display records from any table
    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames, Object... params) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            // Only bind parameters if any are provided
            if (params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                printResultSet(rs, columnHeaders, columnNames);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    // Helper method to print ResultSet
    private void printResultSet(ResultSet rs, String[] columnHeaders, String[] columnNames) throws SQLException {
        StringBuilder headerLine = new StringBuilder();
        int spacing = 25;
        int lineLength = columnHeaders.length * (spacing + 3) + 1;

        // Print header
        for (int i = 0; i < lineLength; i++) {
            headerLine.append("-");
        }
        headerLine.append("\n| ");
        for (String header : columnHeaders) {
            headerLine.append(String.format("%-" + spacing + "s | ", header));
        }
        headerLine.append("\n");
        for (int i = 0; i < lineLength; i++) {
            headerLine.append("-");
        }
        System.out.println(headerLine.toString());

        // Print rows
        while (rs.next()) {
            StringBuilder row = new StringBuilder("| ");
            for (String colName : columnNames) {
                String value = rs.getString(colName);
                row.append(String.format("%-" + spacing + "s | ", value != null ? value : ""));
            }
            System.out.println(row.toString());
        }

        // Print footer line
        for (int i = 0; i < lineLength; i++) {
            System.out.print("-");
        }
        System.out.println();
    }


    
    public void addRecord(String sql, Object... values) {
    try (Connection conn = config.connectDB(); // Use the connectDB method
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Loop through the values and set them in the prepared statement dynamically
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
            } else {
                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
            }
        }

        pstmt.executeUpdate();
        System.out.println("Record added successfully!");
    } catch (SQLException e) {
        System.out.println("Error adding record: " + e.getMessage());
    }
}
    private void setPreparedStatementValues(PreparedStatement pstmt, Object... values) throws SQLException {
        // Loop through the values and set them dynamically in the PreparedStatement
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
            } else {
                pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
            }
        }
    }

    public void updateRecord(String sql, Object... values) {
        try (Connection conn = config.connectDB(); // Use the connectDB method
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Loop through the values and set them in the prepared statement dynamically
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]); // If the value is Integer
                } else if (values[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) values[i]); // If the value is Double
                } else if (values[i] instanceof Float) {
                    pstmt.setFloat(i + 1, (Float) values[i]); // If the value is Float
                } else if (values[i] instanceof Long) {
                    pstmt.setLong(i + 1, (Long) values[i]); // If the value is Long
                } else if (values[i] instanceof Boolean) {
                    pstmt.setBoolean(i + 1, (Boolean) values[i]); // If the value is Boolean
                } else if (values[i] instanceof java.util.Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime())); // If the value is Date
                } else if (values[i] instanceof java.sql.Date) {
                    pstmt.setDate(i + 1, (java.sql.Date) values[i]); // If it's already a SQL Date
                } else if (values[i] instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]); // If the value is Timestamp
                } else {
                    pstmt.setString(i + 1, values[i].toString()); // Default to String for other types
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }
        
    public void deleteRecord(String sql, int id){       
        try (Connection con = config.connectDB();
            PreparedStatement pst = con.prepareStatement(sql);){
            
            pst.setInt(1, id);
            int success = pst.executeUpdate();

            if(success > 0){
                System.out.println("\nRecord Successfully Deleted.");
            }else{
                System.out.println("\nNo Record Found with ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public boolean doesIDExist(String table, int id){
        
        String findID = "SELECT * FROM " + table + " WHERE c_id = ?";
        
        try (Connection con = connectDB();
            PreparedStatement pst = con.prepareStatement(findID);){
            
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()){
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }
    
    public String getDataFromID(String table, int id, String column){
        String findID = "SELECT " + column + " FROM " + table + " WHERE c_id = ?";
        String data = "";
        
        try (Connection con = connectDB();      
            PreparedStatement pst = con.prepareStatement(findID)){
            
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                data = rs.getString(column);
            }
                               
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return data;
    }
    
        // Check if a field value already exists in the table
    public boolean recordExists(String table, String column, String value) {
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + column + " = ?";
        try (Connection con = connectDB();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, value);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;  // Record found
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;  // No record found
    }
        // Check if a field value exists in the table, excluding a specific ID
    public boolean doesFieldExistExcludeId(String table, String column, String value, int id) {
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + column + " = ? AND c_id != ?";

        try (Connection con = connectDB();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, value);
            pst.setInt(2, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;  // Value exists, excluding this ID
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;  // Value doesn't exist or error occurred
    }
    public String[] getCustomerDetails(String query, int c_id) {
        try (Connection con = config.connectDB(); // Establish connection
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, c_id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new String[]{rs.getString("c_fname"), rs.getString("c_lname"), rs.getString("c_address"), rs.getString("meter_number")};
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null; // Return null if no customer is found or an error occurs
    }
    
    public String[] getBillDetails(int billId) {
        String query = "SELECT b_id, amount_due, due_date, units_consumed, bill_date, status FROM tbl_bill WHERE b_id = ?";
        try (Connection con = config.connectDB(); // Establish connection
             PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, billId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                        rs.getString("b_id"), 
                        rs.getString("amount_due"), 
                        rs.getString("due_date"),
                        rs.getString("units_consumed"),
                        rs.getString("bill_date"),
                        rs.getString("status")
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null; // Return null if no bill is found or an error occurs
    }
    public Object getSingleValue(String sql, Object... params) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Set parameters dynamically
        setPreparedStatementValues(pstmt, params);

        try (ResultSet rs = pstmt.executeQuery()) {
            // If there's a result, return the first column of the first row
            if (rs.next()) {
                return rs.getObject(1); // Returns the first column as an Object (can be any type)
            }
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving single value: " + e.getMessage());
    }
    return null; // Return null if no result is found or an error occurs
}




}

