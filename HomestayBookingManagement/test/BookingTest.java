
import org.junit.Test;

import models.Booking;

import org.junit.Before;
import static org.junit.Assert.*;
import java.time.LocalDate;

/**
 * Unit Tests for Booking Model Class Tests business rules and validation logic
 */
public class BookingTest {

    private Booking validBooking;
    private LocalDate validBookingDate;
    private LocalDate validDepartureDate;

    @Before
    public void setUp() {
        validBookingDate = LocalDate.now().plusDays(1);
        validDepartureDate = LocalDate.now().plusDays(5);
        validBooking = new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789");
    }

    // ===== HELPER METHOD (JAVA 8 SAFE) =====
    private String repeatChar(String c, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    // ===== CONSTRUCTOR VALIDATION TESTS =====
    @Test
    public void testValidBookingCreation() {
        Booking booking = new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789");

        assertEquals("B00001", booking.getBookingID());
        assertEquals("John Doe", booking.getFullName());
        assertEquals("T00001", booking.getTourID());
        assertEquals(validBookingDate, booking.getBookingDate());
        assertEquals("0123456789", booking.getPhone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBookingID() {
        new Booking(null, "John Doe", "T00001", validBookingDate, "0123456789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyBookingID() {
        new Booking("", "John Doe", "T00001", validBookingDate, "0123456789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhitespaceBookingID() {
        new Booking("   ", "John Doe", "T00001", validBookingDate, "0123456789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCustomerName() {
        new Booking("B00001", null, "T00001", validBookingDate, "0123456789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyCustomerName() {
        new Booking("B00001", "", "T00001", validBookingDate, "0123456789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTourID() {
        new Booking("B00001", "John Doe", null, validBookingDate, "0123456789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTourID() {
        new Booking("B00001", "John Doe", "", validBookingDate, "0123456789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBookingDate() {
        new Booking("B00001", "John Doe", "T00001", null, "0123456789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPhone() {
        new Booking("B00001", "John Doe", "T00001", validBookingDate, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPhone() {
        new Booking("B00001", "John Doe", "T00001", validBookingDate, "");
    }

    // ===== SETTER VALIDATION TESTS =====
    @Test
    public void testSetValidBookingID() {
        validBooking.setBookingID("B00002");
        assertEquals("B00002", validBooking.getBookingID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullBookingID() {
        validBooking.setBookingID(null);
    }

    @Test
    public void testSetValidFullName() {
        validBooking.setFullName("Jane Smith");
        assertEquals("Jane Smith", validBooking.getFullName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullFullName() {
        validBooking.setFullName(null);
    }

    @Test
    public void testSetValidTourID() {
        validBooking.setTourID("T00002");
        assertEquals("T00002", validBooking.getTourID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullTourID() {
        validBooking.setTourID(null);
    }

    @Test
    public void testSetValidBookingDate() {
        LocalDate newDate = LocalDate.now().plusDays(3);
        validBooking.setBookingDate(newDate);
        assertEquals(newDate, validBooking.getBookingDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullBookingDate() {
        validBooking.setBookingDate(null);
    }

    @Test
    public void testSetValidPhone() {
        validBooking.setPhone("0987654321");
        assertEquals("0987654321", validBooking.getPhone());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullPhone() {
        validBooking.setPhone(null);
    }

    // ===== BUSINESS LOGIC TESTS =====
    @Test
    public void testToStringFormat() {
        String result = validBooking.toString();

        String expectedDate = validBookingDate.format(utilities.AppConstants.DATE_FMT);
        String expected = "B00001,John Doe,T00001," + expectedDate + ",0123456789";

        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithSpecialCharacters() {
        Booking booking = new Booking("B00001", "Nguyễn Văn A", "T00001", validBookingDate, "0123456789");

        String result = booking.toString();

        assertTrue(result.contains("Nguyễn Văn A"));
        assertTrue(result.contains("B00001"));
    }

    // ===== BOUNDARY TESTS =====
    @Test
    public void testMinimumValidName() {
        Booking booking = new Booking("B00001", "AB", "T00001", validBookingDate, "0123456789");
        assertEquals("AB", booking.getFullName());
    }

    @Test
    public void testMaximumValidName() {
        String longName = repeatChar("A", 50);

        Booking booking = new Booking("B00001", longName, "T00001", validBookingDate, "0123456789");

        assertEquals(longName, booking.getFullName());
    }

    @Test
    public void testValidPhoneFormat() {
        Booking booking = new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789");

        assertEquals("0123456789", booking.getPhone());
        assertEquals(10, booking.getPhone().length());
    }

    @Test
    public void testBookingIDFormat() {
        Booking booking = new Booking("B12345", "John Doe", "T00001", validBookingDate, "0123456789");

        assertEquals("B12345", booking.getBookingID());
        assertTrue(booking.getBookingID().startsWith("B"));
    }

    @Test
    public void testTourIDFormat() {
        Booking booking = new Booking("B00001", "John Doe", "T12345", validBookingDate, "0123456789");

        assertEquals("T12345", booking.getTourID());
        assertTrue(booking.getTourID().startsWith("T"));
    }
}
