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

            Owners owner = new Owners(connection);
            Properties properties = new Properties(connection);
            Units units = new Units(connection);
            Tenants tenants = new Tenants(connection,scanner);
            Payments payments = new Payments(connection,scanner);
            Leases leases = new Leases(connection,scanner);
            Repairs repairs = new Repairs(connection,scanner);
            Costs costs = new Costs(connection,scanner);

            while(true){
                System.out.println("Realestate Management System");
                System.out.println("1. Show owners");
                System.out.println("2. Show properties");
                System.out.println("3. Show units");
                System.out.println("4. Show tenants");
                System.out.println("5. Show payments");
                System.out.println("6. Show leases");
                System.out.println("7. Show repairs");
                System.out.println("8. Show costs");
                System.out.println("9. Exit");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        owner.showOwner();
                        break;
                    case 2:
                        properties.showProperties();
                        break;
                    case 3:
                        units.showUnits();
                        break;
                    case 4:
                        tenants.showtenants();
                        break;
                    case 5:
                        payments.showPayments();
                        break;
                    case 6:
                        leases.showLeases();
                        break;
                    case 7:
                        repairs.showRepairs();
                        break;
                    case 8:
                        costs.showCosts();
                        break;
                    case 9:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
