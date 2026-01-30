package utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 * Utility class for handling user input. All methods now THROW exceptions
 * instead of printing errors. WrapRetry uses ErrorHandler for centralized
 * logging.
 */
public class Inputter {

    public static final Scanner sc = new Scanner(System.in);

    // =========================================================================
    // GENERIC WRAPPER (The Engine)
    // =========================================================================
    public static <T> T wrapRetry(Callable<T> func) {
        while (true) {
            try {
                return func.call();
            } catch (Exception e) {
                // Delegate to ErrorHandler for centralized control
                ErrorHandler.logError(e);
            }
        }
    }

    // =========================================================================
    // 1. INTEGER INPUTS (Throws Exception)
    // =========================================================================
    public static int getAnInteger(String inputMsg, String errorMsg) throws Exception {
        System.out.print(inputMsg);
        String s = sc.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new Exception(errorMsg);
        }
    }

    public static int getAnInteger(String inputMsg, String errorMsg, int min, int max) throws Exception {
        if (min > max) {
            int t = min;
            min = max;
            max = t;
        }

        System.out.print(inputMsg);
        String s = sc.nextLine().trim();

        try {
            int n = Integer.parseInt(s);
            if (n < min || n > max) {
                throw new Exception(errorMsg);
            }
            return n;
        } catch (NumberFormatException e) {
            throw new Exception("Invalid input. Must be a number.");
        }
    }

    // =========================================================================
    // 2. DOUBLE INPUTS (Throws Exception)
    // =========================================================================
    public static double getADouble(String inputMsg, String errorMsg) throws Exception {
        System.out.print(inputMsg);
        try {
            return Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new Exception(errorMsg);
        }
    }

    public static double getADouble(String inputMsg, String errorMsg, double min, double max) throws Exception {
        if (min > max) {
            double t = min;
            min = max;
            max = t;
        }

        System.out.print(inputMsg);
        try {
            double n = Double.parseDouble(sc.nextLine().trim());
            if (n < min || n > max) {
                throw new Exception(errorMsg);
            }
            return n;
        } catch (NumberFormatException e) {
            throw new Exception("Invalid input. Must be a number.");
        }
    }

    // =========================================================================
    // 3. STRING INPUTS (Throws Exception)
    // =========================================================================
    public static String getString(String inputMsg, String errorMsg) throws Exception {
        System.out.print(inputMsg);
        String str = sc.nextLine().trim();
        if (str.isEmpty()) {
            throw new Exception(errorMsg);
        }
        return str;
    }

    public static String getString(String inputMsg, String errorMsg, String regex) throws Exception {
        System.out.print(inputMsg);
        String str = sc.nextLine().trim();

        if (str.isEmpty()) {
            throw new Exception("Input cannot be empty.");
        }

        if (!str.matches(regex)) {
            throw new Exception(errorMsg);
        }
        return str;
    }

    // =========================================================================
    // 4. DATE/TIME INPUTS (Java 8 Time API)
    // =========================================================================
    public static LocalDate getLocalDate(String inputMsg, String errorMsg) throws Exception {
        return getLocalDate(inputMsg, errorMsg, AppConstants.DATE_TIME_PATTERN);
    }

    public static LocalDate getLocalDate(String inputMsg, String errorMsg, String pattern) throws Exception {
        System.out.print(inputMsg);
        String dateStr = sc.nextLine().trim();
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            throw new Exception(errorMsg + " (Format: " + pattern + ")");
        }
    }

    public static LocalDateTime getLocalDateTime(String inputMsg, String errorMsg, DateTimeFormatter formatter)
            throws Exception {
        System.out.print(inputMsg);
        String dateStr = sc.nextLine().trim();
        try {
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new Exception(errorMsg);
        }
    }

    // =========================================================================
    // 5. UPDATE METHODS (Enter to keep old value)
    // =========================================================================
    public static String getStringForUpdate(String inputMsg, String oldValue, String regex, String errorMsg)
            throws Exception {
        System.out.print(inputMsg + " (Old: " + oldValue + "): ");
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            return oldValue;
        }

        if (!input.matches(regex)) {
            throw new Exception(errorMsg);
        }

        return input;
    }

    public static int getIntForUpdate(String inputMsg, int oldValue, int min, int max, String errorMsg)
            throws Exception {
        System.out.print(inputMsg + " (Old: " + oldValue + "): ");
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            return oldValue;
        }

        try {
            int n = Integer.parseInt(input);
            if (n < min || n > max) {
                throw new Exception(errorMsg);
            }
            return n;
        } catch (NumberFormatException e) {
            throw new Exception("Invalid number format.");
        }
    }

    public static double getDoubleForUpdate(String inputMsg, double oldValue, double min, double max, String errorMsg)
            throws Exception {
        System.out.print(inputMsg + " (Old: " + oldValue + "): ");
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            return oldValue;
        }

        try {
            double n = Double.parseDouble(input);
            if (n < min || n > max) {
                throw new Exception(errorMsg);
            }
            return n;
        } catch (NumberFormatException e) {
            throw new Exception("Invalid number format.");
        }
    }

    public static LocalDate getLocalDateForUpdate(String inputMsg, LocalDate oldDate, String errorMsg)
            throws Exception {
        System.out.print(inputMsg + " (Old: " + oldDate.format(AppConstants.DATE_FMT) + "): ");
        String dateStr = sc.nextLine().trim();
        if (dateStr.isEmpty()) {
            return oldDate;
        }
        try {
            return LocalDate.parse(dateStr, AppConstants.DATE_FMT);
        } catch (DateTimeParseException e) {
            throw new Exception(errorMsg);
        }
    }

    public static LocalDateTime getLocalDateTimeForUpdate(String inputMsg, LocalDateTime oldDate,
            DateTimeFormatter formatter, String errorMsg) throws Exception {
        System.out.print(inputMsg + " (Old: " + oldDate.format(formatter) + "): ");
        String dateStr = sc.nextLine().trim();
        if (dateStr.isEmpty()) {
            return oldDate;
        }
        try {
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new Exception(errorMsg);
        }
    }

    // =========================================================================
    // 6. DATE FORMATTING UTILS
    // =========================================================================
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "N/A";
        }
        return date.format(AppConstants.DATE_FMT);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public static boolean getYesNo(String string) {
        String s = wrapRetry(() -> getString(string, "Please enter Y or N!", Acceptable.YES_NO_VALID));
        return s.equalsIgnoreCase("Y");
    }
}
