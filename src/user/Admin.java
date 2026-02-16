package user;

public class Admin extends User {

    // ===== ADMIN-SPECIFIC FIELD =====
    private String adminCode;

    // ===== CONSTRUCTOR =====
    public Admin(int userId, String name, String email, String password,
            String adminCode) {

        // Admin does not belong to a department → pass null
        super(userId, name, email, password, null);
        this.adminCode = adminCode;
    }

    // ===== LOGIN IMPLEMENTATION (POLYMORPHISM) =====
    @Override
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // ===== GETTER =====
    public String getAdminCode() {
        return adminCode;
    }
}
