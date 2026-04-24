package user;

public class Admin extends User {

    public Admin(String userId, String name, String email, String password) {
        super(userId, name, email, password, "ADMIN");
    }

    @Override
    public int getMaxBookings() {
        return 0;
    }
}
