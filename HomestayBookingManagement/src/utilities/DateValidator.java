package utilities;

import java.time.LocalDate;

/**
 * Utility class for date validation.
 */
public class DateValidator {

    /**
     * Validates a date string against the application format and business rules.
     * 
     * @param dateStr The date string to validate.
     * @return The parsed LocalDate.
     * @throws Exception If format is invalid or date violates business rules.
     */
    public static LocalDate validateDate(String dateStr) throws Exception {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new Exception("Invalid date format. Use dd/MM/yyyy");
        }
        try {
            LocalDate date = LocalDate.parse(dateStr, AppConstants.DATE_FMT);
            validateDate(date);
            return date;
        } catch (java.time.format.DateTimeParseException e) {
            throw new Exception("Invalid date format. Use dd/MM/yyyy");
        }
    }

    /**
     * Validates a LocalDate against business rules (not in past, not too far
     * future).
     * 
     * @param date The date to validate.
     * @throws Exception If date violates business rules.
     */
    public static void validateDate(LocalDate date) throws Exception {
        if (date.isBefore(LocalDate.now())) {
            throw new Exception("Date cannot be in the past"); // Matches test expectation "Date cannot be in the past"
        }
        if (date.isAfter(LocalDate.now().plusYears(5))) {
            throw new Exception("Date cannot be more than 5 years in the future");
        }
    }

    /**
     * Validates that an end date is valid relative to a departure date.
     * 
     * @param endDate       The tour end date.
     * @param departureDate The tour departure date.
     * @throws Exception If business rules are violated (end < start, end == start,
     *                   duration > 30 days).
     */
    public static void validateEndDate(LocalDate endDate, LocalDate departureDate) throws Exception {
        if (endDate.isBefore(departureDate)) {
            throw new Exception("End Date must be after Departure Date");
        }
        if (endDate.isEqual(departureDate)) {
            throw new Exception("End Date cannot be same as Departure Date");
        }
        if (java.time.temporal.ChronoUnit.DAYS.between(departureDate, endDate) > 30) {
            throw new Exception("Tour duration cannot exceed 30 days");
        }
    }
}
