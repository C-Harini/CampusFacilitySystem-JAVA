package booking;

public class PermissionRequest {

    private int requestId;
    private int bookingId;
    private String reason;

    public PermissionRequest(int requestId, int bookingId, String reason) {
        this.requestId = requestId;
        this.bookingId = bookingId;
        this.reason = reason;
    }

    // ===== GETTERS (REQUIRED) =====
    public int getRequestId() {
        return requestId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getReason() {
        return reason;
    }
}
