package src.campus;

public class Booking {
    private int bookingId;
    private int userId;
    private int facilityId;
    private String bookingDate;
    private String status;

    public Booking(int bookingId, int userId, int facilityId, String bookingDate, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.facilityId = facilityId;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void displayDetails() {
        System.out.println("Booking Details:");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("User ID: " + userId);
        System.out.println("Facility ID: " + facilityId);
        System.out.println("Booking Date: " + bookingDate);
        System.out.println("Status: " + status);
    }
}