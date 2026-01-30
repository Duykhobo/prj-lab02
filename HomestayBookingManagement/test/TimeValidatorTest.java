
import utilities.TimeValidator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit Tests for TimeValidator Utility Class
 * Tests time format validation and business rules
 */
public class TimeValidatorTest {

    // ===== VALID TIME FORMAT TESTS =====

    @Test
    public void testValidTimeFormat() throws Exception {
        // Act & Assert - No exception should be thrown
        TimeValidator.validateTimeFormat("3 days 2 nights");
        TimeValidator.validateTimeFormat("1 day 0 nights");
        TimeValidator.validateTimeFormat("5 days 4 nights");
        TimeValidator.validateTimeFormat("10 days 9 nights");
    }

    @Test
    public void testValidTimeFormatSingularDay() throws Exception {
        // Act & Assert
        TimeValidator.validateTimeFormat("1 day 0 nights");
    }

    @Test
    public void testValidTimeFormatSingularNight() throws Exception {
        // Act & Assert
        TimeValidator.validateTimeFormat("2 days 1 night");
    }

    @Test
    public void testValidTimeFormatPluralDaysAndNights() throws Exception {
        // Act & Assert
        TimeValidator.validateTimeFormat("4 days 3 nights");
    }

    // ===== INVALID FORMAT TESTS =====

    @Test
    public void testInvalidFormatMissingNights() {
        // Act & Assert
        try {
            TimeValidator.validateTimeFormat("3 days");
            fail("Expected Exception for missing nights");
        } catch (Exception e) {
            assertEquals("Invalid format. Use: 'X days Y nights' (e.g., '3 days 2 nights')", e.getMessage());
        }
    }

    @Test
    public void testInvalidFormatMissingDays() {
        // Act & Assert
        try {
            TimeValidator.validateTimeFormat("2 nights");
            fail("Expected Exception for missing days");
        } catch (Exception e) {
            assertEquals("Invalid format. Use: 'X days Y nights' (e.g., '3 days 2 nights')", e.getMessage());
        }
    }

    @Test
    public void testInvalidFormatWrongOrder() {
        // Act & Assert
        try {
            TimeValidator.validateTimeFormat("2 nights 3 days");
            fail("Expected Exception for wrong order");
        } catch (Exception e) {
            assertEquals("Invalid format. Use: 'X days Y nights' (e.g., '3 days 2 nights')", e.getMessage());
        }
    }

    @Test
    public void testInvalidFormatNoNumbers() {
        // Act & Assert
        try {
            TimeValidator.validateTimeFormat("three days two nights");
            fail("Expected Exception for text numbers");
        } catch (Exception e) {
            assertEquals("Invalid format. Use: 'X days Y nights' (e.g., '3 days 2 nights')", e.getMessage());
        }
    }

    @Test
    public void testInvalidFormatExtraWords() {
        // Act & Assert
        try {
            TimeValidator.validateTimeFormat("3 days and 2 nights");
            fail("Expected Exception for extra words");
        } catch (Exception e) {
            assertEquals("Invalid format. Use: 'X days Y nights' (e.g., '3 days 2 nights')", e.getMessage());
        }
    }

    @Test
    public void testInvalidFormatWrongSeparator() {
        // Act & Assert
        try {
            TimeValidator.validateTimeFormat("3-days-2-nights");
            fail("Expected Exception for wrong separator");
        } catch (Exception e) {
            assertEquals("Invalid format. Use: 'X days Y nights' (e.g., '3 days 2 nights')", e.getMessage());
        }
    }

    @Test
    public void testEmptyString() {
        // Act & Assert
        try {
            TimeValidator.validateTimeFormat("");
            fail("Expected Exception for empty string");
        } catch (Exception e) {
            assertEquals("Time cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testNullString() {
        // Act & Assert
        try {
            TimeValidator.validateTimeFormat(null);
            fail("Expected Exception for null string");
        } catch (Exception e) {
            assertEquals("Time cannot be empty", e.getMessage());
        }
    }

    // ===== RANGE VALIDATION TESTS (LOGIC) =====
    // Note: The logic check in TimeValidator catches "nights != days - 1" first
    // usually.

    @Test
    public void testInvalidLogicTooManyNights() {
        // Act & Assert - 3 days 3 nights is invalid logic
        try {
            TimeValidator.validateTimeFormat("3 days 3 nights");
            fail("Expected Exception for invalid logic");
        } catch (Exception e) {
            assertEquals("Invalid logic: For 3 days, you should have 2 nights", e.getMessage());
        }
    }

    @Test
    public void testInvalidLogicTooFewNights() {
        // Act & Assert - 5 days 3 nights is invalid logic
        try {
            TimeValidator.validateTimeFormat("5 days 3 nights");
            fail("Expected Exception for invalid logic");
        } catch (Exception e) {
            assertEquals("Invalid logic: For 5 days, you should have 4 nights", e.getMessage());
        }
    }

    // ===== ROBUSTNESS TESTS (TimeValidator is robust now) =====

    @Test
    public void testCaseSensitivity() throws Exception {
        // Act & Assert - Should PASS now due to robust parsing
        TimeValidator.validateTimeFormat("3 DAYS 2 NIGHTS");
    }

    @Test
    public void testMixedCase() throws Exception {
        // Act & Assert - Should PASS now
        TimeValidator.validateTimeFormat("3 Days 2 Nights");
    }

    @Test
    public void testExtraWhitespace() throws Exception {
        // Act & Assert - Should PASS now
        TimeValidator.validateTimeFormat("3  days  2  nights");
    }

    @Test
    public void testLeadingTrailingWhitespace() throws Exception {
        // Act & Assert - Should PASS now
        TimeValidator.validateTimeFormat(" 3 days 2 nights ");
    }
}