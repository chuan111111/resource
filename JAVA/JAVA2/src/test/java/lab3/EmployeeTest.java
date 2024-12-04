package lab3;

public class EmployeeTest {
    public static void main(String[] args) {
        EmployeeMangement manager = new EmployeeMangement();

        // Step 1: Add 3 employees A, B, and C.
        manager.addEmployee("A", 30);
        manager.addEmployee("B", 25);
        manager.addEmployee("C", 35);

        // Step 2: Delete employee B.
        manager.deleteEmployee("B");

        // Step 3: Add 2 employees A, D.
        manager.addEmployee("A", 40);
        manager.addEmployee("D", 28);

        // Step 4: Print all employees, should be A, C, D.
        System.out.println("All employees after all operations:");
        manager.printAllEmployees();
    }
}
