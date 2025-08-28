package RMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Properties {
    private Connection connection;

    public Properties(Connection connection) {
        this.connection=connection;

    }
    public void showProperties(){
        String query="select * from properties";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery(query);
            System.out.println("Properties: ");
            System.out.println("+----+-------------------------------------+----------------------------+-----------+----------------------------+---------+");
            System.out.println("|  Id| address                             | type                       | units     | value                      | owner   |");
            System.out.println("+----+-------------------------------------+----------------------------+-----------+----------------------------+---------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String address = resultSet.getString("address");
                String type = resultSet.getString("type");
                String units = resultSet.getString("units");
                String value = resultSet.getString("value");
                String owner = resultSet.getString("owner");
                System.out.printf("|%-4s|%-37s|%-28s|%-11s|%-28s|%-9s|",id,address,type,units,value,owner);
                System.out.println();
            }
            System.out.println("+----+-------------------------------------+----------------------------+-----------+----------------------------+---------+");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}