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
}