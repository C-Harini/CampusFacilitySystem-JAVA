package service;

import booking.Booking;
import booking.BookingStatus;
import booking.PermissionRequest;
import facility.Facility;
import user.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FileService {

    private static final String USERS_FILE = "users.txt";
    private static final String FACILITIES_FILE = "facilities.txt";
    private static final String BOOKINGS_FILE = "bookings.txt";
    private static final String REQUESTS_FILE = "requests.txt";
    private static final String AUDIT_LOG_FILE = "audit_log.txt";

    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return users;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 5) continue;
                String role = parts[4];
                User user = null;
                if (role.equals("STUDENT")) {
                    user = new Student(parts[0], parts[1], parts[2], parts[3]);
                } else if (role.equals("FACULTY")) {
                    user = new Faculty(parts[0], parts[1], parts[2], parts[3]);
                } else if (role.equals("ADMIN")) {
                    user = new Admin(parts[0], parts[1], parts[2], parts[3]);
                }
                if (user != null) users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    public void saveUsers(ArrayList<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                bw.write(u.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public ArrayList<Facility> loadFacilities() {
        ArrayList<Facility> facilities = new ArrayList<>();
        File file = new File(FACILITIES_FILE);
        if (!file.exists()) return facilities;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 5) continue;
                Facility f = new Facility(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]), Boolean.parseBoolean(parts[4]));
                facilities.add(f);
            }
        } catch (IOException e) {
            System.out.println("Error loading facilities: " + e.getMessage());
        }
        return facilities;
    }

    public void saveFacilities(ArrayList<Facility> facilities) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FACILITIES_FILE))) {
            for (Facility f : facilities) {
                bw.write(f.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving facilities: " + e.getMessage());
        }
    }

    public ArrayList<Booking> loadBookings() {
        ArrayList<Booking> bookings = new ArrayList<>();
        File file = new File(BOOKINGS_FILE);
        if (!file.exists()) return bookings;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 8) continue;
                ArrayList<String> slots = new ArrayList<>();
                String[] slotArr = parts[5].split(";");
                for (String s : slotArr) {
                    if (!s.isEmpty()) slots.add(s);
                }
                BookingStatus status = BookingStatus.valueOf(parts[6]);
                Booking b = new Booking(parts[0], parts[1], parts[2], parts[3], parts[4], slots, status, parts[7]);
                bookings.add(b);
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
        return bookings;
    }

    public void saveBookings(ArrayList<Booking> bookings) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking b : bookings) {
                bw.write(b.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    public ArrayList<PermissionRequest> loadRequests() {
        ArrayList<PermissionRequest> requests = new ArrayList<>();
        File file = new File(REQUESTS_FILE);
        if (!file.exists()) return requests;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 9) continue;
                BookingStatus status = BookingStatus.valueOf(parts[8]);
                PermissionRequest r = new PermissionRequest(parts[0], parts[1], parts[2], parts[3],
                        parts[4], parts[5], parts[6], parts[7], status);
                requests.add(r);
            }
        } catch (IOException e) {
            System.out.println("Error loading requests: " + e.getMessage());
        }
        return requests;
    }

    public void saveRequests(ArrayList<PermissionRequest> requests) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REQUESTS_FILE))) {
            for (PermissionRequest r : requests) {
                bw.write(r.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving requests: " + e.getMessage());
        }
    }

    public void appendAuditLog(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(AUDIT_LOG_FILE, true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String timestamp = LocalDateTime.now().format(formatter);
            bw.write("[" + timestamp + "] " + message);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing audit log: " + e.getMessage());
        }
    }
}
