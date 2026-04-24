package main;

import booking.Booking;
import exception.BookingNotFoundException;
import exception.DuplicateEmailException;
import exception.InvalidChoiceException;
import facility.Facility;
import facility.TimeSlot;
import service.BookingService;
import service.FacilityService;
import service.FileService;
import user.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<User> users = new ArrayList<>();
    private static FileService fileService = new FileService();
    private static FacilityService facilityService = new FacilityService(fileService);
    private static BookingService bookingService = new BookingService(fileService, facilityService);
    private static int userIdCounter = 1;

    public static void main(String[] args) {
        loadUsers();
        seedDefaultAdmin();

        System.out.println("========================================");
        System.out.println("   CAMPUS FACILITY BOOKING SYSTEM");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            try {
                System.out.println("\n1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");
                int choice = readInt();
                switch (choice) {
                    case 1: registerUser(); break;
                    case 2: loginUser(); break;
                    case 3:
                        System.out.println("Exiting system. Goodbye!");
                        running = false;
                        break;
                    default:
                        throw new InvalidChoiceException("Invalid choice. Please select 1-3.");
                }
            } catch (InvalidChoiceException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private static void loadUsers() {
        users = fileService.loadUsers();
        userIdCounter = users.size() + 1;
    }

    private static void seedDefaultAdmin() {
        for (User u : users) {
            if (u.getRole().equals("ADMIN")) return;
        }
        Admin admin = new Admin("USR001", "System Admin", "admin@campus.edu", "admin123");
        users.add(admin);
        fileService.saveUsers(users);
        userIdCounter = users.size() + 1;
        System.out.println("Default admin created: admin@campus.edu / admin123");
    }

    private static void registerUser() {
        System.out.println("\n--- REGISTER ---");
        System.out.println("Select role:");
        System.out.println("1. Student");
        System.out.println("2. Faculty");
        System.out.print("Enter choice: ");

        int roleChoice;
        try {
            roleChoice = readInt();
            if (roleChoice < 1 || roleChoice > 2) {
                System.out.println("ERROR: Invalid role choice.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("ERROR: Please enter a valid number.");
            scanner.nextLine();
            return;
        }

        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        try {
            checkDuplicateEmail(email);
        } catch (DuplicateEmailException e) {
            System.out.println("ERROR: " + e.getMessage());
            return;
        }

        String userId = "USR" + String.format("%03d", userIdCounter++);
        User newUser;
        if (roleChoice == 1) {
            newUser = new Student(userId, name, email, password);
        } else {
            newUser = new Faculty(userId, name, email, password);
        }
        users.add(newUser);
        fileService.saveUsers(users);
        fileService.appendAuditLog("User registered – " + name + " – " + newUser.getRole());
        System.out.println("Registration successful! Your User ID: " + userId);
    }

    private static void checkDuplicateEmail(String email) throws DuplicateEmailException {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new DuplicateEmailException("Email already registered: " + email);
            }
        }
    }

    private static void loginUser() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        User loggedIn = null;
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                loggedIn = u;
                break;
            }
        }

        if (loggedIn == null) {
            System.out.println("ERROR: Invalid email or password.");
            return;
        }

        System.out.println("\nWelcome, " + loggedIn.getName() + " (" + loggedIn.getRole() + ")!");
        fileService.appendAuditLog("User logged in – " + loggedIn.getName() + " – " + loggedIn.getRole());

        if (loggedIn.getRole().equals("ADMIN")) {
            adminMenu((Admin) loggedIn);
        } else {
            userMenu(loggedIn);
        }
    }

    private static void userMenu(User user) {
        boolean active = true;
        while (active) {
            try {
                System.out.println("\n========== USER MENU ==========");
                System.out.println("1. View Available Facilities");
                System.out.println("2. Create Booking");
                System.out.println("3. View My Bookings");
                System.out.println("4. Cancel Booking");
                System.out.println("5. Request Special Permission");
                System.out.println("6. Logout");
                System.out.print("Enter choice: ");
                int choice = readInt();
                switch (choice) {
                    case 1:
                        facilityService.displayActiveFacilities();
                        break;
                    case 2:
                        createBookingFlow(user);
                        break;
                    case 3:
                        bookingService.displayUserBookings(user.getUserId());
                        break;
                    case 4:
                        cancelBookingFlow(user);
                        break;
                    case 5:
                        requestSpecialPermissionFlow(user);
                        break;
                    case 6:
                        System.out.println("Logged out.");
                        active = false;
                        break;
                    default:
                        throw new InvalidChoiceException("Invalid choice. Please select 1-6.");
                }
            } catch (InvalidChoiceException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private static void createBookingFlow(User user) {
        facilityService.displayActiveFacilities();
        System.out.print("Enter Facility ID: ");
        String facilityId = scanner.nextLine().trim();

        Facility facility = facilityService.getFacilityById(facilityId);
        if (facility == null || !facility.isActive()) {
            System.out.println("ERROR: Facility not found or inactive.");
            return;
        }

        System.out.print("Enter booking date (yyyy-MM-dd): ");
        String date = scanner.nextLine().trim();

        System.out.println("\nAvailable Slots:");
        for (TimeSlot ts : facility.getTimeSlots()) {
            System.out.println("  " + ts);
        }

        System.out.print("Enter slot numbers (comma-separated, e.g. 1,2,3): ");
        String slotsInput = scanner.nextLine().trim();
        ArrayList<Integer> slotNumbers = new ArrayList<>();
        try {
            String[] parts = slotsInput.split(",");
            for (String p : parts) {
                slotNumbers.add(Integer.parseInt(p.trim()));
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid slot numbers.");
            return;
        }

        String result = bookingService.createBooking(user, facilityId, date, slotNumbers);
        System.out.println(result);
    }

    private static void cancelBookingFlow(User user) {
        bookingService.displayUserBookings(user.getUserId());
        System.out.print("Enter Booking ID to cancel: ");
        String bookingId = scanner.nextLine().trim();
        try {
            String result = bookingService.cancelBooking(user, bookingId);
            System.out.println(result);
        } catch (BookingNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void requestSpecialPermissionFlow(User user) {
        bookingService.displayUserBookings(user.getUserId());
        System.out.print("Enter Booking ID for special permission request: ");
        String bookingId = scanner.nextLine().trim();
        System.out.print("Enter reason for special permission: ");
        String reason = scanner.nextLine().trim();
        try {
            String result = bookingService.requestSpecialPermission(user, bookingId, reason);
            System.out.println(result);
        } catch (BookingNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void adminMenu(Admin admin) {
        boolean active = true;
        while (active) {
            try {
                System.out.println("\n========== ADMIN MENU ==========");
                System.out.println("1. View All Bookings");
                System.out.println("2. View Pending Special Requests (Priority)");
                System.out.println("3. Approve/Reject Special Request");
                System.out.println("4. Approve Booking");
                System.out.println("5. Reject Booking");
                System.out.println("6. Manage Facilities");
                System.out.println("7. View All Users");
                System.out.println("8. Logout");
                System.out.print("Enter choice: ");
                int choice = readInt();
                switch (choice) {
                    case 1:
                        bookingService.displayAllBookings();
                        break;
                    case 2:
                        bookingService.displayPendingSpecialRequests();
                        break;
                    case 3:
                        handleSpecialRequestFlow();
                        break;
                    case 4:
                        approveBookingFlow();
                        break;
                    case 5:
                        rejectBookingFlow();
                        break;
                    case 6:
                        facilityManagementMenu();
                        break;
                    case 7:
                        displayAllUsers();
                        break;
                    case 8:
                        System.out.println("Logged out.");
                        active = false;
                        break;
                    default:
                        throw new InvalidChoiceException("Invalid choice. Please select 1-8.");
                }
            } catch (InvalidChoiceException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private static void handleSpecialRequestFlow() {
        bookingService.displayPendingSpecialRequests();
        System.out.print("Enter Request ID: ");
        String requestId = scanner.nextLine().trim();
        System.out.println("1. Approve   2. Reject");
        System.out.print("Enter choice: ");
        try {
            int choice = readInt();
            if (choice == 1) {
                String result = bookingService.adminApproveSpecialRequest(requestId);
                System.out.println(result);
            } else if (choice == 2) {
                String result = bookingService.adminRejectSpecialRequest(requestId);
                System.out.println(result);
            } else {
                System.out.println("ERROR: Invalid choice.");
            }
        } catch (BookingNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("ERROR: Invalid input.");
            scanner.nextLine();
        }
    }

    private static void approveBookingFlow() {
        bookingService.displayAllBookings();
        System.out.print("Enter Booking ID to approve: ");
        String bookingId = scanner.nextLine().trim();
        try {
            String result = bookingService.adminApproveBooking(bookingId);
            System.out.println(result);
        } catch (BookingNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void rejectBookingFlow() {
        bookingService.displayAllBookings();
        System.out.print("Enter Booking ID to reject: ");
        String bookingId = scanner.nextLine().trim();
        try {
            String result = bookingService.adminRejectBooking(bookingId);
            System.out.println(result);
        } catch (BookingNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void facilityManagementMenu() {
        boolean active = true;
        while (active) {
            try {
                System.out.println("\n--- FACILITY MANAGEMENT ---");
                System.out.println("1. View All Facilities");
                System.out.println("2. Add Facility");
                System.out.println("3. Update Facility");
                System.out.println("4. Remove Facility (Soft Delete)");
                System.out.println("5. Back");
                System.out.print("Enter choice: ");
                int choice = readInt();
                switch (choice) {
                    case 1:
                        facilityService.displayAllFacilities();
                        break;
                    case 2:
                        addFacilityFlow();
                        break;
                    case 3:
                        updateFacilityFlow();
                        break;
                    case 4:
                        deleteFacilityFlow();
                        break;
                    case 5:
                        active = false;
                        break;
                    default:
                        throw new InvalidChoiceException("Invalid choice. Please select 1-5.");
                }
            } catch (InvalidChoiceException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("ERROR: Invalid input.");
                scanner.nextLine();
            }
        }
    }

    private static void addFacilityFlow() {
        System.out.print("Enter facility name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter location: ");
        String location = scanner.nextLine().trim();
        System.out.print("Enter capacity: ");
        try {
            int capacity = readInt();
            facilityService.addFacility(name, location, capacity);
            System.out.println("Facility added successfully.");
        } catch (InputMismatchException e) {
            System.out.println("ERROR: Invalid capacity.");
            scanner.nextLine();
        }
    }

    private static void updateFacilityFlow() {
        facilityService.displayAllFacilities();
        System.out.print("Enter Facility ID to update: ");
        String facilityId = scanner.nextLine().trim();
        System.out.print("Enter new name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter new location: ");
        String location = scanner.nextLine().trim();
        System.out.print("Enter new capacity: ");
        try {
            int capacity = readInt();
            boolean updated = facilityService.updateFacility(facilityId, name, location, capacity);
            System.out.println(updated ? "Facility updated successfully." : "ERROR: Facility not found or inactive.");
        } catch (InputMismatchException e) {
            System.out.println("ERROR: Invalid capacity.");
            scanner.nextLine();
        }
    }

    private static void deleteFacilityFlow() {
        facilityService.displayAllFacilities();
        System.out.print("Enter Facility ID to remove: ");
        String facilityId = scanner.nextLine().trim();
        boolean deleted = facilityService.softDeleteFacility(facilityId);
        System.out.println(deleted ? "Facility removed (soft delete) successfully." : "ERROR: Facility not found.");
    }

    private static void displayAllUsers() {
        System.out.println("\n========== ALL USERS ==========");
        if (users.isEmpty()) {
            System.out.println("No users found.");
        }
        for (User u : users) {
            System.out.println("ID: " + u.getUserId() + " | Name: " + u.getName() +
                    " | Email: " + u.getEmail() + " | Role: " + u.getRole());
        }
        System.out.println("================================");
    }

    private static int readInt() {
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }
}
