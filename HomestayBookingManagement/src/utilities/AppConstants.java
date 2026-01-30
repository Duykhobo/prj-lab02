package utilities;

import java.time.format.DateTimeFormatter;

public class AppConstants {
    // File Paths (CSV/Text Persistance)
    public static final String FILE_HOMESTAYS = "Homestays2.txt";
    public static final String FILE_BOOKINGS = "Bookings.txt";
    public static final String FILE_TOURS = "Tours3.txt"; // Inferred/Checked from usage if needed, or keeping safe
                                                          // default

    // Date Formats
    public static final String DATE_TIME_PATTERN = "dd/MM/yyyy"; // Standard project requirement typically
    public static final String DATE_TIME_FULL_PATTERN = "dd/MM/yyyy HH:mm";

    public static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern(DATE_TIME_FULL_PATTERN);

    // Regex Patterns
    public static final String REGEX_BOOKING_ID = "^B\\d{5}$";
    public static final String REGEX_TOUR_ID = "^T\\d{5}$";
    public static final String REGEX_PHONE = "^0\\d{9}$";
    public static final String REGEX_NAME = "^[a-zA-Z ]+$"; // Simple name validation
}
