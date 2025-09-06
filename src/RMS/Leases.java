package RMS;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

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

    public void addLease() {
        System.out.print("Enter Tenant ID: ");
        int tenantId;
        try {
            tenantId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Tenant ID.");
            return;
        }

        System.out.print("Enter Unit ID: ");
        int unitId;
        try {
            unitId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Unit ID.");
            return;
        }

        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startStr = scanner.nextLine().trim();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endStr = scanner.nextLine().trim();

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startStr);
            endDate = LocalDate.parse(endStr);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        if (!endDate.isAfter(startDate)) {
            System.out.println("End date must be after start date.");
            return;
        }

        // compute months between, counting any leftover days as an extra month
        long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate);
        LocalDate plusMonths = startDate.plusMonths(monthsBetween);
        if (plusMonths.isBefore(endDate)) monthsBetween++;
        int months = (int) Math.max(1, monthsBetween);

        try {
            // 1) Check unit
            String checkUnit = "SELECT rent, status FROM Units WHERE id = ?";
            try (PreparedStatement psUnit = connection.prepareStatement(checkUnit)) {
                psUnit.setInt(1, unitId);
                try (ResultSet rsUnit = psUnit.executeQuery()) {
                    if (!rsUnit.next()) {
                        System.out.println("Unit not found.");
                        return;
                    }
                    double monthlyRent = rsUnit.getDouble("rent");
                    String unitStatus = rsUnit.getString("status");
                    if (!"empty".equalsIgnoreCase(unitStatus)) {
                        System.out.println("Unit not available (status: " + unitStatus + ").");
                        return;
                    }

                    // 2) Check tenant
                    String checkTenant = "SELECT income, status FROM Tenants WHERE id = ?";
                    try (PreparedStatement psTenant = connection.prepareStatement(checkTenant)) {
                        psTenant.setInt(1, tenantId);
                        try (ResultSet rsTenant = psTenant.executeQuery()) {
                            if (!rsTenant.next()) {
                                System.out.println("Tenant not found.");
                                return;
                            }
                            double income = rsTenant.getDouble("income");
                            String tenantStatus = rsTenant.getString("status");
                            if (!"active".equalsIgnoreCase(tenantStatus)) {
                                System.out.println("Tenant is not active.");
                                return;
                            }
                            if (income < monthlyRent) {
                                System.out.println("Tenant's income is insufficient for monthly rent.");
                                return;
                            }

                            // 3) compute total rent (monthly * months)
                            double totalRent = monthlyRent * months;

                            // 4) insert lease (storing total rent in Leases.rent per your schema)
                            String insertLease = "INSERT INTO Leases (unit, tenant, start_date, end_date, rent) VALUES (?, ?, ?, ?, ?)";
                            try (PreparedStatement psLease = connection.prepareStatement(insertLease)) {
                                psLease.setInt(1, unitId);
                                psLease.setInt(2, tenantId);
                                psLease.setDate(3, java.sql.Date.valueOf(startDate)); // convert LocalDate
                                psLease.setDate(4, java.sql.Date.valueOf(endDate));
                                psLease.setDouble(5, totalRent);

                                int rows = psLease.executeUpdate();
                                if (rows > 0) {
                                    // 5) update unit status
                                    String updateUnit = "UPDATE Units SET status = 'rented' WHERE id = ?";
                                    try (PreparedStatement psUpdate = connection.prepareStatement(updateUnit)) {
                                        psUpdate.setInt(1, unitId);
                                        psUpdate.executeUpdate();
                                    }
                                    System.out.printf("✅ Lease created: %d month(s) | monthly=%.2f | total=%.2f%n",
                                            months, monthlyRent, totalRent);
                                } else {
                                    System.out.println("Failed to create lease.");
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}