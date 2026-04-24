package service;

import booking.Booking;
import booking.BookingStatus;
import booking.PermissionRequest;
import exception.BookingNotFoundException;
import facility.Facility;
import facility.TimeSlot;
import user.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class BookingService {

    private ArrayList<Booking> bookings;
    private ArrayList<PermissionRequest> permissionRequests;
    private FileService fileService;
    private FacilityService facilityService;
    private int bookingIdCounter;
    private int requestIdCounter;

    public BookingService(FileService fileService, FacilityService facilityService) {
        this.fileService = fileService;
        this.facilityService = facilityService;
        this.bookings = fileService.loadBookings();
        this.permissionRequests = fileService.loadRequests();
        this.bookingIdCounter = bookings.size() + 1;
        this.requestIdCounter = permissionRequests.size() + 1;
    }

    public int getActiveBookingCount(String userId) {
        int count = 0;
        for (Booking b : bookings) {
            if (b.getUserId().equals(userId)) {
                BookingStatus s = b.getStatus();
                if (s == BookingStatus.PENDING || s == BookingStatus.APPROVED ||
                        s == BookingStatus.SPECIAL_REQUESTED || s == BookingStatus.SPECIAL_APPROVED) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isContinuous(ArrayList<Integer> slotNumbers) {
        if (slotNumbers.isEmpty()) return false;
        Collections.sort(slotNumbers);
        for (int i = 1; i < slotNumbers.size(); i++) {
            if (slotNumbers.get(i) != slotNumbers.get(i - 1) + 1) return false;
        }
        return true;
    }

    private boolean isSlotBlocked(String facilityId, String bookingDate, String slotLabel) {
        for (Booking b : bookings) {
            if (!b.getFacilityId().equals(facilityId)) continue;
            if (!b.getBookingDate().equals(bookingDate)) continue;
            BookingStatus s = b.getStatus();
            if (s == BookingStatus.APPROVED || s == BookingStatus.SPECIAL_APPROVED || s == BookingStatus.SPECIAL_REQUESTED) {
                if (b.getTimeSlots().contains(slotLabel)) return true;
            }
        }
        return false;
    }

    public String createBooking(User user, String facilityId, String bookingDate, ArrayList<Integer> slotNumbers) {
        if (getActiveBookingCount(user.getUserId()) >= user.getMaxBookings()) {
            return "ERROR: You have reached your maximum active booking limit of " + user.getMaxBookings() + ".";
        }

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate requestedDate;
        try {
            requestedDate = LocalDate.parse(bookingDate, formatter);
        } catch (Exception e) {
            return "ERROR: Invalid date format. Use yyyy-MM-dd.";
        }

        if (requestedDate.isBefore(today)) {
            return "ERROR: Cannot book for a past date.";
        }

        if (!isContinuous(slotNumbers)) {
            return "ERROR: Selected slots must be continuous.";
        }

        Facility facility = facilityService.getFacilityById(facilityId);
        if (facility == null || !facility.isActive()) {
            return "ERROR: Facility not found or inactive.";
        }

        ArrayList<String> slotLabels = new ArrayList<>();
        for (int slotNum : slotNumbers) {
            boolean found = false;
            for (TimeSlot ts : facility.getTimeSlots()) {
                if (ts.getSlotNumber() == slotNum) {
                    slotLabels.add(ts.toString());
                    found = true;
                    break;
                }
            }
            if (!found) return "ERROR: Slot number " + slotNum + " is invalid.";
        }

        for (String slotLabel : slotLabels) {
            if (isSlotBlocked(facilityId, bookingDate, slotLabel)) {
                return "ERROR: One or more selected slots are already booked. Entire booking rejected.";
            }
        }

        String bookingId = "BKG" + String.format("%04d", bookingIdCounter++);
        Booking booking = new Booking(bookingId, user.getUserId(), facilityId, facility.getName(),
                bookingDate, slotLabels, BookingStatus.PENDING, "");
        bookings.add(booking);
        fileService.saveBookings(bookings);
        fileService.appendAuditLog("Booking created – " + facility.getName() + " – PENDING");

        return "SUCCESS: Booking created with ID: " + bookingId + " | Status: PENDING";
    }

    public String requestSpecialPermission(User user, String bookingId, String reason) throws BookingNotFoundException {
        Booking booking = getBookingById(bookingId);
        if (!booking.getUserId().equals(user.getUserId())) {
            return "ERROR: You do not own this booking.";
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            return "ERROR: Only PENDING bookings can request special permission.";
        }

        booking.setStatus(BookingStatus.SPECIAL_REQUESTED);
        booking.setRemarks("Special request: " + reason);

        String requestId = "REQ" + String.format("%04d", requestIdCounter++);
        PermissionRequest request = new PermissionRequest(requestId, bookingId, user.getUserId(),
                booking.getFacilityId(), booking.getFacilityName(), booking.getBookingDate(),
                booking.timeSlotsToString(), reason, BookingStatus.SPECIAL_REQUESTED);
        permissionRequests.add(request);

        fileService.saveBookings(bookings);
        fileService.saveRequests(permissionRequests);
        fileService.appendAuditLog("Special permission requested – Booking: " + bookingId + " – " + booking.getFacilityName());

        return "SUCCESS: Special permission requested. Request ID: " + requestId;
    }

    public String cancelBooking(User user, String bookingId) throws BookingNotFoundException {
        Booking booking = getBookingById(bookingId);
        if (!booking.getUserId().equals(user.getUserId())) {
            return "ERROR: You do not own this booking.";
        }
        BookingStatus s = booking.getStatus();
        if (s == BookingStatus.CANCELLED || s == BookingStatus.REJECTED) {
            return "ERROR: Booking is already " + s.name() + ".";
        }
        booking.setStatus(BookingStatus.CANCELLED);
        fileService.saveBookings(bookings);
        fileService.appendAuditLog("Booking cancelled – " + bookingId + " – " + booking.getFacilityName());
        return "SUCCESS: Booking " + bookingId + " cancelled.";
    }

    public String adminApproveBooking(String bookingId) throws BookingNotFoundException {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            return "ERROR: Only PENDING bookings can be approved.";
        }
        booking.setStatus(BookingStatus.APPROVED);
        fileService.saveBookings(bookings);
        fileService.appendAuditLog("Booking approved – " + bookingId + " – " + booking.getFacilityName());
        return "SUCCESS: Booking " + bookingId + " approved.";
    }

    public String adminRejectBooking(String bookingId) throws BookingNotFoundException {
        Booking booking = getBookingById(bookingId);
        BookingStatus s = booking.getStatus();
        if (s == BookingStatus.CANCELLED || s == BookingStatus.REJECTED) {
            return "ERROR: Booking is already " + s.name() + ".";
        }
        booking.setStatus(BookingStatus.REJECTED);
        fileService.saveBookings(bookings);
        fileService.appendAuditLog("Booking rejected – " + bookingId + " – " + booking.getFacilityName());
        return "SUCCESS: Booking " + bookingId + " rejected.";
    }

    public String adminApproveSpecialRequest(String requestId) throws BookingNotFoundException {
        PermissionRequest request = getRequestById(requestId);
        if (request.getStatus() != BookingStatus.SPECIAL_REQUESTED) {
            return "ERROR: Request is not in SPECIAL_REQUESTED state.";
        }
        request.setStatus(BookingStatus.SPECIAL_APPROVED);

        Booking booking = getBookingById(request.getBookingId());
        booking.setStatus(BookingStatus.SPECIAL_APPROVED);

        fileService.saveRequests(permissionRequests);
        fileService.saveBookings(bookings);
        fileService.appendAuditLog("Special request approved – " + requestId + " – Booking: " + request.getBookingId());
        return "SUCCESS: Special request " + requestId + " approved.";
    }

    public String adminRejectSpecialRequest(String requestId) throws BookingNotFoundException {
        PermissionRequest request = getRequestById(requestId);
        if (request.getStatus() != BookingStatus.SPECIAL_REQUESTED) {
            return "ERROR: Request is not in SPECIAL_REQUESTED state.";
        }
        request.setStatus(BookingStatus.REJECTED);

        Booking booking = getBookingById(request.getBookingId());
        booking.setStatus(BookingStatus.REJECTED);

        fileService.saveRequests(permissionRequests);
        fileService.saveBookings(bookings);
        fileService.appendAuditLog("Special request rejected – " + requestId + " – Booking: " + request.getBookingId());
        return "SUCCESS: Special request " + requestId + " rejected.";
    }

    public void displayUserBookings(String userId) {
        boolean found = false;
        System.out.println("\n========== YOUR BOOKINGS ==========");
        for (Booking b : bookings) {
            if (b.getUserId().equals(userId)) {
                printBooking(b);
                found = true;
            }
        }
        if (!found) System.out.println("No bookings found.");
        System.out.println("====================================");
    }

    public void displayAllBookings() {
        System.out.println("\n========== ALL BOOKINGS ==========");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        }
        // Show SPECIAL_REQUESTED first
        System.out.println("--- SPECIAL REQUESTED (Priority) ---");
        boolean hasSpecial = false;
        for (Booking b : bookings) {
            if (b.getStatus() == BookingStatus.SPECIAL_REQUESTED) {
                printBooking(b);
                hasSpecial = true;
            }
        }
        if (!hasSpecial) System.out.println("None.");

        System.out.println("--- OTHER BOOKINGS ---");
        for (Booking b : bookings) {
            if (b.getStatus() != BookingStatus.SPECIAL_REQUESTED) {
                printBooking(b);
            }
        }
        System.out.println("==================================");
    }

    public void displayPendingSpecialRequests() {
        System.out.println("\n========== PENDING SPECIAL REQUESTS ==========");
        boolean found = false;
        for (PermissionRequest r : permissionRequests) {
            if (r.getStatus() == BookingStatus.SPECIAL_REQUESTED) {
                System.out.println("Request ID: " + r.getRequestId() +
                        " | Booking ID: " + r.getBookingId() +
                        " | User: " + r.getUserId() +
                        " | Facility: " + r.getFacilityName() +
                        " | Date: " + r.getBookingDate() +
                        " | Slots: " + r.getTimeSlots() +
                        " | Reason: " + r.getReason());
                found = true;
            }
        }
        if (!found) System.out.println("No pending special requests.");
        System.out.println("===============================================");
    }

    private void printBooking(Booking b) {
        System.out.println("ID: " + b.getBookingId() +
                " | Facility: " + b.getFacilityName() +
                " | Date: " + b.getBookingDate() +
                " | Slots: " + b.timeSlotsToString() +
                " | Status: " + b.getStatus().name() +
                " | Remarks: " + b.getRemarks());
    }

    public Booking getBookingById(String bookingId) throws BookingNotFoundException {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId)) return b;
        }
        throw new BookingNotFoundException("Booking not found with ID: " + bookingId);
    }

    public PermissionRequest getRequestById(String requestId) throws BookingNotFoundException {
        for (PermissionRequest r : permissionRequests) {
            if (r.getRequestId().equals(requestId)) return r;
        }
        throw new BookingNotFoundException("Permission request not found with ID: " + requestId);
    }
}
