package RMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class rms {
    private static final String url = "jdbc:mysql://localhost:3306/RMS";
    private static final String username = "root";
    private static final String password = "shagor";
    public static void main(String args[]){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Owners owner = new Owners(connection,scanner);
            owner.addOwner();
            owner.showOwner();


        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
