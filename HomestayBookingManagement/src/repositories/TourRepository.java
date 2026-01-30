package repositories;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import models.Tour;
import repositories.interfaces.ITourRepository;
import utilities.AppConstants; // [UPDATE]
import utilities.TextFileHandler;

/**
 * Tour Repository - Handles CRUD + Business Queries
 */
public class TourRepository extends TextFileHandler<Tour> implements ITourRepository {

    private final List<Tour> tours = new ArrayList<>();
    // [UPDATE] Use AppConstants
    private final String FILE_NAME = AppConstants.FILE_TOURS;

    @Override
    public Tour parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        try {
            String[] parts = line.split(",");
            if (parts.length < 9) {
                System.err.println(">> Invalid format - Expected 9 fields, got " + parts.length + ": " + line);
                return null;
            }

            String tourId = parts[0].trim();
            if (tourId.isEmpty() || tourId.equalsIgnoreCase("TourID")) {
                return null; // Skip header or empty ID
            }

            String tourName = parts[1].trim();
            String time = parts[2].trim();
            double price = Double.parseDouble(parts[3].trim());
            String homeID = parts[4].trim();

            // [UPDATE] Use AppConstants.DATE_FMT
            LocalDate departureDate = LocalDate.parse(parts[5].trim(), AppConstants.DATE_FMT);
            LocalDate endDate = LocalDate.parse(parts[6].trim(), AppConstants.DATE_FMT);

            int numberTourist = Integer.parseInt(parts[7].trim());
            boolean isBooked = Boolean.parseBoolean(parts[8].trim());

            return new Tour(tourId, tourName, time, price, homeID, departureDate, endDate, numberTourist, isBooked);

        } catch (DateTimeParseException e) {
            String sanitizedLine = line.replaceAll("[\r\n\t]", "_");
            System.err.println(">> Date parsing failed (expected " + AppConstants.DATE_TIME_PATTERN + "): "
                    + sanitizedLine + " - " + e.getMessage());
        } catch (NumberFormatException e) {
            String sanitizedLine = line.replaceAll("[\r\n\t]", "_");
            System.err.println(">> Number parsing failed: " + sanitizedLine + " - " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            String sanitizedLine = line.replaceAll("[\r\n\t]", "_");
            System.err.println(">> Missing required fields: " + sanitizedLine);
        } catch (Exception e) {
            String sanitizedLine = line.replaceAll("[\r\n\t]", "_");
            System.err.println(">> Unexpected error parsing line: " + sanitizedLine + " - " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Tour> findAll() {
        return new ArrayList<>(tours);
    }

    @Override
    public Tour findById(String id) {
        return tours.stream()
                .filter(t -> t.getTourId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean save(Tour entity) {
        if (exists(entity.getTourId())) {
            return false;
        }
        return tours.add(entity);
    }

    @Override
    public boolean update(Tour entity) {
        Tour existing = findById(entity.getTourId());
        if (existing == null) {
            return false;
        }
        int index = tours.indexOf(existing);
        tours.set(index, entity);
        return true;
    }

    @Override
    public boolean delete(String id) {
        return tours.removeIf(t -> t.getTourId().equalsIgnoreCase(id));
    }

    @Override
    public boolean exists(String id) {
        return findById(id) != null;
    }

    // ===== BUSINESS QUERIES =====

    @Override
    public List<Tour> findByHomestayId(String homeId) {
        return tours.stream()
                .filter(t -> t.getHomeID().equalsIgnoreCase(homeId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tour> findByDateAfter(LocalDate date) {
        return tours.stream()
                .filter(t -> t.getDepartureDate().isAfter(date))
                .sorted(Comparator.comparing(Tour::getTotalAmount).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Tour> findByDateBefore(LocalDate date) {
        return tours.stream()
                .filter(t -> t.getDepartureDate().isBefore(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Tour> findByBooked(boolean isBooked) {
        return tours.stream()
                .filter(t -> t.isBooked() == isBooked)
                .collect(Collectors.toList());
    }

    /**
     * Business Method: Check if tour conflicts with existing tours
     */
    public boolean hasTimeConflict(Tour newTour) {
        return tours.stream()
                .anyMatch(existing -> existing.isOverlapWith(newTour));
    }

    /**
     * Business Method: Get upcoming tours sorted by revenue (desc)
     */
    public List<Tour> getUpcomingToursByRevenue() {
        return tours.stream()
                .filter(Tour::isUpcoming)
                .sorted(Comparator.comparing(Tour::getTotalAmount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Business Method: Get expired tours
     */
    public List<Tour> getExpiredTours() {
        return tours.stream()
                .filter(Tour::isExpired)
                .collect(Collectors.toList());
    }

    @Override
    public void loadFromFile() {
        super.load(tours, FILE_NAME);
    }

    @Override
    public void saveToFile() {
        super.save(tours, FILE_NAME);
    }
}