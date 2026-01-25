package src.campus;

public class Mainapp {

    public static void main(String[] args) {

        System.out.println("Campus Facility Booking System Started\n");

        // Create User object
        User user1 = new User(1, "joshna", "pass123", "Student");

        // Display user details
        user1.displayDetails();   // use displayDetails() if you rename it
        System.out.println();

        // Create Booking object
        Booking booking1 = new Booking(
                101,      // bookingId
                1,        // userId
                201,      // facilityId
                "2026-01-25",
                "CONFIRMED"
        );

        // Display booking details
        booking1.displayDetails();

        System.out.println("\nSystem executed successfully.");
    }
}
