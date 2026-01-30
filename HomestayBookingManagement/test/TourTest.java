
import org.junit.Test;

import models.Tour;
import models.TourStatus;

import org.junit.Before;
import static org.junit.Assert.*;
import java.time.LocalDate;

/**
 * Unit Tests for Tour Model Class
 * Tests business rules, validation, and complex logic
 */
public class TourTest {

    private Tour validTour;
    private LocalDate futureDate;
    private LocalDate pastDate;
    private LocalDate today;

    @Before
    public void setUp() {
        today = LocalDate.now();
        futureDate = today.plusDays(10);
        pastDate = today.minusDays(5);

        validTour = new Tour("T00001", "Da Lat Adventure", "3 days 2 nights",
                1500.0, "HS0001", futureDate, futureDate.plusDays(2), 4, false);
    }

    // ===== CONSTRUCTOR TESTS =====

    @Test
    public void testValidTourCreation() {
        // Arrange & Act
        Tour tour = new Tour("T00001", "Da Lat Adventure", "3 days 2 nights",
                1500.0, "HS0001", futureDate, futureDate.plusDays(2), 4, false);

        // Assert
        assertEquals("T00001", tour.getTourId());
        assertEquals("Da Lat Adventure", tour.getTourName());
        assertEquals("3 days 2 nights", tour.getTime());
        assertEquals(1500.0, tour.getPrice(), 0.01);
        assertEquals("HS0001", tour.getHomeID());
        assertEquals(futureDate, tour.getDepartureDate());
        assertEquals(futureDate.plusDays(2), tour.getEndDate());
        assertEquals(4, tour.getNumberTourist());
        assertFalse(tour.isBooked());
    }

    @Test
    public void testDefaultConstructor() {
        // Act
        Tour tour = new Tour();

        // Assert - All fields should be default values
        assertNull(tour.getTourId());
        assertNull(tour.getTourName());
        assertNull(tour.getTime());
        assertEquals(0.0, tour.getPrice(), 0.01);
        assertNull(tour.getHomeID());
        assertNull(tour.getDepartureDate());
        assertNull(tour.getEndDate());
        assertEquals(0, tour.getNumberTourist());
        assertFalse(tour.isBooked());
    }

    // ===== PRICE VALIDATION TESTS =====

    @Test
    public void testSetValidPrice() {
        // Act
        validTour.setPrice(2000.0);

        // Assert
        assertEquals(2000.0, validTour.getPrice(), 0.01);
    }

    @Test
    public void testSetZeroPrice() {
        // Act
        validTour.setPrice(0.0);

        // Assert
        assertEquals(0.0, validTour.getPrice(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativePrice() {
        // Act & Assert
        validTour.setPrice(-100.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetVeryNegativePrice() {
        // Act & Assert
        validTour.setPrice(-1500.0);
    }

    // ===== NUMBER OF TOURISTS VALIDATION TESTS =====

    @Test
    public void testSetValidNumberTourist() {
        // Act
        validTour.setNumberTourist(8);

        // Assert
        assertEquals(8, validTour.getNumberTourist());
    }

    @Test
    public void testSetZeroTourists() {
        // Act
        validTour.setNumberTourist(0);

        // Assert
        assertEquals(0, validTour.getNumberTourist());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeTourists() {
        // Act & Assert
        validTour.setNumberTourist(-1);
    }

    // ===== BUSINESS LOGIC TESTS =====

    @Test
    public void testGetTotalAmount() {
        // Arrange
        validTour.setPrice(1500.0);
        validTour.setNumberTourist(4);

        // Act
        double totalAmount = validTour.getTotalAmount();

        // Assert
        assertEquals(6000.0, totalAmount, 0.01);
    }

    @Test
    public void testGetTotalAmountWithZeroTourists() {
        // Arrange
        validTour.setPrice(1500.0);
        validTour.setNumberTourist(0);

        // Act
        double totalAmount = validTour.getTotalAmount();

        // Assert
        assertEquals(0.0, totalAmount, 0.01);
    }

    @Test
    public void testGetTotalAmountWithZeroPrice() {
        // Arrange
        validTour.setPrice(0.0);
        validTour.setNumberTourist(4);

        // Act
        double totalAmount = validTour.getTotalAmount();

        // Assert
        assertEquals(0.0, totalAmount, 0.01);
    }

    // ===== DATE LOGIC TESTS =====

    @Test
    public void testIsExpiredWithPastDate() {
        // Arrange
        Tour expiredTour = new Tour("T00002", "Past Tour", "2 days 1 night",
                1000.0, "HS0001", pastDate, pastDate.plusDays(1), 2, false);

        // Act & Assert
        assertTrue(expiredTour.isExpired());
    }

    @Test
    public void testIsExpiredWithFutureDate() {
        // Act & Assert
        assertFalse(validTour.isExpired());
    }

    @Test
    public void testIsExpiredWithToday() {
        // Arrange
        Tour todayTour = new Tour("T00003", "Today Tour", "1 day 0 nights",
                500.0, "HS0001", today, today, 1, false);

        // Act & Assert
        assertFalse(todayTour.isExpired()); // Today is not expired
    }

    @Test
    public void testIsUpcomingWithFutureDate() {
        // Act & Assert
        assertTrue(validTour.isUpcoming());
    }

    @Test
    public void testIsUpcomingWithPastDate() {
        // Arrange
        Tour pastTour = new Tour("T00004", "Past Tour", "2 days 1 night",
                1000.0, "HS0001", pastDate, pastDate.plusDays(1), 2, false);

        // Act & Assert
        assertFalse(pastTour.isUpcoming());
    }

    @Test
    public void testIsUpcomingWithToday() {
        // Arrange
        Tour todayTour = new Tour("T00005", "Today Tour", "1 day 0 nights",
                500.0, "HS0001", today, today, 1, false);

        // Act & Assert
        assertFalse(todayTour.isUpcoming()); // Today is not upcoming
    }

    @Test
    public void testHasValidDates() {
        // Act & Assert
        assertTrue(validTour.hasValidDates());
    }

    @Test
    public void testHasInvalidDates() {
        // Arrange - End date before departure date
        Tour invalidTour = new Tour("T00006", "Invalid Tour", "1 day 0 nights",
                500.0, "HS0001", futureDate, futureDate.minusDays(1), 1, false);

        // Act & Assert
        assertFalse(invalidTour.hasValidDates());
    }

    @Test
    public void testHasValidDatesWithSameDate() {
        // Arrange - Same departure and end date
        Tour sameDateTour = new Tour("T00007", "Same Date Tour", "1 day 0 nights",
                500.0, "HS0001", futureDate, futureDate, 1, false);

        // Act & Assert
        assertFalse(sameDateTour.hasValidDates()); // Same date is invalid
    }

    // ===== OVERLAP LOGIC TESTS =====

    @Test
    public void testIsOverlapWithSameHomestay() {
        // Arrange
        Tour tour1 = new Tour("T00008", "Tour 1", "3 days 2 nights",
                1000.0, "HS0001", futureDate, futureDate.plusDays(2), 2, false);
        Tour tour2 = new Tour("T00009", "Tour 2", "2 days 1 night",
                800.0, "HS0001", futureDate.plusDays(1), futureDate.plusDays(2), 2, false);

        // Act & Assert
        assertTrue(tour1.isOverlapWith(tour2));
        assertTrue(tour2.isOverlapWith(tour1));
    }

    @Test
    public void testIsOverlapWithDifferentHomestay() {
        // Arrange
        Tour tour1 = new Tour("T00010", "Tour 1", "3 days 2 nights",
                1000.0, "HS0001", futureDate, futureDate.plusDays(2), 2, false);
        Tour tour2 = new Tour("T00011", "Tour 2", "2 days 1 night",
                800.0, "HS0002", futureDate.plusDays(1), futureDate.plusDays(2), 2, false);

        // Act & Assert
        assertFalse(tour1.isOverlapWith(tour2));
        assertFalse(tour2.isOverlapWith(tour1));
    }

    @Test
    public void testIsOverlapWithNullTour() {
        // Act & Assert
        assertFalse(validTour.isOverlapWith(null));
    }

    @Test
    public void testIsOverlapWithNonOverlappingDates() {
        // Arrange - Tour 2 starts after Tour 1 ends
        Tour tour1 = new Tour("T00012", "Tour 1", "2 days 1 night",
                1000.0, "HS0001", futureDate, futureDate.plusDays(1), 2, false);
        Tour tour2 = new Tour("T00013", "Tour 2", "2 days 1 night",
                800.0, "HS0001", futureDate.plusDays(2), futureDate.plusDays(3), 2, false);

        // Act & Assert
        assertFalse(tour1.isOverlapWith(tour2));
        assertFalse(tour2.isOverlapWith(tour1));
    }

    @Test
    public void testIsOverlapWithAdjacentDates() {
        // Arrange - Tour 2 starts when Tour 1 ends (boundary case)
        Tour tour1 = new Tour("T00014", "Tour 1", "2 days 1 night",
                1000.0, "HS0001", futureDate, futureDate.plusDays(1), 2, false);
        Tour tour2 = new Tour("T00015", "Tour 2", "2 days 1 night",
                800.0, "HS0001", futureDate.plusDays(1), futureDate.plusDays(2), 2, false);

        // Act & Assert
        assertTrue(tour1.isOverlapWith(tour2)); // Adjacent dates overlap
    }

    // ===== STATUS TESTS =====

    @Test
    public void testGetStatusExpired() {
        // Arrange
        Tour expiredTour = new Tour("T00016", "Expired Tour", "2 days 1 night",
                1000.0, "HS0001", pastDate, pastDate.plusDays(1), 2, false);

        // Act & Assert
        assertEquals(TourStatus.EXPIRED, expiredTour.getStatus());
    }

    @Test
    public void testGetStatusBooked() {
        // Arrange
        validTour.setIsBooked(true);

        // Act & Assert
        assertEquals(TourStatus.BOOKED, validTour.getStatus());
    }

    @Test
    public void testGetStatusAvailable() {
        // Act & Assert
        assertEquals(TourStatus.AVAILABLE, validTour.getStatus());
    }

    @Test
    public void testGetStatusPriorityExpiredOverBooked() {
        // Arrange - Expired and booked (expired takes priority)
        Tour expiredBookedTour = new Tour("T00017", "Expired Booked Tour", "2 days 1 night",
                1000.0, "HS0001", pastDate, pastDate.plusDays(1), 2, true);

        // Act & Assert
        assertEquals(TourStatus.EXPIRED, expiredBookedTour.getStatus());
    }

    // ===== COMPARISON TESTS =====

    @Test
    public void testCompareToByTotalAmount() {
        // Arrange
        Tour tour1 = new Tour("T00018", "Tour 1", "2 days 1 night",
                1000.0, "HS0001", futureDate, futureDate.plusDays(1), 2, false); // Total: 2000
        Tour tour2 = new Tour("T00019", "Tour 2", "3 days 2 nights",
                1500.0, "HS0001", futureDate, futureDate.plusDays(2), 4, false); // Total: 6000

        // Act
        int result = tour1.compareTo(tour2);

        // Assert - tour2 should come first (higher total amount)
        assertTrue(result > 0);
    }

    @Test
    public void testCompareToEqual() {
        // Arrange
        Tour tour1 = new Tour("T00020", "Tour 1", "2 days 1 night",
                1000.0, "HS0001", futureDate, futureDate.plusDays(1), 3, false); // Total: 3000
        Tour tour2 = new Tour("T00021", "Tour 2", "3 days 2 nights",
                1500.0, "HS0001", futureDate, futureDate.plusDays(2), 2, false); // Total: 3000

        // Act
        int result = tour1.compareTo(tour2);

        // Assert
        assertEquals(0, result);
    }

    // ===== STRING FORMAT TESTS =====

    @Test
    public void testToStringFormat() {
        // Arrange
        Tour tour = new Tour("T00022", "Test Tour", "2 days 1 night",
                1000.0, "HS0001", futureDate, futureDate.plusDays(1), 2, false);

        // Act
        String result = tour.toString();

        // Assert
        String expectedDeparture = futureDate.format(utilities.AppConstants.DATE_FMT);
        String expectedEnd = futureDate.plusDays(1).format(utilities.AppConstants.DATE_FMT);
        String expected = "T00022,Test Tour,2 days 1 night,1000.0,HS0001," + expectedDeparture + "," + expectedEnd
                + ",2,FALSE";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithBookedTour() {
        // Arrange
        validTour.setIsBooked(true);

        // Act
        String result = validTour.toString();

        // Assert
        assertTrue(result.endsWith(",TRUE"));
    }

    // ===== BOUNDARY TESTS =====

    @Test
    public void testMaximumPrice() {
        // Act
        validTour.setPrice(Double.MAX_VALUE);

        // Assert
        assertEquals(Double.MAX_VALUE, validTour.getPrice(), 0.01);
    }

    @Test
    public void testMaximumTourists() {
        // Act
        validTour.setNumberTourist(Integer.MAX_VALUE);

        // Assert
        assertEquals(Integer.MAX_VALUE, validTour.getNumberTourist());
    }
}