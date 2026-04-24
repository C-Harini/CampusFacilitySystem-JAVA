package service;

import facility.Facility;
import user.User;

import java.util.ArrayList;

public class FacilityService {

    private ArrayList<Facility> facilities;
    private FileService fileService;
    private int idCounter;

    public FacilityService(FileService fileService) {
        this.fileService = fileService;
        this.facilities = fileService.loadFacilities();
        this.idCounter = facilities.size() + 1;
    }

    public ArrayList<Facility> getAllActiveFacilities() {
        ArrayList<Facility> active = new ArrayList<>();
        for (Facility f : facilities) {
            if (f.isActive()) active.add(f);
        }
        return active;
    }

    public ArrayList<Facility> getAllFacilities() {
        return facilities;
    }

    public Facility getFacilityById(String facilityId) {
        for (Facility f : facilities) {
            if (f.getFacilityId().equals(facilityId)) return f;
        }
        return null;
    }

    public void addFacility(String name, String location, int capacity) {
        String id = "FAC" + String.format("%03d", idCounter++);
        Facility f = new Facility(id, name, location, capacity, true);
        facilities.add(f);
        fileService.saveFacilities(facilities);
        fileService.appendAuditLog("Facility added – " + name);
    }

    public boolean softDeleteFacility(String facilityId) {
        for (Facility f : facilities) {
            if (f.getFacilityId().equals(facilityId) && f.isActive()) {
                f.setActive(false);
                fileService.saveFacilities(facilities);
                fileService.appendAuditLog("Facility soft-deleted – " + f.getName());
                return true;
            }
        }
        return false;
    }

    public boolean updateFacility(String facilityId, String name, String location, int capacity) {
        for (Facility f : facilities) {
            if (f.getFacilityId().equals(facilityId) && f.isActive()) {
                f.setName(name);
                f.setLocation(location);
                f.setCapacity(capacity);
                fileService.saveFacilities(facilities);
                fileService.appendAuditLog("Facility updated – " + name);
                return true;
            }
        }
        return false;
    }

    public void displayActiveFacilities() {
        ArrayList<Facility> active = getAllActiveFacilities();
        if (active.isEmpty()) {
            System.out.println("No active facilities available.");
            return;
        }
        System.out.println("\n========== ACTIVE FACILITIES ==========");
        for (Facility f : active) {
            System.out.println("ID: " + f.getFacilityId() + " | Name: " + f.getName() +
                    " | Location: " + f.getLocation() + " | Capacity: " + f.getCapacity());
        }
        System.out.println("========================================");
    }

    public void displayAllFacilities() {
        if (facilities.isEmpty()) {
            System.out.println("No facilities found.");
            return;
        }
        System.out.println("\n========== ALL FACILITIES ==========");
        for (Facility f : facilities) {
            System.out.println("ID: " + f.getFacilityId() + " | Name: " + f.getName() +
                    " | Location: " + f.getLocation() + " | Capacity: " + f.getCapacity() +
                    " | Active: " + f.isActive());
        }
        System.out.println("=====================================");
    }
}
