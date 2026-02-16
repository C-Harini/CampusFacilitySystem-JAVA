package main;

import java.util.Scanner;

import user.*;
import facility.*;
import booking.*;
import service.*;

public class App {

    // ===== ARRAYS ONLY (NO COLLECTIONS) =====
    static User[] users = new User[50];
    static Facility[] facilities = new Facility[20];
    static Booking[] bookings = new Booking[100];
    static PermissionRequest[] requests = new PermissionRequest[50];

    static int userCount = 0;
    static int facilityCount = 0;
    static int bookingCount = 0;
    static int requestCount = 0;

    static Scanner sc = new Scanner(System.in);

    static FacilityService facilityService = new FacilityService();
    static BookingService bookingService = new BookingService();

    // ================= MAIN =================
    public static void main(String[] args) {

        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== CAMPUS FACILITY BOOKING SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    register();
                    break;

                case 2:
                    login();
                    break;

                case 3:
                    exit = true;
                    System.out.println("Thank you for using Campus Facility Booking System!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ================= HELPER METHODS =================
    static boolean isValidDate(String date) {
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}"))
            return false;

        String[] p = date.split("-");
        int d = Integer.parseInt(p[0]);
        int m = Integer.parseInt(p[1]);
        int y = Integer.parseInt(p[2]);

        return !(d < 1 || d > 31 || m < 1 || m > 12 || y < 2024);
    }

    static String getTimeSlot(int c) {
        switch (c) {
            case 1:
                return "09:00 AM – 10:00 AM";
            case 2:
                return "10:00 AM – 11:00 AM";
            case 3:
                return "02:00 PM – 03:00 PM";
            case 4:
                return "04:00 PM – 05:00 PM";
            default:
                return null;
        }
    }

    // ================= REGISTER =================
    static void register() {

        System.out.println("\n===== USER REGISTRATION =====");
        System.out.println("1. Student");
        System.out.println("2. Faculty");
        System.out.println("3. Admin");
        System.out.print("Select Role: ");

        int role = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (role == 1) {
            System.out.print("Enter roll number: ");
            String roll = sc.nextLine();
            System.out.print("Enter department: ");
            String dept = sc.nextLine();
            users[userCount++] = new Student(userCount, name, email, password, dept, roll);

        } else if (role == 2) {
            System.out.print("Enter employee ID: ");
            String emp = sc.nextLine();
            System.out.print("Enter department: ");
            String dept = sc.nextLine();
            users[userCount++] = new Faculty(userCount, name, email, password, dept, emp);

        } else if (role == 3) {
            System.out.print("Enter admin code: ");
            String code = sc.nextLine();
            users[userCount++] = new Admin(userCount, name, email, password, code);

        } else {
            System.out.println("Invalid role selected.");
            return;
        }

        System.out.println("Registration successful!");
    }

    // ================= LOGIN =================
    static void login() {

        System.out.println("\n===== USER LOGIN =====");
        System.out.println("1. Student");
        System.out.println("2. Faculty");
        System.out.println("3. Admin");
        System.out.print("Select Role: ");

        int role = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        for (int i = 0; i < userCount; i++) {
            User u = users[i];

            if ((role == 1 && u instanceof Student) ||
                    (role == 2 && u instanceof Faculty) ||
                    (role == 3 && u instanceof Admin)) {

                if (u.login(email, password)) {
                    System.out.println("Login successful!");
                    if (u instanceof Admin)
                        adminMenu((Admin) u);
                    else
                        userMenu(u);
                    return;
                }
            }
        }
        System.out.println("Invalid login credentials.");
    }

    // ================= USER MENU =================
    static void userMenu(User user) {

        boolean logout = false;

        while (!logout) {

            System.out.println("\n===== USER MENU =====");
            System.out.println("1. View Facilities");
            System.out.println("2. Check Facility Availability");
            System.out.println("3. Book Facility");
            System.out.println("4. View My Bookings");
            System.out.println("5. Modify Booking");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Request Special Permission");
            System.out.println("8. View Booking Status");
            System.out.println("9. Logout");
            System.out.print("Enter your choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1:
                    facilityService.viewFacilities(facilities, facilityCount);
                    System.out.println("1. Return to Menu");
                    System.out.println("2. View Facility Details");
                    int opt = sc.nextInt();
                    sc.nextLine();
                    if (opt == 2) {
                        System.out.print("Enter facility number: ");
                        int f = sc.nextInt();
                        sc.nextLine();
                        facilityService.viewFacilityDetails(facilities, facilityCount, f);
                    }
                    break;

                case 2:
                    System.out.println("1. Check availability for a specific time slot");
                    System.out.println("2. View all available time slots for a day");
                    int mode = sc.nextInt();
                    sc.nextLine();

                    if (mode == 1) {
                        System.out.print("Enter facility name: ");
                        String fname = sc.nextLine();
                        System.out.print("Enter date (DD-MM-YYYY): ");
                        String date = sc.nextLine();
                        if (!isValidDate(date)) {
                            System.out.println("Invalid date format.");
                            break;
                        }

                        System.out.println("Select time slot:");
                        for (int i = 1; i <= 4; i++)
                            System.out.println(i + ". " + getTimeSlot(i));

                        int s = sc.nextInt();
                        sc.nextLine();
                        String slot = getTimeSlot(s);
                        if (slot == null) {
                            System.out.println("Invalid slot.");
                            break;
                        }

                        facilityService.checkAvailabilityForSlot(fname, date, slot);
                    } else if (mode == 2) {
                        facilityService.viewAvailableTimeSlots();
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 3:
                    facilityService.viewFacilities(facilities, facilityCount);
                    System.out.print("Select facility number: ");
                    int idx = sc.nextInt();
                    sc.nextLine();

                    if (idx < 1 || idx > facilityCount) {
                        System.out.println("Invalid facility.");
                        break;
                    }

                    System.out.print("Enter date (DD-MM-YYYY): ");
                    String date = sc.nextLine();
                    if (!isValidDate(date)) {
                        System.out.println("Invalid date format.");
                        break;
                    }

                    System.out.println("Select time slot:");
                    for (int i = 1; i <= 4; i++)
                        System.out.println(i + ". " + getTimeSlot(i));

                    int s = sc.nextInt();
                    sc.nextLine();
                    String slot = getTimeSlot(s);
                    if (slot == null) {
                        System.out.println("Invalid slot.");
                        break;
                    }
                    // ===== TIME SLOT COLLISION CHECK =====
                    boolean slotTaken = false;

                    for (int i = 0; i < bookingCount; i++) {
                        Booking b = bookings[i];

                        if (b.getFacility().getFacilityId() == facilities[idx - 1].getFacilityId()
                                && b.getDate().equals(date)
                                && b.getTimeSlot().equals(slot)
                                && b.getStatus().equals("APPROVED")) {

                            slotTaken = true;
                            break;
                        }
                    }

                    if (slotTaken) {
                        System.out.println("Selected time slot is already booked for this facility.");
                        break;
                    }

                    bookingService.createBooking(
                            bookingCount + 1,
                            user,
                            bookings,
                            bookingCount,
                            facilities[idx - 1],
                            date,
                            slot);
                    bookingCount++;
                    break;

                case 4:
                    bookingService.viewUserBookings(bookings, bookingCount, user);
                    break;

                case 5:
                    System.out.print("Enter Booking ID: ");
                    int bid = sc.nextInt();
                    sc.nextLine();

                    Booking found = null;
                    for (int i = 0; i < bookingCount; i++) {
                        if (bookings[i].getBookingId() == bid &&
                                bookings[i].getUser() == user) {
                            found = bookings[i];
                            break;
                        }
                    }

                    if (found == null) {
                        System.out.println("Invalid Booking ID.");
                        break;
                    }

                    System.out.print("Enter new date (DD-MM-YYYY): ");
                    String nd = sc.nextLine();
                    if (!isValidDate(nd)) {
                        System.out.println("Invalid date format.");
                        break;
                    }

                    System.out.println("Select new time slot:");
                    for (int i = 1; i <= 4; i++)
                        System.out.println(i + ". " + getTimeSlot(i));

                    int ns = sc.nextInt();
                    sc.nextLine();
                    String nslot = getTimeSlot(ns);
                    if (nslot == null) {
                        System.out.println("Invalid slot.");
                        break;
                    }

                    found.modify(nd, nslot);
                    System.out.println("Booking updated successfully!");
                    break;

                case 6:
                    System.out.print("Enter Booking ID: ");
                    int cid = sc.nextInt();
                    sc.nextLine();

                    Booking cancel = null;
                    for (int i = 0; i < bookingCount; i++) {
                        if (bookings[i].getBookingId() == cid &&
                                bookings[i].getUser() == user) {
                            cancel = bookings[i];
                            break;
                        }
                    }

                    if (cancel == null) {
                        System.out.println("Invalid Booking ID.");
                        break;
                    }

                    cancel.cancel();
                    System.out.println("Booking cancelled successfully!");
                    break;

                case 7:
                    System.out.print("Enter Booking ID: ");
                    int rb = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter reason: ");
                    String reason = sc.nextLine();
                    requests[requestCount++] = new PermissionRequest(requestCount, rb, reason);
                    System.out.println("Request sent to admin for approval");
                    break;

                case 8:
                    bookingService.viewBookingStatus(bookings, bookingCount, user);
                    break;

                case 9:
                    logout = true;
                    System.out.println("You have been logged out successfully!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // ================= ADMIN MENU =================
    // ================= ADMIN MENU =================
    static void adminMenu(Admin admin) {

        boolean logout = false;

        while (!logout) {

            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. Add Facility");
            System.out.println("2. Update Facility");
            System.out.println("3. Remove Facility");
            System.out.println("4. View All Bookings");
            System.out.println("5. Approve / Reject Booking");
            System.out.println("6. View Special Permission Requests");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                // ===== 1. ADD FACILITY =====
                case 1:
                    System.out.print("Enter facility name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter capacity: ");
                    int cap = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter location: ");
                    String loc = sc.nextLine();
                    System.out.print("Enter description: ");
                    String desc = sc.nextLine();

                    facilities[facilityCount++] = new Facility(facilityCount, name, cap, loc, desc);

                    System.out.println("Facility added successfully!");
                    break;

                // ===== 2. UPDATE FACILITY =====
                case 2:
                    System.out.print("Enter Facility ID: ");
                    int fid = sc.nextInt();
                    sc.nextLine();

                    if (fid < 1 || fid > facilityCount) {
                        System.out.println("Invalid Facility ID.");
                        break;
                    }

                    Facility f = facilities[fid - 1];

                    System.out.println("\n===== UPDATE FACILITY =====");
                    System.out.println("1. Update Facility Name");
                    System.out.println("2. Update Capacity");
                    System.out.println("3. Update Location");
                    System.out.println("4. Update Description");
                    System.out.println("5. Update All Details");
                    System.out.print("Enter your choice: ");

                    int opt = sc.nextInt();
                    sc.nextLine();

                    switch (opt) {
                        case 1:
                            System.out.print("Enter new facility name: ");
                            f.setFacilityName(sc.nextLine());
                            break;

                        case 2:
                            System.out.print("Enter new capacity: ");
                            f.setCapacity(sc.nextInt());
                            sc.nextLine();
                            break;

                        case 3:
                            System.out.print("Enter new location: ");
                            f.setLocation(sc.nextLine());
                            break;

                        case 4:
                            System.out.print("Enter new description: ");
                            f.setDescription(sc.nextLine());
                            break;

                        case 5:
                            System.out.print("Enter new facility name: ");
                            String n = sc.nextLine();
                            System.out.print("Enter new capacity: ");
                            int c = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Enter new location: ");
                            String l = sc.nextLine();
                            System.out.print("Enter new description: ");
                            String d = sc.nextLine();
                            f.updateAll(n, c, l, d);
                            break;

                        default:
                            System.out.println("Invalid update option.");
                            continue;
                    }

                    System.out.println("Facility updated successfully!");
                    break;

                // ===== 3. REMOVE FACILITY =====
                case 3:
                    System.out.print("Enter Facility ID to remove: ");
                    int rid = sc.nextInt();
                    sc.nextLine();

                    if (rid < 1 || rid > facilityCount) {
                        System.out.println("Invalid Facility ID.");
                        break;
                    }

                    for (int i = rid - 1; i < facilityCount - 1; i++) {
                        facilities[i] = facilities[i + 1];
                    }
                    facilityCount--;

                    System.out.println("Facility removed successfully!");
                    break;

                // ===== 4. VIEW ALL BOOKINGS =====
                case 4:
                    bookingService.viewAllBookings(bookings, bookingCount);
                    break;

                // ===== 5. APPROVE / REJECT BOOKING =====
                case 5:
                    System.out.print("Enter Booking ID: ");
                    int bid = sc.nextInt();
                    sc.nextLine();

                    Booking booking = null;
                    for (int i = 0; i < bookingCount; i++) {
                        if (bookings[i].getBookingId() == bid) {
                            booking = bookings[i];
                            break;
                        }
                    }

                    if (booking == null) {
                        System.out.println("Invalid Booking ID.");
                        break;
                    }

                    System.out.println("1. Approve");
                    System.out.println("2. Reject");
                    System.out.print("Enter your choice: ");
                    int action = sc.nextInt();
                    sc.nextLine();

                    if (action == 1) {
                        booking.setStatus("APPROVED");
                        System.out.println("Booking approved successfully!");
                    } else if (action == 2) {
                        booking.setStatus("REJECTED");
                        System.out.println("Booking rejected successfully!");
                    } else {
                        System.out.println("Invalid action selected.");
                    }
                    break;

                // ===== 6. SPECIAL PERMISSION REQUESTS =====
                case 6:
                    if (requestCount == 0) {
                        System.out.println("No special permission requests.");
                        break;
                    }

                    System.out.println("\n===== SPECIAL PERMISSION REQUESTS =====");
                    for (int i = 0; i < requestCount; i++) {
                        System.out.println(
                                "Request ID: " + requests[i].getRequestId() +
                                        " | Booking ID: " + requests[i].getBookingId() +
                                        " | Reason: " + requests[i].getReason());
                    }

                    System.out.print("Enter Booking ID to review: ");
                    int sbid = sc.nextInt();
                    sc.nextLine();

                    Booking target = null;
                    for (int i = 0; i < bookingCount; i++) {
                        if (bookings[i].getBookingId() == sbid) {
                            target = bookings[i];
                            break;
                        }
                    }

                    if (target == null) {
                        System.out.println("Invalid Booking ID.");
                        break;
                    }

                    System.out.println("1. Approve Special Permission");
                    System.out.println("2. Reject");
                    System.out.print("Enter your choice: ");
                    int sp = sc.nextInt();
                    sc.nextLine();

                    if (sp == 1) {
                        target.setStatus("SPECIAL_APPROVED");
                        System.out.println("Special permission approved!");
                    } else if (sp == 2) {
                        target.setStatus("REJECTED");
                        System.out.println("Special permission rejected!");
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                // ===== 7. LOGOUT =====
                case 7:
                    logout = true;
                    System.out.println("Admin logged out successfully!");
                    break;

                default:
                    System.out.println("Invalid admin option.");
            }
        }
    }

}
