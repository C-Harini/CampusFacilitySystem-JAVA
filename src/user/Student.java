package user;

public class Student extends User {

    public Student(String userId, String name, String email, String password) {
        super(userId, name, email, password, "STUDENT");
    }

    @Override
    public int getMaxBookings() {
        return 2;
    }
}
