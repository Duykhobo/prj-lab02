

import utilities.DateValidator;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;

/**
 * Unit Tests for DateValidator Utility Class
 * Tests date validation business rules and edge cases
 */
public class DateValidatorTest {
    
    // ===== VALID DATE STRING TESTS =====
    
    @Test
    public void testValidDateString() throws Exception {
        // Arrange - Use a future date
        LocalDate futureDate = LocalDate.now().plusDays(30);
        String validDateStr = futureDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        // Act
        LocalDate result = DateValidator.validateDate(validDateStr);
        
        // Assert
        assertEquals(futureDate, result);
    }
    
    @Test
    public void testValidDateStringTomorrow() throws Exception {
        // Arrange
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String tomorrowStr = tomorrow.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        // Act
        LocalDate result = DateValidator.validateDate(tomorrowStr);
        
        // Assert
        assertEquals(tomorrow, result);
    }
    
    // ===== INVALID DATE FORMAT TESTS =====
    
    @Test
    public void testInvalidDateFormat() {
        // Arrange
        String invalidFormat = "2025-12-25";
        
        // Act & Assert
        try {
            DateValidator.validateDate(invalidFormat);
            fail("Expected Exception for invalid format");
        } catch (Exception e) {
            assertEquals("Invalid date format. Use dd/MM/yyyy", e.getMessage());
        }
    }
    
    @Test
    public void testInvalidDateFormatSlashes() {
        // Arrange
        String invalidFormat = "25-12-2025";
        
        // Act & Assert
        try {
            DateValidator.validateDate(invalidFormat);
            fail("Expected Exception for invalid format");
        } catch (Exception e) {
            assertEquals("Invalid date format. Use dd/MM/yyyy", e.getMessage());
        }
    }
    
    @Test
    public void testInvalidDateFormatNoSeparators() {
        // Arrange
        String invalidFormat = "25122025";
        
        // Act & Assert
        try {
            DateValidator.validateDate(invalidFormat);
            fail("Expected Exception for invalid format");
        } catch (Exception e) {
            assertEquals("Invalid date format. Use dd/MM/yyyy", e.getMessage());
        }
    }
    
    @Test
    public void testInvalidDateFormatText() {
        // Arrange
        String invalidFormat = "December 25, 2025";
        
        // Act & Assert
        try {
            DateValidator.validateDate(invalidFormat);
            fail("Expected Exception for invalid format");
        } catch (Exception e) {
            assertEquals("Invalid date format. Use dd/MM/yyyy", e.getMessage());
        }
    }
    
    @Test
    public void testEmptyDateString() {
        // Arrange
        String emptyDate = "";
        
        // Act & Assert
        try {
            DateValidator.validateDate(emptyDate);
            fail("Expected Exception for empty date");
        } catch (Exception e) {
            assertEquals("Invalid date format. Use dd/MM/yyyy", e.getMessage());
        }
    }
    
    @Test
    public void testNullDateString() {
        // Arrange
        String nullDate = null;
        
        // Act & Assert
        try {
            DateValidator.validateDate(nullDate);
            fail("Expected Exception for null date");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Invalid date format"));
        }
    }
    
    // ===== BUSINESS RULE TESTS =====
    
    @Test
    public void testDateInPast() {
        // Arrange
        LocalDate pastDate = LocalDate.now().minusDays(1);
        
        // Act & Assert
        try {
            DateValidator.validateDate(pastDate);
            fail("Expected Exception for past date");
        } catch (Exception e) {
            assertEquals("Date cannot be in the past", e.getMessage());
        }
    }
    
    @Test
    public void testDateToday() throws Exception {
        // Arrange
        LocalDate today = LocalDate.now();
        
        // Act & Assert - Today should be valid (not in past)
        DateValidator.validateDate(today);
        // No exception should be thrown
    }
    
    @Test
    public void testDateTooFarInFuture() {
        // Arrange
        LocalDate farFuture = LocalDate.now().plusYears(6);
        
        // Act & Assert
        try {
            DateValidator.validateDate(farFuture);
            fail("Expected Exception for date too far in future");
        } catch (Exception e) {
            assertEquals("Date cannot be more than 5 years in the future", e.getMessage());
        }
    }
    
    @Test
    public void testDateExactly5YearsInFuture() throws Exception {
        // Arrange
        LocalDate fiveYearsFuture = LocalDate.now().plusYears(5);
        
        // Act & Assert - Exactly 5 years should be valid
        DateValidator.validateDate(fiveYearsFuture);
        // No exception should be thrown
    }
    
    @Test
    public void testDateAlmost5YearsInFuture() throws Exception {
        // Arrange
        LocalDate almostFiveYears = LocalDate.now().plusYears(5).minusDays(1);
        
        // Act & Assert
        DateValidator.validateDate(almostFiveYears);
        // No exception should be thrown
    }
    
    // ===== END DATE VALIDATION TESTS =====
    
    @Test
    public void testValidEndDate() throws Exception {
        // Arrange
        LocalDate departureDate = LocalDate.now().plusDays(10);
        LocalDate endDate = departureDate.plusDays(3);
        
        // Act & Assert
        DateValidator.validateEndDate(endDate, departureDate);
        // No exception should be thrown
    }
    
    @Test
    public void testEndDateBeforeDepartureDate() {
        // Arrange
        LocalDate departureDate = LocalDate.now().plusDays(10);
        LocalDate endDate = departureDate.minusDays(1);
        
        // Act & Assert
        try {
            DateValidator.validateEndDate(endDate, departureDate);
            fail("Expected Exception for end date before departure");
        } catch (Exception e) {
            assertEquals("End Date must be after Departure Date", e.getMessage());
        }
    }
    
    @Test
    public void testEndDateSameAsDepartureDate() {
        // Arrange
        LocalDate departureDate = LocalDate.now().plusDays(10);
        LocalDate endDate = departureDate;
        
        // Act & Assert
        try {
            DateValidator.validateEndDate(endDate, departureDate);
            fail("Expected Exception for same departure and end date");
        } catch (Exception e) {
            assertEquals("End Date cannot be same as Departure Date", e.getMessage());
        }
    }
    
    @Test
    public void testEndDateTooLongAfterDeparture() {
        // Arrange
        LocalDate departureDate = LocalDate.now().plusDays(10);
        LocalDate endDate = departureDate.plusDays(31); // 31 days > 30 days limit
        
        // Act & Assert
        try {
            DateValidator.validateEndDate(endDate, departureDate);
            fail("Expected Exception for tour duration exceeding 30 days");
        } catch (Exception e) {
            assertEquals("Tour duration cannot exceed 30 days", e.getMessage());
        }
    }
    
    @Test
    public void testEndDateExactly30DaysAfterDeparture() throws Exception {
        // Arrange
        LocalDate departureDate = LocalDate.now().plusDays(10);
        LocalDate endDate = departureDate.plusDays(30); // Exactly 30 days
        
        // Act & Assert
        DateValidator.validateEndDate(endDate, departureDate);
        // No exception should be thrown
    }
    
    @Test
    public void testEndDateAlmost30DaysAfterDeparture() throws Exception {
        // Arrange
        LocalDate departureDate = LocalDate.now().plusDays(10);
        LocalDate endDate = departureDate.plusDays(29); // 29 days < 30 days limit
        
        // Act & Assert
        DateValidator.validateEndDate(endDate, departureDate);
        // No exception should be thrown
    }
    
    // ===== BOUNDARY TESTS =====
    
    @Test
    public void testLeapYearDate() throws Exception {
        // Arrange - February 29 in a future leap year
        String leapYearDate = "29/02/2028"; // 2028 is a leap year
        
        // Act
        LocalDate result = DateValidator.validateDate(leapYearDate);
        
        // Assert
        assertEquals(LocalDate.of(2028, 2, 29), result);
    }
    
    @Test
    public void testInvalidLeapYearDate() {
        // Arrange - February 29 in a non-leap year (2023 is not a leap year)
        String invalidLeapDate = "29/02/2023";
        
        // Act & Assert
        try {
            DateValidator.validateDate(invalidLeapDate);
            fail("Expected Exception for invalid leap year date");
        } catch (Exception e) {
            // Should fail due to past date, not format
            assertEquals("Date cannot be in the past", e.getMessage());
        }
    }
    
    @Test
    public void testInvalidDayOfMonth() {
        // Arrange - Day 32 doesn't exist
        String invalidDay = "32/01/2025";
        
        // Act & Assert
        try {
            DateValidator.validateDate(invalidDay);
            fail("Expected Exception for invalid day");
        } catch (Exception e) {
            assertEquals("Invalid date format. Use dd/MM/yyyy", e.getMessage());
        }
    }
    
    @Test
    public void testInvalidMonth() {
        // Arrange - Month 13 doesn't exist
        String invalidMonth = "15/13/2025";
        
        // Act & Assert
        try {
            DateValidator.validateDate(invalidMonth);
            fail("Expected Exception for invalid month");
        } catch (Exception e) {
            assertEquals("Invalid date format. Use dd/MM/yyyy", e.getMessage());
        }
    }
    
    @Test
    public void testSingleDigitDayAndMonth() throws Exception {
        // Arrange - Use a future date
        LocalDate futureDate = LocalDate.now().plusMonths(2).withDayOfMonth(5);
        String singleDigitDate = futureDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        // Act
        LocalDate result = DateValidator.validateDate(singleDigitDate);
        
        // Assert
        assertEquals(futureDate, result);
    }
}