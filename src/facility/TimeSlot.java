package facility;

public class TimeSlot {

    // ===== FIELDS =====
    private String slot;
    private boolean available;

    // ===== CONSTRUCTOR =====
    public TimeSlot(String slot) {
        this.slot = slot;
        this.available = true; // default available
    }

    // ===== GETTERS =====
    public String getSlot() {
        return slot;
    }

    public boolean isAvailable() {
        return available;
    }

    // ===== SETTERS =====
    public void setAvailable(boolean available) {
        this.available = available;
    }

    // ===== DISPLAY =====
    public void display() {
        System.out.println(slot + " - " + (available ? "AVAILABLE" : "BOOKED"));
    }
}
