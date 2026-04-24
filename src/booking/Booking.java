package booking;

import java.util.ArrayList;

public class Booking {
    private String bookingId;
    private String userId;
    private String facilityId;
    private String facilityName;
    private String bookingDate;
    private ArrayList<String> timeSlots;
    private BookingStatus status;
    private String remarks;

    public Booking(String bookingId, String userId, String facilityId, String facilityName,
                   String bookingDate, ArrayList<String> timeSlots, BookingStatus status, String remarks) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.bookingDate = bookingDate;
        this.timeSlots = timeSlots;
        this.status = status;
        this.remarks = remarks;
    }

    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public String getFacilityId() { return facilityId; }
    public String getFacilityName() { return facilityName; }
    public String getBookingDate() { return bookingDate; }
    public ArrayList<String> getTimeSlots() { return timeSlots; }
    public BookingStatus getStatus() { return status; }
    public String getRemarks() { return remarks; }

    public void setStatus(BookingStatus status) { this.status = status; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String timeSlotsToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < timeSlots.size(); i++) {
            sb.append(timeSlots.get(i));
            if (i < timeSlots.size() - 1) sb.append(";");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return bookingId + "|" + userId + "|" + facilityId + "|" + facilityName + "|" +
               bookingDate + "|" + timeSlotsToString() + "|" + status.name() + "|" + remarks;
    }
}
