package RMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class rms {
    private static final String url = "jdbc:mysql://localhost:3306/RMS";
    private static final String username = "root";
    private static final String password = "Thor_Loki";
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
                System.out.println("10 . add tenants");
                System.out.println("11 . add leases");
                System.out.println("12 . add payments");
                System.out.println("13 . view due payments");
                System.out.println("14 . add repairs");
                System.out.println("15 . update repairs");
                System.out.println("16 . add costs");
                //int choice = scanner.nextInt();
                String input = scanner.nextLine();
                int choice;
                try {
                    choice = Integer.parseInt(input.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice.");
                    continue;
                }

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
                    case 10:
                        tenants.addTenant();
                        break;
                    case 11:
                        leases.addLease();
                        break;
                    case 12:
                        payments.addPayment();
                        break;
                    case 13:
                        payments.calculateDuePayments();
                        break;
                    case 14:
                        repairs.addRepair();
                        break;
                    case 15:
                        repairs.updateRepairStatus();
                        break;
                    case 16:
                        costs.addCost();
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
