/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package utilities;

public interface Acceptable {

    // Common ID patterns
    String ID_PREFIX_PATTERN = "^[A-Z]{1,3}\\d{4,6}$";

    // Common field validations
    String NAME_VALID = "^.{2,50}$";
    String PERSON_NAME_VALID = "^[a-zA-ZÀ-ỹ\\s]{2,50}$";
    String PHONE_VALID = "^\\d{10}$";
    String VN_PHONE_VALID = "^0\\d{9}$";
    String EMAIL_VALID = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    // Project specific IDs
    String BOOKING_ID_VALID = "^B\\d{5}$";
    String TOUR_ID_VALID = "^T\\d{5}$";
    String HOME_ID_VALID = "^HS\\d{4,6}$"; // Assuming HS + digits
    String CUSTOMER_NAME_VALID = "^[a-zA-ZÀ-ỹ\\s]{2,50}$";

    // Date patterns
    String DATE_VALID = "^\\d{2}/\\d{2}/\\d{4}$";
    String DATE_FORMAT = "dd/MM/yyyy";

    // Number patterns
    String INTEGER_VALID = "^-?\\d+$";
    String POSITIVE_INT_VALID = "^[1-9]\\d*$";
    String DOUBLE_VALID = "^-?\\d+(\\.\\d+)?$";
    String POSITIVE_DOUBLE_VALID = "^[0-9]+(\\.[0-9]+)?$";

    // Common choices
    String YES_NO_VALID = "^[YyNn]$";

    static boolean isValid(String data, String pattern) {
        return data != null && data.matches(pattern);
    }

    static String createIdPattern(String prefix, int digitCount) {
        return "^" + prefix + "\\\\d{" + digitCount + "}$";
    }
}
