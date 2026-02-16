package user;

public class Student extends User {

    // ===== STUDENT-SPECIFIC FIELD =====
    private String rollNumber;

    // ===== CONSTRUCTOR =====
    public Student(int userId, String name, String email, String password,
            String department, String rollNumber) {

        // call parent (User) constructor
        super(userId, name, email, password, department);
        this.rollNumber = rollNumber;
    }

    // ===== LOGIN IMPLEMENTATION (POLYMORPHISM) =====
    @Override
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // ===== GETTER =====
    public String getRollNumber() {
        return rollNumber;
    }
}
