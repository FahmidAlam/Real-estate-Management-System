package RMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Tenants {
    private Connection connection;
    private Scanner scanner;
    public Tenants(Connection connection,Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    public void showtenants(){
        String query="select * from tenants";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery(query);
            System.out.println("Tenants: ");
            System.out.println("+----+-------------------------+----------------------------+----------------+---------------+------------+");
            System.out.println("|  Id| name                    | email                      | phone          | income        | status     |");
            System.out.println("+----+-------------------------+----------------------------+----------------+---------------+------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                int income = resultSet.getInt("income");
                String status = resultSet.getString("status");
                System.out.printf("|%-4s|%-25s|%-28s|%-16s|%-15s|%-12s|",id,name,email,phone,income,status);
                System.out.println();
            }
            System.out.println("+----+-------------------------+----------------------------+----------------+---------------+------------+");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Add Tenant
    public void addTenant() {
        //scanner.nextLine(); // clear buffer from previous nextInt()

        System.out.print("Tenant name: ");
        String name = scanner.nextLine();

        System.out.print("Tenant email: ");
        String email = scanner.nextLine();

        System.out.print("Tenant phone: ");
        String phone = scanner.nextLine();

        System.out.print("Tenant income: ");
        double income = scanner.nextDouble();
        scanner.nextLine(); // consume leftover newline

        System.out.print("Tenant status (active/inactive): ");
        String status = scanner.nextLine();

        try {
            String query = "INSERT INTO Tenants (name, email, phone, income, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setDouble(4, income);
            ps.setString(5, status);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Tenant added successfully.");
            } else {
                System.out.println("⚠️ Something went wrong.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}