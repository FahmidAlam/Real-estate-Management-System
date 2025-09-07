package RMS;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Payments {
    private Scanner scanner;
    private Connection connection;
    public Payments( Connection connection, Scanner scanner ) {
        this.scanner = scanner;
        this.connection = connection;
    }
    public void showPayments(){
        String query="select * from payments";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery(query);
            System.out.println("Payments: ");
            System.out.println("+----+-------+------------+-------------+--------+--------+");
            System.out.println("| id | lease | date       | amount      | status | method |");
            System.out.println("+----+-------+------------+-------------+--------+--------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int lease = resultSet.getInt("lease");
                String date = resultSet.getString("date");
                int amount = resultSet.getInt("amount");
                String status  = resultSet.getString("status");
                String method = resultSet.getString("method");
                System.out.printf("|%-4s|%-7s|%-12s|%-13s|%-8s|%-8s|",id,lease ,date,amount,status,method);
                System.out.println();
            }
            System.out.println("+----+-------+------------+-------------+--------+--------+");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addPayment() {
        try {
            scanner.nextLine(); // clear buffer

            System.out.print("Enter Lease ID: ");
            int leaseId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Payment Date (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();

            System.out.print("Enter Payment Amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Payment Method (cash/check/bank/card): ");
            String method = scanner.nextLine().trim();

            // 1) Get total rent for the lease
            String leaseQuery = "SELECT rent FROM Leases WHERE id = ?";
            PreparedStatement psLease = connection.prepareStatement(leaseQuery);
            psLease.setInt(1, leaseId);
            ResultSet rsLease = psLease.executeQuery();

            if (!rsLease.next()) {
                System.out.println("Invalid Lease ID.");
                return;
            }

            double totalRent = rsLease.getDouble("rent");

            // 2) Get total paid so far for this lease
            String paidQuery = "SELECT COALESCE(SUM(amount),0) AS totalPaid FROM Payments WHERE lease = ?";
            PreparedStatement psPaid = connection.prepareStatement(paidQuery);
            psPaid.setInt(1, leaseId);
            ResultSet rsPaid = psPaid.executeQuery();
            rsPaid.next();
            double totalPaid = rsPaid.getDouble("totalPaid");

            // 3) Calculate remaining due
            double remainingDue = totalRent - totalPaid;

            if (remainingDue <= 0) {
                System.out.println("This lease is already fully paid. No further payments accepted.");
                return;
            }

            // 4) Check against remaining due
            if (amount > remainingDue) {
                System.out.printf("Payment exceeds remaining due (%.2f). Payment rejected.%n", remainingDue);
                return;
            }

            // 5) Decide payment status
            String status;
            if (amount < remainingDue) {
                status = "due";   // still has balance left
            } else {
                status = "paid";  // lease fully settled
            }

            // 6) Insert the payment
            String insertQuery = "INSERT INTO Payments (lease, date, amount, status, method) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psInsert = connection.prepareStatement(insertQuery);
            psInsert.setInt(1, leaseId);
            psInsert.setDate(2, Date.valueOf(date));
            psInsert.setDouble(3, amount);
            psInsert.setString(4, status);
            psInsert.setString(5, method);

            int rows = psInsert.executeUpdate();
            if (rows > 0) {
                System.out.printf("Payment of %.2f added successfully with status: %s%n", amount, status);

                // 7) If lease is now fully paid, adjust statuses
                if (status.equals("paid")) {
                    // Reset all payments for this lease to 'due'
                    String resetQuery = "UPDATE Payments SET status = 'due' WHERE lease = ?";
                    PreparedStatement psReset = connection.prepareStatement(resetQuery);
                    psReset.setInt(1, leaseId);
                    psReset.executeUpdate();

                    // Mark latest payment as 'paid'
                    String updateLatest = "UPDATE Payments SET status = 'paid' " +
                            "WHERE id = (SELECT MAX(id) FROM Payments WHERE lease = ?)";
                    PreparedStatement psLatest = connection.prepareStatement(updateLatest);
                    psLatest.setInt(1, leaseId);
                    psLatest.executeUpdate();

                    System.out.println("Lease fully settled. Latest payment marked as 'paid', previous ones set to 'due'.");
                }
            } else {
                System.out.println("Failed to add payment.");
            }

        } catch (Exception e) {
            System.out.println("Error adding payment: " + e.getMessage());
        }
    }

    public void calculateDuePayments() {
        try {
            //System.out.print("Enter Tenant ID: ");
            //int tenantId = scanner.nextInt();
            System.out.print("Enter Tenant ID: ");
            String tenantInput = scanner.nextLine();
            int tenantId;
            try {
                tenantId = Integer.parseInt(tenantInput.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid tenant ID.");
                return;
            }


            String query = "SELECT l.id, l.rent, IFNULL(SUM(p.amount), 0) as total_paid " +
                    "FROM Leases l " +
                    "LEFT JOIN Payments p ON l.id = p.lease " +
                    "WHERE l.tenant = ? " +
                    "GROUP BY l.id, l.rent";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, tenantId);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                int leaseId = rs.getInt("id");
                double totalRent = rs.getDouble("rent");
                double totalPaid = rs.getDouble("total_paid");
                double remainingDue = totalRent - totalPaid;

                System.out.println("Lease ID " + leaseId + ":");
                System.out.println("  Total Rent = " + totalRent);
                System.out.println("  Total Paid = " + totalPaid);

                if (remainingDue > 0) {
                    System.out.println("  Outstanding Due = " + remainingDue);
                } else if (remainingDue == 0) {
                    System.out.println("  ✅ Fully Paid");
                }
            }

            if (!found) {
                System.out.println("No leases found for this tenant.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
