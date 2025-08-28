package RMS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Units {
    private Connection connection;
    public Units(Connection connection) {
        this.connection = connection;
    }
    public void showUnits(){
        String query="select * from units";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery(query);
            System.out.println("Units: ");
            System.out.println("+-----+---------------------+-------------------+-----------------+---------------+---------+");
            System.out.println("|  Id | property ID         | number            | rent            | status        | rooms   |");
            System.out.println("+-----+---------------------+-------------------+-----------------+---------------+---------+");
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int property_id = resultSet.getInt("property");
                String number = resultSet.getString("number");
                int rent = resultSet.getInt("rent");
                String status  = resultSet.getString("status");
                String rooms = resultSet.getString("rooms");
                System.out.printf("|%-5s|%-21s|%-19s|%-17s|%-15s|%-9s|",id,property_id,number,rent,status,rooms);
                System.out.println();
            }
            System.out.println("+-----+---------------------+-------------------+-----------------+---------------+---------+");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}