package service;

import booking.Booking;
import user.User;

public class BookingService {

    public void viewUserBookings(Booking[] bookings, int count, User user) {
        System.out.println("\n===== MY BOOKINGS =====");
        System.out.println("Booking ID | Facility | Date | Time | Status");

        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (bookings[i].getUser() == user) {
                bookings[i].displayForUser(); // ✅ NOW EXISTS
                found = true;
            }
        }

        if (!found) {
            System.out.println("No bookings found.");
        }
    }

    public void viewAllBookings(Booking[] bookings, int count) {
        System.out.println("\n===== ALL BOOKINGS =====");
        System.out.println("Booking ID | User | Facility | Date | Time | Status");

        if (count == 0) {
            System.out.println("No bookings available.");
            return;
        }

        for (int i = 0; i < count; i++) {
            bookings[i].displayForAdmin(); // ✅ NOW EXISTS
        }
    }

    public void viewBookingStatus(Booking[] bookings, int count, User user) {
        System.out.println("\n===== BOOKING STATUS =====");

        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (bookings[i].getUser() == user) {
                System.out.println(
                        "Booking ID: " + bookings[i].getBookingId() +
                                " | Status: " + bookings[i].getStatus());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No bookings found.");
        }
    }

    public void createBooking(int id, User user, Booking[] bookings, int index,
            facility.Facility facility, String date, String slot) {
        bookings[index] = new Booking(id, user, facility, date, slot);
        System.out.println("Booking request submitted successfully!");
    }
}
