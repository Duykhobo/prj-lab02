package services;

import java.time.LocalDate;
import java.util.List;
import models.Homestay;
import models.Tour;
import repositories.interfaces.IHomestayRepository;
import repositories.interfaces.ITourRepository;
import services.interfaces.IService;
import utilities.ErrorHandler;

/**
 * Tour Service Layer - Business Logic for Tour Management
 *
 * Responsibilities: - Tour CRUD operations with business validation - Homestay
 * capacity constraint checking - Tour overlap detection and prevention -
 * Statistics calculation - File I/O coordination
 *
 * Design Patterns Used: - Service Layer Pattern: Encapsulates business logic -
 * Dependency Injection: Receives repositories via constructor
 *
 * @author ThanhDuy
 */
public class TourService implements IService<Tour> {

    // ===== DEPENDENCIES =====
    private final ITourRepository tourRepository; // Data access for tours
    private final IHomestayRepository homestayRepository; // Data access for homestays

    /**
     * Constructor with Dependency Injection
     *
     * @param tourRepository     Tour data access layer
     * @param homestayRepository Homestay data access layer
     */
    public TourService(ITourRepository tourRepository, IHomestayRepository homestayRepository) {
        this.tourRepository = tourRepository;
        this.homestayRepository = homestayRepository;
    }

    // ===== BASIC CRUD OPERATIONS =====
    /**
     * Get all tours from repository
     *
     * @return List of all tours
     */
    @Override
    public List<Tour> getAll() {
        return tourRepository.findAll();
    }

    /**
     * Find tour by ID
     *
     * @param id Tour ID to search for
     * @return Tour object or null if not found
     */
    @Override
    public Tour getById(String id) {
        return tourRepository.findById(id);
    }

    /**
     * Add new tour (basic operation without validation) For validated add, use
     * addTourWithValidation()
     *
     * @param item Tour to add
     */
    @Override
    public void add(Tour item) {
        tourRepository.save(item);
    }

    /**
     * Update existing tour (basic operation without validation)
     *
     * @param newItem Updated tour data
     */
    @Override
    public void update(Tour newItem) {
        tourRepository.update(newItem);
    }
    // ===== BUSINESS VALIDATION METHODS =====

    /**
     * Add tour with comprehensive business validation
     *
     * Validation Rules: 1. Homestay must exist 2. Tour capacity cannot exceed
     * homestay capacity 3. Tour dates must be valid (departure < end) 4. No
     * time conflicts with existing tours
     *
     * @param tour Tour to add
     * @return true if tour added successfully, false if validation failed
     */
    public boolean addTourWithValidation(Tour tour) {
        // 1. Check homestay exists
        Homestay homestay = homestayRepository.findById(tour.getHomeID());
        if (homestay == null) {
            return false; // Invalid homeID
        }

        // 2. Check individual tour capacity constraint
        if (tour.getNumberTourist() > homestay.getMaximumCapacity()) {
            return false; // Exceed homestay capacity
        }

        // 3. Check date validity
        if (!tour.hasValidDates()) {
            return false; // Invalid dates (departure >= end)
        }

        // 4. Check time conflict with existing tours
        try {
            if (tourRepository instanceof repositories.TourRepository) {
                if (((repositories.TourRepository) tourRepository).hasTimeConflict(tour)) {
                    return false; // Time overlap detected
                }
            }
        } catch (ClassCastException e) {
            ErrorHandler.logError(
                    new Exception("Warning: Cannot check time conflict - repository type mismatch: " + e.getMessage()));
        }

        return tourRepository.save(tour);
    }

    /**
     * Update tour with business validation
     */
    public boolean updateTourWithValidation(Tour newTour, Tour oldTour) {
        // 1. Homestay check (if changed or just safety)
        Homestay homestay = homestayRepository.findById(newTour.getHomeID());
        if (homestay != null && newTour.getNumberTourist() > homestay.getMaximumCapacity()) {
            utilities.ErrorHandler.logError(new Exception(
                    "Number of tourists exceeds homestay capacity (" + homestay.getMaximumCapacity() + ")!"));
            return false;
        }

        // 2. Date/Overlap check
        // Check for time overlap if dates changed
        if (!newTour.getDepartureDate().equals(oldTour.getDepartureDate())
                || !newTour.getEndDate().equals(oldTour.getEndDate())) {

            // Check against ALL other tours
            List<Tour> existingTours = tourRepository.findAll();
            for (Tour existingTour : existingTours) {
                // Skip self
                if (existingTour.getTourId().equalsIgnoreCase(newTour.getTourId())) {
                    continue;
                }

                // Check overlap
                if (existingTour.getHomeID().equals(newTour.getHomeID())
                        && newTour.isOverlapWith(existingTour)) {
                    utilities.ErrorHandler.logError(
                            new Exception("Updated tour dates overlap with existing tour " + existingTour.getTourId()));
                    return false;
                }
            }
        }

        return tourRepository.update(newTour);
    }

    // ===== FILE I/O OPERATIONS =====
    /**
     * Load tour data from file Called during application startup
     */
    public void loadFromFile() {
        tourRepository.loadFromFile();
    }

    /**
     * Save tour data to file Called when data changes or application exits
     */
    public void saveToFile() {
        tourRepository.saveToFile();
    }

    // ===== BUSINESS QUERY METHODS =====
    /**
     * Get tours with departure date after today Business Rule: Future tours
     * sorted by total amount (descending)
     *
     * @return List of upcoming tours
     */
    public List<Tour> getLaterThanToday() {
        return tourRepository.findByDateAfter(LocalDate.now());
    }

    /**
     * Get tours with departure date before today Business Rule: Past/expired
     * tours
     *
     * @return List of expired tours
     */
    public List<Tour> getEarlierThanToday() {
        return tourRepository.findByDateBefore(LocalDate.now());
    }

    /**
     * Get upcoming tours sorted by revenue (business requirement) Used for menu
     * option 4: "List Tours (Departure > Today) [Sorted by Amount]"
     *
     * @return List of upcoming tours sorted by total amount descending
     */
    public List<Tour> getUpcomingToursByRevenue() {
        try {
            if (tourRepository instanceof repositories.TourRepository) {
                return ((repositories.TourRepository) tourRepository).getUpcomingToursByRevenue();
            }
        } catch (ClassCastException e) {
            ErrorHandler.logError(new Exception(
                    "Warning: Cannot get upcoming tours by revenue - using fallback method: " + e.getMessage()));
        }
        return getLaterThanToday(); // Fallback to basic method
    }

    /**
     * Get expired tours (business requirement) Used for menu option 3: "List
     * Tours (Departure < Today)"
     *
     * @return List of expired tours
     */
    public List<Tour> getExpiredTours() {
        try {
            if (tourRepository instanceof repositories.TourRepository) {
                return ((repositories.TourRepository) tourRepository).getExpiredTours();
            }
        } catch (ClassCastException e) {
            ErrorHandler.logError(
                    new Exception("Warning: Cannot get expired tours - using fallback method: " + e.getMessage()));
        }
        return getEarlierThanToday(); // Fallback to basic method
    }

    /**
     * Generate statistics: Total tourists per homestay Business Rule: Only
     * count booked tours
     *
     * Used for menu option 9: "Statistics (Tourists per Homestay)"
     *
     * @return 2D array [homestayName, totalTourists]
     */
    public Object[][] getStatistics() {
        List<Homestay> homestays = homestayRepository.findAll();
        Object[][] statistics = new Object[homestays.size()][2];

        for (int i = 0; i < homestays.size(); i++) {
            String homestayId = homestays.get(i).getHomeID();
            String homestayName = homestays.get(i).getHomeName();

            // Calculate total tourists for BOOKED tours only
            // Business Rule: Only booked tours contribute to statistics
            int totalTourists = tourRepository.findByHomestayId(homestayId)
                    .stream()
                    .filter(Tour::isBooked) // Only booked tours
                    .mapToInt(Tour::getNumberTourist)
                    .sum();

            statistics[i][0] = homestayName; // Column 0: Homestay name
            statistics[i][1] = totalTourists; // Column 1: Total tourists
        }

        return statistics;
    }
}
