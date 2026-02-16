package booking;

import user.User;
import facility.Facility;

public class Booking {

    private int bookingId;
    private User user;
    private Facility facility;
    private String date;
    private String timeSlot;
    private String status;

    public Booking(int bookingId, User user, Facility facility, String date, String timeSlot) {
        this.bookingId = bookingId;
        this.user = user;
        this.facility = facility;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = "PENDING";
    }

    // ===== GETTERS (REQUIRED) =====
    public int getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Facility getFacility() {
        return facility;
    }

    public String getDate() {
        return date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getStatus() {
        return status;
    }

    // ===== SETTERS =====
    public void setStatus(String status) {
        this.status = status;
    }

    // ===== OPERATIONS =====
    public void modify(String newDate, String newTimeSlot) {
        this.date = newDate;
        this.timeSlot = newTimeSlot;
        this.status = "PENDING";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    // ===== DISPLAY METHODS =====
    public void displayForUser() {
        System.out.println(
                bookingId + " | " +
                        facility.getFacilityName() + " | " +
                        date + " | " +
                        timeSlot + " | " +
                        status);
    }

    public void displayForAdmin() {
        System.out.println(
                bookingId + " | " +
                        user.getName() + " | " +
                        facility.getFacilityName() + " | " +
                        date + " | " +
                        timeSlot + " | " +
                        status);
    }

}
