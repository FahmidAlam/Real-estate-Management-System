package RMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Tenants {
    private Connection connection;
    private Scanner scanner;
    public Tenants(Connection connnection,Scanner scanner) {
        this.connection = connnection;
        this.scanner = scanner;
    }
    public void addTenants(){

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
}