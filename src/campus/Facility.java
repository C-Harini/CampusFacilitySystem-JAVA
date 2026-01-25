package src.campus;

public class Facility {
    private int facilityId;
    private String facilityName;
    private String location;
    private String description;
    private int capacity;

    public Facility(int facilityId, String facilityName, String location, String description, int capacity) {
         this.facilityId = facilityId;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.location = location;
        this.description = description;
        this.capacity = capacity;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void displayDetails() {
        System.out.println("Facility Details:");
        System.out.println("Facility ID: " + facilityId);
        System.out.println("Facility Name: " + facilityName);
        System.out.println("Location: " + location);
        System.out.println("Description: " + description);
    }
}