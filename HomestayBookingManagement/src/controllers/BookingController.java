package controllers;

import middlewares.MiddlewareChain;
import models.Booking;
import services.BookingService;
import services.TourService;
import view.BookingView;

public class BookingController {

    private BookingService bookingService;
    private TourService tourService;
    private BookingView view;

    public BookingController(BookingService bookingService, TourService tourService) {
        this.bookingService = bookingService;
        this.tourService = tourService;
        this.view = new BookingView();
    }

    /**
     * Thêm booking mới với cập nhật trạng thái tour
     */
    void addBooking() {
        view.displayMessage("----- ADD NEW BOOKING -----");
        do {
            MiddlewareChain.perform(() -> {
                String id = view.inputIdForAdd(bid -> bookingService.getById(bid) != null);
                Booking newBooking = view.inputBookingDetails(id,
                        tid -> tourService.getById(tid) != null,
                        tid -> tourService.getById(tid));

                if (bookingService.addBookingWithValidation(newBooking)) {
                    view.displayMessage("Booking added successfully!");
                    bookingService.saveToFile();
                    // Tour status updated in service, save tour file:
                    tourService.saveToFile();
                } else {
                    view.displayError("Failed to add booking (Tour unavailable/Invalid date).");
                }
            });
        } while (view.confirmContinue());
    }

    void removeBooking() {
        view.displayMessage("----- REMOVE BOOKING -----");
        do {
            MiddlewareChain.perform(() -> {
                String id = view.inputIdForAction(bid -> bookingService.getById(bid) != null,
                        "Enter Booking ID to remove: ");

                if (bookingService.removeBookingWithValidation(id)) {
                    view.displayMessage("Booking removed successfully!");
                    bookingService.saveToFile();
                    tourService.saveToFile();
                } else {
                    view.displayError("Failed to remove booking.");
                }
            });
        } while (view.confirmContinue());
    }

    void updateBooking() {
        view.displayMessage("----- UPDATE BOOKING -----");
        do {
            MiddlewareChain.perform(() -> {
                String id = view.inputIdForAction(bid -> bookingService.getById(bid) != null,
                        "Enter Booking ID to update: ");
                Booking oldBooking = bookingService.getById(id);
                if (oldBooking == null) {
                    throw new Exception("This Booking does not exist!");
                }

                Booking newBooking = view.inputUpdateDetails(oldBooking,
                        tid -> tourService.getById(tid) != null,
                        tid -> tourService.getById(tid));

                if (bookingService.updateBookingWithValidation(newBooking, oldBooking)) {
                    view.displayMessage("Booking updated successfully!");
                    bookingService.saveToFile();
                    tourService.saveToFile();
                } else {
                    view.displayError("Failed to update booking (Tour unavailable/Invalid date).");
                }
            });
        } while (view.confirmContinue());
    }

    void searchBooking() {
        view.displayMessage("----- SEARCH BOOKING BY NAME -----");
        MiddlewareChain.perform(() -> {
            String name = utilities.Inputter.getString("Enter customer name: ", "Cannot be empty").trim();

            if (name.isEmpty()) {
                throw new Exception("Name cannot be empty!");
            }

            java.util.List<models.Booking> results = null;
            try {
                results = bookingService.getAll().stream()
                        .filter(b -> b.getFullName().toLowerCase().contains(name.toLowerCase()))
                        .collect(java.util.stream.Collectors.toList());
            } catch (Exception streamException) {
                throw new Exception("Error processing booking data: " + streamException.getMessage());
            }

            if (results == null || results.isEmpty()) {
                view.displayMessage("No booking found with name containing: " + name);
                return;
            }

            view.displayList(results);
        });
    }
}
