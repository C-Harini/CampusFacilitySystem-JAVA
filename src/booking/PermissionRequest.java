package booking;

public class PermissionRequest {
    private String requestId;
    private String bookingId;
    private String userId;
    private String facilityId;
    private String facilityName;
    private String bookingDate;
    private String timeSlots;
    private String reason;
    private BookingStatus status;

    public PermissionRequest(String requestId, String bookingId, String userId,
                              String facilityId, String facilityName, String bookingDate,
                              String timeSlots, String reason, BookingStatus status) {
        this.requestId = requestId;
        this.bookingId = bookingId;
        this.userId = userId;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.bookingDate = bookingDate;
        this.timeSlots = timeSlots;
        this.reason = reason;
        this.status = status;
    }

    public String getRequestId() { return requestId; }
    public String getBookingId() { return bookingId; }
    public String getUserId() { return userId; }
    public String getFacilityId() { return facilityId; }
    public String getFacilityName() { return facilityName; }
    public String getBookingDate() { return bookingDate; }
    public String getTimeSlots() { return timeSlots; }
    public String getReason() { return reason; }
    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) { this.status = status; }

    @Override
    public String toString() {
        return requestId + "|" + bookingId + "|" + userId + "|" + facilityId + "|" +
               facilityName + "|" + bookingDate + "|" + timeSlots + "|" + reason + "|" + status.name();
    }
}
