package facility;

import java.util.ArrayList;

public class Facility {
    private String facilityId;
    private String name;
    private String location;
    private int capacity;
    private boolean active;
    private ArrayList<TimeSlot> timeSlots;

    public Facility(String facilityId, String name, String location, int capacity, boolean active) {
        this.facilityId = facilityId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.active = active;
        this.timeSlots = new ArrayList<>();
        initializeTimeSlots();
    }

    private void initializeTimeSlots() {
        timeSlots.add(new TimeSlot(1, "08:00", "09:00"));
        timeSlots.add(new TimeSlot(2, "09:00", "10:00"));
        timeSlots.add(new TimeSlot(3, "10:00", "11:00"));
        timeSlots.add(new TimeSlot(4, "11:00", "12:00"));
        timeSlots.add(new TimeSlot(5, "12:00", "13:00"));
        timeSlots.add(new TimeSlot(6, "13:00", "14:00"));
        timeSlots.add(new TimeSlot(7, "14:00", "15:00"));
        timeSlots.add(new TimeSlot(8, "15:00", "16:00"));
        timeSlots.add(new TimeSlot(9, "16:00", "17:00"));
        timeSlots.add(new TimeSlot(10, "17:00", "18:00"));
    }

    public String getFacilityId() { return facilityId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public boolean isActive() { return active; }
    public ArrayList<TimeSlot> getTimeSlots() { return timeSlots; }

    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return facilityId + "|" + name + "|" + location + "|" + capacity + "|" + active;
    }
}
