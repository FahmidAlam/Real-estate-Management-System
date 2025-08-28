package RMS;

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
}