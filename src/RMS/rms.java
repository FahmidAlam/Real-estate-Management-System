package RMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class rms {
    private static final String url = "jdbc:mysql://localhost:3306/RMS";
    private static final String username = "root";
    private static final String password = "Thor_Loki";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            Owners owner = new Owners(connection);
            Properties properties = new Properties(connection);
            Units units = new Units(connection);
            Tenants tenants = new Tenants(connection, scanner);
            Payments payments = new Payments(connection, scanner);
            Leases leases = new Leases(connection, scanner);
            Repairs repairs = new Repairs(connection, scanner);
            Costs costs = new Costs(connection, scanner);

            while (true) {
                printMenu();
                int choice = getUserChoice(scanner);

                switch (choice) {
                    // Owners & Properties
                    case 1 -> owner.showOwner();
                    case 2 -> properties.showProperties();
                    case 3 -> units.showUnits();

                    // Tenants & Leases
                    case 4 -> tenants.showtenants();
                    case 5 -> tenants.addTenant();
                    case 6 -> leases.showLeases();
                    case 7 -> leases.addLease();

                    // Payments
                    case 8 -> payments.showPayments();
                    case 9 -> payments.addPayment();
                    case 10 -> payments.calculateDuePayments();

                    // Repairs
                    case 11 -> repairs.showRepairs();
                    case 12 -> repairs.addRepair();
                    case 13 -> repairs.updateRepairStatus();

                    // Costs
                    case 14 -> costs.showCosts();
                    case 15 -> costs.addCost();

                    // Exit
                    case 0 -> {
                        System.out.println("Exiting... Goodbye!");
                        System.exit(0);
                    }

                    default -> System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("\n===== Real Estate Management System =====");
        System.out.println(" Owners & Properties");
        System.out.println("  1. Show Owners");
        System.out.println("  2. Show Properties");
        System.out.println("  3. Show Units");
        System.out.println();
        System.out.println(" Tenants & Leases");
        System.out.println("  4. Show Tenants");
        System.out.println("  5. Add Tenant");
        System.out.println("  6. Show Leases");
        System.out.println("  7. Add Lease");
        System.out.println();
        System.out.println(" Payments");
        System.out.println("  8. Show Payments");
        System.out.println("  9. Add Payment");
        System.out.println(" 10. View Due Payments");
        System.out.println();
        System.out.println(" Repairs");
        System.out.println(" 11. Show Repairs");
        System.out.println(" 12. Add Repair");
        System.out.println(" 13. Update Repair Status");
        System.out.println();
        System.out.println(" Costs");
        System.out.println(" 14. Show Costs");
        System.out.println(" 15. Add Cost");
        System.out.println();
        System.out.println("  0. Exit");
        System.out.println("=========================================");
        System.out.print("Enter choice: ");
    }

    private static int getUserChoice(Scanner scanner) {
        String input = scanner.nextLine();
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1; // invalid input
        }
    }
}
