package user;

public class Faculty extends User {

    public Faculty(String userId, String name, String email, String password) {
        super(userId, name, email, password, "FACULTY");
    }

    @Override
    public int getMaxBookings() {
        return 5;
    }
}
