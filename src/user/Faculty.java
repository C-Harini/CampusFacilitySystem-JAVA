package user;

public class Faculty extends User {

    // ===== FACULTY-SPECIFIC FIELD =====
    private String employeeId;

    // ===== CONSTRUCTOR =====
    public Faculty(int userId, String name, String email, String password,
            String department, String employeeId) {

        // call parent constructor
        super(userId, name, email, password, department);
        this.employeeId = employeeId;
    }

    // ===== LOGIN IMPLEMENTATION (POLYMORPHISM) =====
    @Override
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // ===== GETTER =====
    public String getEmployeeId() {
        return employeeId;
    }
}
