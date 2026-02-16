package facility;

public class Facility {

    // ===== FIELDS (ENCAPSULATION) =====
    private int facilityId;
    private String facilityName;
    private int capacity;
    private String location;
    private String description;

    // ===== CONSTRUCTOR =====
    public Facility(int facilityId, String facilityName,
            int capacity, String location, String description) {
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.capacity = capacity;
        this.location = location;
        this.description = description;
    }

    // ===== GETTERS =====
    public int getFacilityId() {
        return facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    // ===== SETTERS (USED FOR UPDATE FACILITY) =====
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // ===== FULL UPDATE =====
    public void updateAll(String name, int capacity,
            String location, String description) {
        this.facilityName = name;
        this.capacity = capacity;
        this.location = location;
        this.description = description;
    }

    // ===== DISPLAY DETAILS =====
    public void displayDetails() {
        System.out.println("Facility ID   : " + facilityId);
        System.out.println("Facility Name : " + facilityName);
        System.out.println("Capacity      : " + capacity);
        System.out.println("Location      : " + location);
        System.out.println("Description   : " + description);
    }
}
