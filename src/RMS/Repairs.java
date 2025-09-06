package RMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Repairs {
    private Scanner scanner ;
    private Connection connection;
    public Repairs(Connection connection,Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }
    public void showRepairs(){
        String query="select * from repairs";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery(query);
            System.out.println("repairs: ");
            System.out.println("+----+------+------------+----------------------------------------+--------+------------+");
            System.out.println("| id | unit | type       | details                                | status | cost       |");
            System.out.println("+----+------+------------+----------------------------------------+--------+------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int unit = resultSet.getInt("unit");
                String type = resultSet.getString("type");
                String details = resultSet.getString("details");
                String status  = resultSet.getString("status");
                int cost = resultSet.getInt("cost");
                System.out.printf("|%-4s|%-6s|%-12s|%-40s|%-8s|%-12s|",id,unit,type,details,status,cost);
                System.out.println();
            }
            System.out.println("+----+------+------------+----------------------------------------+--------+------------+");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addRepair() {
        try {
            scanner.nextLine(); // clear buffer

            System.out.print("Enter Unit ID: ");
            int unitId = Integer.parseInt(scanner.nextLine().trim());

            // 1) Check if unit exists and get its status
            String unitQuery = "SELECT status FROM Units WHERE id = ?";
            PreparedStatement psUnit = connection.prepareStatement(unitQuery);
            psUnit.setInt(1, unitId);
            ResultSet rsUnit = psUnit.executeQuery();

            if (!rsUnit.next()) {
                System.out.println("Unit ID not found.");
                return;
            }

            String unitStatus = rsUnit.getString("status");
            if (unitStatus.equals("repair")) {
                System.out.println("Unit is already under repair.");
                return;
            }

            System.out.print("Enter Repair Type (plumbing/electric/other): ");
            String type = scanner.nextLine().trim();

            System.out.print("Enter Repair Details: ");
            String details = scanner.nextLine().trim();

            System.out.print("Enter Cost (optional, enter 0 if unknown): ");
            double cost = Double.parseDouble(scanner.nextLine().trim());

            // 2) Insert new repair
            String insertQuery = "INSERT INTO Repairs (unit, type, details, status, cost) VALUES (?, ?, ?, 'new', ?)";
            PreparedStatement psInsert = connection.prepareStatement(insertQuery);
            psInsert.setInt(1, unitId);
            psInsert.setString(2, type);
            psInsert.setString(3, details);
            psInsert.setDouble(4, cost);

            int rows = psInsert.executeUpdate();
            if (rows > 0) {
                System.out.println("Repair request added successfully!");

                // 3) Update unit status to 'repair'
                String updateUnit = "UPDATE Units SET status = 'repair' WHERE id = ?";
                PreparedStatement psUpdate = connection.prepareStatement(updateUnit);
                psUpdate.setInt(1, unitId);
                psUpdate.executeUpdate();

                System.out.println("Unit status updated to 'repair'.");
            } else {
                System.out.println("Failed to add repair.");
            }

        } catch (Exception e) {
            System.out.println("Error adding repair: " + e.getMessage());
        }
    }


    public void updateRepairStatus() {
        try {
            scanner.nextLine(); // clear buffer

            System.out.print("Enter Repair ID to update: ");
            int repairId = Integer.parseInt(scanner.nextLine().trim());

            // 1) Get current status and unit ID
            String query = "SELECT status, unit FROM Repairs WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, repairId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Repair ID not found.");
                return;
            }

            String currentStatus = rs.getString("status");
            int unitId = rs.getInt("unit");

            System.out.println("Current status: " + currentStatus);
            System.out.print("Enter new status (new/doing/done): ");
            String newStatus = scanner.nextLine().trim().toLowerCase();

            // 2) Validate status transition
            if (!(newStatus.equals("new") || newStatus.equals("doing") || newStatus.equals("done"))) {
                System.out.println("Invalid status entered.");
                return;
            }

            if (currentStatus.equals("done")) {
                System.out.println("Repair is already completed. Status cannot be changed.");
                return;
            }

            if (currentStatus.equals("new") && newStatus.equals("done")) {
                System.out.println("Cannot skip status. Change to 'doing' first.");
                return;
            }

            if (currentStatus.equals("doing") && newStatus.equals("new")) {
                System.out.println("Cannot revert to 'new' from 'doing'.");
                return;
            }

            // 3) Update repair status
            String updateQuery = "UPDATE Repairs SET status = ? WHERE id = ?";
            PreparedStatement psUpdate = connection.prepareStatement(updateQuery);
            psUpdate.setString(1, newStatus);
            psUpdate.setInt(2, repairId);
            psUpdate.executeUpdate();

            System.out.println("Repair status updated to '" + newStatus + "'.");

            // 4) If repair is done, update unit status to 'empty'
            if (newStatus.equals("done")) {
                String updateUnit = "UPDATE Units SET status = 'empty' WHERE id = ?";
                PreparedStatement psUnit = connection.prepareStatement(updateUnit);
                psUnit.setInt(1, unitId);
                psUnit.executeUpdate();
                System.out.println("Unit status updated to 'empty'.");
            }

        } catch (Exception e) {
            System.out.println("Error updating repair status: " + e.getMessage());
        }
    }

}