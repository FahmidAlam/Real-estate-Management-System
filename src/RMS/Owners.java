package RMS;

import java.sql.*;
import java.util.Scanner;

public class Owners {

    private Connection connection;
    private Scanner scanner;

    public Owners(Connection connection){
        this.connection = connection;
        this.scanner = scanner;

    }
    public void addOwner(){
        System.out.print("Owner name: ");

        String name = scanner.nextLine();
        System.out.print("Owner email: ");
        String  email= scanner.nextLine();
        System.out.print("Owner phone: ");
        String phone = scanner.nextLine();
        System.out.print("Owner address: ");
        String address = scanner.nextLine();
        System.out.print("Owner type: ");
        String type = scanner.nextLine();
        try{
            String query = "Insert Into Owners (name,email,phone,address,type) Values(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, type);
            int affectedrows = preparedStatement.executeUpdate();
            if(affectedrows > 0){
                System.out.println("Successful");
            }
            else System.out.println("someting is wrong");
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public void showOwner(){
        String query = "Select *from Owners";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery(query);
            System.out.println("Owners: ");
            System.out.println("+-----+--------------------------+----------------------------+------------+------------------------------+---------+");
            System.out.println("| Id  | Name                     | Email                      | Phone      | Address                      | Type    |");
            System.out.println("+-----+--------------------------+----------------------------+------------+------------------------------+---------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String type = resultSet.getString("type");
                System.out.printf("|%-5s|%-26s|%-28s|%-12s|%-30s|%-9s|",id,name,email,phone,address,type);
                System.out.println();

            }
            System.out.println("+-----+--------------------------+----------------------------+------------+------------------------------+---------+");



        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
