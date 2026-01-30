package models;

import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;

/**
 * Lớp Model Booking - Đại diện cho việc đặt tour của khách hàng
 * 
 * Định Dạng File: BookingID,FullName,TourID,BookingDate,Phone
 * Ví dụ: B00001,Nguyen Van A,T00001,15/12/2025,0123456789
 * 
 * Quy Tắc Nghiệp Vụ:
 * - Định dạng BookingID: B + 5 chữ số (B00001)
 * - Tên khách hàng: 2-50 ký tự
 * - Số điện thoại: chính xác 10 chữ số
 * - Ngày đặt phải trước ngày khởi hành tour
 * - Một booking mỗi tour (tour trở nên không có sẵn sau khi đặt)
 * 
 * @author ThanhDuy
 * @version 1.0
 * @since 2025
 */
public class Booking {

    // ===== CÁC THUỘC TÍNH CHÍNH =====
    private String bookingID; // Mã định danh booking duy nhất (định dạng B00001)
    private String fullName; // Tên đầy đủ khách hàng (2-50 ký tự)
    private String tourID; // Tham chiếu tới tour được đặt (định dạng T00001)
    private LocalDate bookingDate; // Ngày thực hiện booking
    private String phone; // Liên hệ khách hàng (10 chữ số)

    // Date formatter cho tính nhất quán file I/O
    // private static final DateTimeFormatter DATE_FMT =
    // DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constructor for Booking creation
     * 
     * @param bookingID   Unique booking identifier (B00001 format)
     * @param fullName    Customer full name (2-50 characters)
     * @param tourID      Tour being booked (T00001 format)
     * @param bookingDate Date of booking (must be before tour departure)
     * @param phone       Customer phone (exactly 10 digits)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Booking(String bookingID, String fullName, String tourID, LocalDate bookingDate, String phone) {
        if (bookingID == null || bookingID.trim().isEmpty()) {
            throw new IllegalArgumentException("BookingID cannot be null or empty");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (tourID == null || tourID.trim().isEmpty()) {
            throw new IllegalArgumentException("TourID cannot be null or empty");
        }
        if (bookingDate == null) {
            throw new IllegalArgumentException("Booking date cannot be null");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }

        this.bookingID = bookingID;
        this.fullName = fullName;
        this.tourID = tourID; // CRITICAL: Links booking to specific tour
        this.bookingDate = bookingDate;
        this.phone = phone;
    }

    // ===== GETTERS AND SETTERS =====

    /**
     * Get booking unique identifier
     * 
     * @return BookingID in B00001 format
     */
    public String getBookingID() {
        return bookingID;
    }

    /**
     * Set booking identifier
     * 
     * @param bookingID Must follow B00001 format
     */
    public void setBookingID(String bookingID) {
        if (bookingID == null || bookingID.trim().isEmpty()) {
            throw new IllegalArgumentException("BookingID cannot be null or empty");
        }
        this.bookingID = bookingID;
    }

    /**
     * Get customer full name
     * 
     * @return Customer name (used for search functionality)
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Set customer full name
     * 
     * @param fullName Must be 2-50 characters
     */
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        this.fullName = fullName;
    }

    /**
     * Get associated tour ID
     * CRITICAL: This links booking to tour for business logic
     * 
     * @return TourID in T00001 format
     */
    public String getTourID() {
        return tourID;
    }

    /**
     * Set associated tour ID
     * IMPORTANT: Changing this affects tour booking status
     * 
     * @param tourID Must be valid existing tour ID
     */
    public void setTourID(String tourID) {
        if (tourID == null || tourID.trim().isEmpty()) {
            throw new IllegalArgumentException("TourID cannot be null or empty");
        }
        this.tourID = tourID;
    }

    /**
     * Get booking creation date
     * 
     * @return Date when booking was made
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    /**
     * Set booking date
     * 
     * @param bookingDate Must be before tour departure date
     */
    public void setBookingDate(LocalDate bookingDate) {
        if (bookingDate == null) {
            throw new IllegalArgumentException("Booking date cannot be null");
        }
        this.bookingDate = bookingDate;
    }

    /**
     * Get customer phone number
     * 
     * @return Phone number (10 digits)
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set customer phone number
     * 
     * @param phone Must be exactly 10 digits
     */
    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        this.phone = phone;
    }

    /**
     * Convert booking to file format string
     * Format: BookingID,FullName,TourID,BookingDate,Phone
     * 
     * @return Formatted string for file storage
     */
    @Override
    public String toString() {
        try {
            String formattedDate = bookingDate.format(utilities.AppConstants.DATE_FMT);
            return String.format("%s,%s,%s,%s,%s",
                    bookingID, fullName, tourID, formattedDate, phone);
        } catch (Exception e) {
            System.err.println("Error formatting booking: " + e.getMessage());
            return bookingID + ",ERROR,ERROR,ERROR,ERROR";
        }
    }
}
