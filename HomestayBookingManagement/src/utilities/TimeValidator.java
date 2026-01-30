package utilities;

/**
 * Utility class for time validation.
 */
public class TimeValidator {

    /**
     * Validates and formats the time string (e.g., "3 days 2 nights").
     * 
     * @param time The time string to validate.
     * @return The validated and potentially slightly formatted time string.
     * @throws Exception if the format is invalid or logic (nights != days - 1) is
     *                   incorrect.
     */
    public static String validateTimeFormat(String time) throws Exception {
        if (time == null || time.trim().isEmpty()) {
            throw new Exception("Time cannot be empty");
        }

        // Normalize spaces
        String t = time.trim().replaceAll("\\s+", " ").toLowerCase();

        // Check format
        if (!t.matches("\\d+\\s+days?\\s+\\d+\\s+nights?")) {
            throw new Exception("Invalid format. Use: 'X days Y nights' (e.g., '3 days 2 nights')");
        }

        // Extract days and nights
        String[] parts = t.split("\\s+");
        int days = Integer.parseInt(parts[0]);
        int nights = Integer.parseInt(parts[2]);

        // Validate logic: nights should be days - 1
        if (nights != days - 1) {
            throw new Exception("Invalid logic: For " + days + " days, you should have "
                    + (days - 1) + " nights");
        }

        // Return mostly original format but cleaner? Or just return valid string
        // Returning standard format "X days Y nights" to handle singular/plural cleanly
        return days + (days == 1 ? " day " : " days ") + nights + (nights == 1 ? " night" : " nights");
    }
}
