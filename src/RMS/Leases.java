package RMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Leases {
    private Scanner scanner ;
    private Connection connection;
    public Leases(Connection connection,Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }
    public void showLeases(){
        String query="select * from leases";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery(query);
            System.out.println("Leases: ");
            System.out.println("+----+------+--------+------------+------------+------------+");
            System.out.println("| id | unit | tenant | start_date | end_date   | rent       |");
            System.out.println("+----+------+--------+------------+------------+------------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int unit = resultSet.getInt("unit");
                int tenant = resultSet.getInt("tenant");
                String start_date = resultSet.getString("start_date");
                String end_date  = resultSet.getString("end_date");
                int rent = resultSet.getInt("rent");
                System.out.printf("|%-4s|%-6s|%-8s|%-12s|%-12s|%-12s|",id,unit,tenant,start_date,end_date,rent);
                System.out.println();
            }
            System.out.println("+----+------+--------+------------+------------+------------+");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}