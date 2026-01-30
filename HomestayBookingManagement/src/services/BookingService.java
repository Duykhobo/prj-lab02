package services;

import java.util.List;

import models.Booking;
import models.Tour;
import repositories.interfaces.IBookingRepository;
import repositories.interfaces.ITourRepository;
import services.interfaces.IService;
import utilities.ErrorHandler;

public class BookingService implements IService<Booking> {

    private final IBookingRepository bookingRepository;
    private final ITourRepository tourRepository;

    // Dependency Injection via Constructor
    public BookingService(IBookingRepository bookingRepository, ITourRepository tourRepository) {
        this.bookingRepository = bookingRepository;
        this.tourRepository = tourRepository;
    }

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getById(String id) {
        return bookingRepository.findById(id);
    }

    @Override
    public void add(Booking item) {
        bookingRepository.save(item);
    }

    @Override
    public void update(Booking item) {
        bookingRepository.update(item);
    }

    // ===== BUSINESS METHODS =====
    /**
     * Add booking with full business validation
     */
    public synchronized boolean addBookingWithValidation(Booking booking) {
        try {
            // 1. Check tour exists
            Tour tour = tourRepository.findById(booking.getTourID());
            if (tour == null) {
                // Log warning or return false
                return false;
            }

            // 2. Check tour is available (not booked)
            if (tour.isBooked()) {
                ErrorHandler.logError(new Exception("Tour " + tour.getTourId() + " is already booked."));
                return false;
            }

            // 3. Check booking date is before departure date
            if (!booking.getBookingDate().isBefore(tour.getDepartureDate())) {
                ErrorHandler.logError(new Exception("Booking date must be before tour departure."));
                return false;
            }

            // 4. Save booking
            if (!bookingRepository.save(booking)) {
                return false; // Duplicate booking ID
            }

            // 5. Update tour status to booked
            tour.setIsBooked(true);
            tourRepository.update(tour);

            return true;
        } catch (Exception e) {
            ErrorHandler.logError(e); // Centralized Error Logic
            return false;
        }
    }

    /**
     * Remove booking and update tour status
     */
    public synchronized boolean removeBookingWithValidation(String bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId);
            if (booking == null) {
                return false;
            }

            // Update tour status back to available
            Tour tour = tourRepository.findById(booking.getTourID());
            if (tour != null) {
                tour.setIsBooked(false);
                tourRepository.update(tour);
            }

            return bookingRepository.delete(bookingId);
        } catch (Exception e) {
            ErrorHandler.logError(e); // Centralized Error Logic
            return false;
        }
    }

    /**
     * Update booking with business validation
     */
    public synchronized boolean updateBookingWithValidation(Booking newBooking, Booking oldBooking) {
        try {
            // If tour changed, handle availability logic
            if (!oldBooking.getTourID().equals(newBooking.getTourID())) {
                // 1. Release old tour
                Tour oldTour = tourRepository.findById(oldBooking.getTourID());
                if (oldTour != null) {
                    oldTour.setIsBooked(false);
                    tourRepository.update(oldTour);
                }

                // 2. Lock new tour
                Tour newTour = tourRepository.findById(newBooking.getTourID());
                if (newTour != null) {
                    if (newTour.isBooked()) {
                        ErrorHandler.logError(new Exception("New tour " + newTour.getTourId() + " is already booked!"));
                        // Revert old tour? Strictly yes, but here we fail.
                        // Ideally transaction-like, but simple revert:
                        if (oldTour != null) {
                            oldTour.setIsBooked(true);
                            tourRepository.update(oldTour);
                        }
                        return false;
                    }
                    newTour.setIsBooked(true);
                    tourRepository.update(newTour);
                }
            }

            return bookingRepository.update(newBooking);
        } catch (Exception e) {
            ErrorHandler.logError(e);
            return false;
        }
    }

    /**
     * Find bookings by customer name (partial match)
     */
    public List<Booking> findByCustomerName(String name) {
        return bookingRepository.findByCustomerName(name);
    }

    public void loadFromFile() {
        bookingRepository.loadFromFile();
    }

    public void saveToFile() {
        bookingRepository.saveToFile();
    }
}
