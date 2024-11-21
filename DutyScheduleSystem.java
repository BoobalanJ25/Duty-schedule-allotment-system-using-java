import java.sql.*;
import java.util.Scanner;

// Employee Class
class Employee {
    private String name;
    private int id;

    // Constructor
    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name;
    }
}

// Main System Class
public class DutyScheduleSystem {
    private Connection conn;
    private Scanner scanner = new Scanner(System.in);

    // Constructor to establish database connection
    public DutyScheduleSystem() {
        try {
            // Replace with your MySQL credentials
            String url = "jdbc:mysql://localhost:3306/duty_schedule";
            String username = "23ALR010";  // Your MySQL username
            String password = "23ALR010";      // Your MySQL password

            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    // Add Employee to the database
    public void addEmployee() {
        try {
            System.out.print("Enter Employee ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Employee Name: ");
            String name = scanner.nextLine();

            String query = "INSERT INTO employees (id,name) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.executeUpdate();

            System.out.println("Employee " + name + " added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
        }
    }

    // Assign duty to an employee
    public void assignDuty() {
        try {
            System.out.print("Enter Employee ID to assign duty: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Duty Day (e.g., Monday, Tuesday): ");
            String day = scanner.nextLine();

            String query = "INSERT INTO duties (employee_id, duty_day) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setString(2, day);
            pstmt.executeUpdate();

            System.out.println("Duty assigned successfully.");
        } catch (SQLException e) {
            System.err.println("Error assigning duty: " + e.getMessage());
        }
    }

    // View current duty schedule
    public void viewDutySchedule() {
        try {
            String query = "SELECT e.id, e.name, d.duty_day FROM employees e LEFT JOIN duties d ON e.id = d.employee_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("Current Duty Schedule:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String day = rs.getString("duty_day");
                System.out.println("Employee: " + name + " (ID: " + id + ") - Duty Day: " + (day == null ? "Not Assigned" : day));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving duty schedule: " + e.getMessage());
        }
    }

    // Remove a duty assignment
    public void removeDuty() {
        try {
            System.out.print("Enter Employee ID to remove duty: ");
            int id = Integer.parseInt(scanner.nextLine());

            String query = "DELETE FROM duties WHERE employee_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Duty assignment removed successfully.");
            } else {
                System.out.println("No duty found for Employee ID " + id + ".");
            }
        } catch (SQLException e) {
            System.err.println("Error removing duty: " + e.getMessage());
        }
    }

    // Main Method
    public static void main(String[] args) {
        DutyScheduleSystem system = new DutyScheduleSystem();

        while (true) {
            System.out.println("\nDuty Schedule Allotment System");
            System.out.println("1. Add Employee");
            System.out.println("2. Assign Duty");
            System.out.println("3. View Duty Schedule");
            System.out.println("4. Remove Duty");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(system.scanner.nextLine());

            switch (choice) {
                case 1:
                    system.addEmployee();
                    break;
                case 2:
                    system.assignDuty();
                    break;
                case 3:
                    system.viewDutySchedule();
                    break;

                case 4:
                    system.removeDuty();
                    break;
                case 5:
                    System.out.println("Thank you for using the Duty Schedule Allotment System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
