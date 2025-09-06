package RMS;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Costs {
    private Scanner scanner ;
    private Connection connection;
    public Costs(Connection connection, Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }
    public void showCosts(){
        String query="select * from costs";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery(query);
            System.out.println("costs: ");
            System.out.println("+----+----------+------------+------------+------------+----------------------------------------+");
            System.out.println("| id | property | type       | amount     | date       | notes                                  |");
            System.out.println("+----+----------+------------+------------+------------+----------------------------------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int property = resultSet.getInt("property");
                String type = resultSet.getString("type");
                int amount = resultSet.getInt("amount");
                String date  = resultSet.getString("date");
                String notes = resultSet.getString("notes");
                System.out.printf("|%-4s|%-10s|%-12s|%-12s|%-12s|%-40s|",id,property,type,amount,date,notes);
                System.out.println();
            }
            System.out.println("+----+----------+------------+------------+------------+----------------------------------------+");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addCost() {
        try {
            scanner.nextLine(); // clear buffer

            System.out.print("Enter Property ID: ");
            int propertyId = Integer.parseInt(scanner.nextLine().trim());

            // 1) Check if property exists
            String checkQuery = "SELECT id FROM Properties WHERE id = ?";
            PreparedStatement psCheck = connection.prepareStatement(checkQuery);
            psCheck.setInt(1, propertyId);
            ResultSet rs = psCheck.executeQuery();

            if (!rs.next()) {
                System.out.println("Property ID not found.");
                return;
            }

            System.out.print("Enter Cost Type (repair/utility/tax/insurance): ");
            String type = scanner.nextLine().trim().toLowerCase();

            System.out.print("Enter Amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();

            System.out.print("Enter Notes (optional): ");
            String notes = scanner.nextLine().trim();

            // 2) Insert cost record
            String insertQuery = "INSERT INTO Costs (property, type, amount, date, notes) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psInsert = connection.prepareStatement(insertQuery);
            psInsert.setInt(1, propertyId);
            psInsert.setString(2, type);
            psInsert.setDouble(3, amount);
            psInsert.setDate(4, Date.valueOf(date));
            psInsert.setString(5, notes);

            int rows = psInsert.executeUpdate();
            if (rows > 0) {
                System.out.println("Cost added successfully.");
            } else {
                System.out.println("Failed to add cost.");
            }

        } catch (Exception e) {
            System.out.println("Error adding cost: " + e.getMessage());
        }
    }

}