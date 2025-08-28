package RMS;


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
}
