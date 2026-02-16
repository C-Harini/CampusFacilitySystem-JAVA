package service;

import facility.Facility;
import facility.TimeSlot;

public class FacilityService {

    // ===== DISPLAY ALL FACILITIES =====
    public void viewFacilities(Facility[] facilities, int facilityCount) {

        if (facilityCount == 0) {
            System.out.println("No facilities available.");
            return;
        }

        System.out.println("===== AVAILABLE FACILITIES =====");
        for (int i = 0; i < facilityCount; i++) {
            System.out.println((i + 1) + ". " + facilities[i].getFacilityName());
        }
    }

    // ===== VIEW FACILITY DETAILS =====
    public void viewFacilityDetails(Facility[] facilities, int facilityCount, int choice) {

        if (choice < 1 || choice > facilityCount) {
            System.out.println("Invalid facility selection.");
            return;
        }

        System.out.println("===== FACILITY DETAILS =====");
        facilities[choice - 1].displayDetails();
    }

    // ===== CHECK AVAILABILITY (SINGLE SLOT) =====
    public void checkAvailabilityForSlot(String facilityName,
            String date,
            String timeSlot) {

        // Since no DB / persistence, assume availability
        System.out.println("Facility is AVAILABLE");
    }

    // ===== CHECK AVAILABILITY (FULL DAY) =====
    public void viewAvailableTimeSlots() {

        TimeSlot[] slots = {
                new TimeSlot("09:00 AM – 10:00 AM"),
                new TimeSlot("10:00 AM – 11:00 AM"),
                new TimeSlot("02:00 PM – 03:00 PM"),
                new TimeSlot("04:00 PM – 05:00 PM")
        };

        System.out.println("===== AVAILABLE TIME SLOTS =====");
        for (TimeSlot slot : slots) {
            slot.display();
        }
    }
}
