package user;

public abstract class User {

    // ===== FIELDS (ENCAPSULATION) =====
    protected int userId;
    protected String name;
    protected String email;
    protected String password;
    protected String department;

    // ===== CONSTRUCTOR =====
    public User(int userId, String name, String email, String password, String department) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
    }

    // ===== ABSTRACT METHOD (POLYMORPHISM) =====
    // Each subclass (Student / Faculty / Admin) will implement this
    public abstract boolean login(String email, String password);

    // ===== COMMON METHOD =====
    public void logout() {
        System.out.println("You have been logged out successfully!");
    }

    // ===== GETTERS =====
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }
}
