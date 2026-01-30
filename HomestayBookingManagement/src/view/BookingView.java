package view;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import models.Booking;
import utilities.Acceptable;
import utilities.Inputter;

public class BookingView extends utilities.GeneralView<models.Booking> {

    public String inputIdForAdd(Predicate<String> isDuplicate) {
        return Inputter.wrapRetry(() -> {
            String id = Inputter.getString("Enter Booking ID (Bxxxxx): ", "Invalid format",
                    Acceptable.BOOKING_ID_VALID);
            if (isDuplicate.test(id)) {
                throw new Exception("Booking ID already exists!");
            }
            return id;
        });
    }

    public Booking inputBookingDetails(String id, Predicate<String> isTourExist,
            java.util.function.Function<String, models.Tour> getTour) {
        String name = Inputter.wrapRetry(() -> Inputter.getString("Enter customer name: ",
                "Invalid name (only letters, spaces, Vietnamese chars)", Acceptable.CUSTOMER_NAME_VALID));

        String tourId = Inputter.wrapRetry(() -> {
            String tid = Inputter.getString("Enter Tour ID: ", "Invalid format", Acceptable.TOUR_ID_VALID);
            if (!isTourExist.test(tid)) {
                throw new Exception("Tour ID not found!");
            }
            return tid;
        });

        LocalDate date = Inputter.wrapRetry(() -> {
            LocalDate bookingDate = Inputter.getLocalDate("Enter booking date: ", "Invalid date");
            if (bookingDate.isBefore(LocalDate.now())) {
                throw new Exception("Booking date cannot be in the past!");
            }

            // Kiểm tra ngày booking phải trước ngày khởi hành tour
            models.Tour tour = getTour.apply(tourId);
            if (tour != null && bookingDate.isAfter(tour.getDepartureDate())) {
                throw new Exception("Booking date must be before tour departure date (" +
                        utilities.Inputter.formatDate(tour.getDepartureDate()) + ")!");
            }

            return bookingDate;
        });

        String phone = Inputter.wrapRetry(() -> Inputter.getString("Enter phone number: ",
                "Invalid phone (must be 10 digits starting with 0)", Acceptable.VN_PHONE_VALID));

        return new Booking(id, name, tourId, date, phone);
    }

    public String inputIdForAction(Predicate<String> isExist, String msg) {
        return Inputter.wrapRetry(() -> {
            String id = Inputter.getString(msg, "Invalid", ".+");
            if (!isExist.test(id)) {
                throw new Exception("Booking ID not found!");
            }
            return id;
        });
    }

    public Booking inputUpdateDetails(Booking oldB, Predicate<String> isTourExist,
            java.util.function.Function<String, models.Tour> getTour) {
        System.out.println("--- UPDATE BOOKING ---");
        String newName = Inputter.wrapRetry(() -> Inputter.getStringForUpdate("New Name", oldB.getFullName(),
                Acceptable.CUSTOMER_NAME_VALID, "Invalid name (only letters, spaces, Vietnamese chars)"));

        String newTourId = Inputter.wrapRetry(() -> {
            String tid = Inputter.getStringForUpdate("New TourID", oldB.getTourID(), Acceptable.TOUR_ID_VALID,
                    "Invalid");
            if (!tid.equals(oldB.getTourID()) && !isTourExist.test(tid)) {
                throw new Exception("New Tour ID not found!");
            }
            return tid;
        });

        LocalDate newDate = Inputter.wrapRetry(() -> {
            LocalDate bookingDate = Inputter.getLocalDateForUpdate("New Date", oldB.getBookingDate(), "Invalid date");
            if (!bookingDate.equals(oldB.getBookingDate()) && bookingDate.isBefore(LocalDate.now())) {
                throw new Exception("New booking date cannot be in the past!");
            }

            // Kiểm tra ngày booking phải trước ngày khởi hành tour
            models.Tour tour = getTour.apply(newTourId);
            if (tour != null && bookingDate.isAfter(tour.getDepartureDate())) {
                throw new Exception("Booking date must be before tour departure date (" +
                        utilities.Inputter.formatDate(tour.getDepartureDate()) + ")!");
            }

            return bookingDate;
        });

        String newPhone = Inputter.wrapRetry(() -> Inputter.getStringForUpdate("New Phone", oldB.getPhone(),
                Acceptable.VN_PHONE_VALID, "Invalid phone (must be 10 digits starting with 0)"));

        return new Booking(oldB.getBookingID(), newName, newTourId, newDate, newPhone);
    }

    @Override
    public void displayList(List<Booking> list) {
        if (list.isEmpty()) {
            System.out.println(">> No booking found.");
            return;
        }
        String border = "-------------------------------------------------------------------------";
        System.out.println(border);
        System.out.printf("| %-10s | %-20s | %-10s | %-12s | %-12s |\n", "BookingID", "Name", "TourID", "Date",
                "Phone");
        System.out.println(border);
        for (Booking b : list) {
            System.out.printf("| %-10s | %-20s | %-10s | %-12s | %-12s |\n",
                    b.getBookingID(),
                    b.getFullName(),
                    b.getTourID(),
                    utilities.Inputter.formatDate(b.getBookingDate()),
                    b.getPhone());
        }
        System.out.println(border);
    }
}
