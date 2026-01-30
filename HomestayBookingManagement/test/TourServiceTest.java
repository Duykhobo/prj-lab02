

import org.junit.Test;

import models.Homestay;
import models.Tour;
import repositories.interfaces.IHomestayRepository;
import repositories.interfaces.ITourRepository;
import services.TourService;

import org.junit.Before;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit Tests for TourService Class
 * Tests complex business logic with mock repositories
 */
public class TourServiceTest {
    
    private TourService tourService;
    private MockTourRepository mockTourRepo;
    private MockHomestayRepository mockHomestayRepo;
    private LocalDate futureDate;
    private LocalDate pastDate;
    private LocalDate today;
    
    @Before
    public void setUp() {
        mockTourRepo = new MockTourRepository();
        mockHomestayRepo = new MockHomestayRepository();
        tourService = new TourService(mockTourRepo, mockHomestayRepo);
        
        today = LocalDate.now();
        futureDate = today.plusDays(10);
        pastDate = today.minusDays(5);
    }
    
    // ===== BASIC CRUD TESTS =====
    
    @Test
    public void testGetAll() {
        // Arrange
        List<Tour> expectedTours = new ArrayList<>();
        expectedTours.add(new Tour("T00001", "Test Tour", "3 days 2 nights", 
                                 1500.0, "HS0001", futureDate, futureDate.plusDays(2), 4, false));
        mockTourRepo.setTours(expectedTours);
        
        // Act
        List<Tour> result = tourService.getAll();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("T00001", result.get(0).getTourId());
    }
    
    @Test
    public void testGetById() {
        // Arrange
        Tour expectedTour = new Tour("T00001", "Test Tour", "3 days 2 nights", 
                                   1500.0, "HS0001", futureDate, futureDate.plusDays(2), 4, false);
        mockTourRepo.addTour(expectedTour);
        
        // Act
        Tour result = tourService.getById("T00001");
        
        // Assert
        assertNotNull(result);
        assertEquals("T00001", result.getTourId());
        assertEquals("Test Tour", result.getTourName());
    }
    
    @Test
    public void testGetByIdNotFound() {
        // Act
        Tour result = tourService.getById("NOTFOUND");
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testAdd() {
        // Arrange
        Tour tour = new Tour("T00001", "Test Tour", "3 days 2 nights", 
                           1500.0, "HS0001", futureDate, futureDate.plusDays(2), 4, false);
        
        // Act
        tourService.add(tour);
        
        // Assert
        assertTrue(mockTourRepo.saveWasCalled);
        assertEquals(tour, mockTourRepo.lastSavedTour);
    }
    
    @Test
    public void testUpdate() {
        // Arrange
        Tour tour = new Tour("T00001", "Updated Tour", "3 days 2 nights", 
                           1500.0, "HS0001", futureDate, futureDate.plusDays(2), 4, false);
        
        // Act
        tourService.update(tour);
        
        // Assert
        assertTrue(mockTourRepo.updateWasCalled);
        assertEquals(tour, mockTourRepo.lastUpdatedTour);
    }
    
    // ===== BUSINESS VALIDATION TESTS =====
    
    @Test
    public void testAddTourWithValidationSuccess() {
        // Arrange
        Homestay validHomestay = new Homestay("HS0001", "Test Homestay", 3, "Test Address", 10);
        mockHomestayRepo.addHomestay(validHomestay);
        
        Tour validTour = new Tour("T00001", "Test Tour", "3 days 2 nights", 
                                1500.0, "HS0001", futureDate, futureDate.plusDays(2), 8, false);
        mockTourRepo.setSaveResult(true);
        
        // Act
        boolean result = tourService.addTourWithValidation(validTour);
        
        // Assert
        assertTrue(result);
        assertTrue(mockTourRepo.saveWasCalled);
    }
    
    @Test
    public void testAddTourWithValidationHomestayNotFound() {
        // Arrange
        Tour tour = new Tour("T00001", "Test Tour", "3 days 2 nights", 
                           1500.0, "NOTFOUND", futureDate, futureDate.plusDays(2), 4, false);
        
        // Act
        boolean result = tourService.addTourWithValidation(tour);
        
        // Assert
        assertFalse(result);
        assertFalse(mockTourRepo.saveWasCalled);
    }
    
    @Test
    public void testAddTourWithValidationExceedsHomestayCapacity() {
        // Arrange
        Homestay smallHomestay = new Homestay("HS0001", "Small Homestay", 2, "Test Address", 5);
        mockHomestayRepo.addHomestay(smallHomestay);
        
        Tour largeTour = new Tour("T00001", "Large Tour", "3 days 2 nights", 
                                1500.0, "HS0001", futureDate, futureDate.plusDays(2), 10, false); // 10 > 5
        
        // Act
        boolean result = tourService.addTourWithValidation(largeTour);
        
        // Assert
        assertFalse(result);
        assertFalse(mockTourRepo.saveWasCalled);
    }
    
    @Test
    public void testAddTourWithValidationInvalidDates() {
        // Arrange
        Homestay validHomestay = new Homestay("HS0001", "Test Homestay", 3, "Test Address", 10);
        mockHomestayRepo.addHomestay(validHomestay);
        
        // Invalid tour: end date before departure date
        Tour invalidTour = new Tour("T00001", "Invalid Tour", "3 days 2 nights", 
                                  1500.0, "HS0001", futureDate, futureDate.minusDays(1), 4, false);
        
        // Act
        boolean result = tourService.addTourWithValidation(invalidTour);
        
        // Assert
        assertFalse(result);
        assertFalse(mockTourRepo.saveWasCalled);
    }
    
    @Test
    public void testAddTourWithValidationSameDepartureAndEndDate() {
        // Arrange
        Homestay validHomestay = new Homestay("HS0001", "Test Homestay", 3, "Test Address", 10);
        mockHomestayRepo.addHomestay(validHomestay);
        
        // Invalid tour: same departure and end date
        Tour invalidTour = new Tour("T00001", "Invalid Tour", "1 day 0 nights", 
                                  1500.0, "HS0001", futureDate, futureDate, 4, false);
        
        // Act
        boolean result = tourService.addTourWithValidation(invalidTour);
        
        // Assert
        assertFalse(result);
        assertFalse(mockTourRepo.saveWasCalled);
    }
    
    @Test
    public void testAddTourWithValidationBoundaryCapacity() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Test Homestay", 3, "Test Address", 8);
        mockHomestayRepo.addHomestay(homestay);
        
        Tour boundaryTour = new Tour("T00001", "Boundary Tour", "3 days 2 nights", 
                                   1500.0, "HS0001", futureDate, futureDate.plusDays(2), 8, false); // Exactly 8
        mockTourRepo.setSaveResult(true);
        
        // Act
        boolean result = tourService.addTourWithValidation(boundaryTour);
        
        // Assert
        assertTrue(result); // Should succeed with exact capacity match
        assertTrue(mockTourRepo.saveWasCalled);
    }
    
    // ===== DATE QUERY TESTS =====
    
    @Test
    public void testGetLaterThanToday() {
        // Arrange
        List<Tour> futureTours = new ArrayList<>();
        futureTours.add(new Tour("T00001", "Future Tour 1", "3 days 2 nights", 
                               1500.0, "HS0001", futureDate, futureDate.plusDays(2), 4, false));
        futureTours.add(new Tour("T00002", "Future Tour 2", "2 days 1 night", 
                               1000.0, "HS0001", futureDate.plusDays(5), futureDate.plusDays(6), 2, false));
        mockTourRepo.setFutureTours(futureTours);
        
        // Act
        List<Tour> result = tourService.getLaterThanToday();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(mockTourRepo.findByDateAfterWasCalled);
    }
    
    @Test
    public void testGetEarlierThanToday() {
        // Arrange
        List<Tour> pastTours = new ArrayList<>();
        pastTours.add(new Tour("T00003", "Past Tour", "3 days 2 nights", 
                             1500.0, "HS0001", pastDate, pastDate.plusDays(2), 4, false));
        mockTourRepo.setPastTours(pastTours);
        
        // Act
        List<Tour> result = tourService.getEarlierThanToday();
        
        // Assert
        assertEquals(1, result.size());
        assertTrue(mockTourRepo.findByDateBeforeWasCalled);
    }
    
    @Test
    public void testGetUpcomingToursByRevenue() {
        // Arrange
        List<Tour> upcomingTours = new ArrayList<>();
        upcomingTours.add(new Tour("T00001", "High Revenue Tour", "3 days 2 nights", 
                                 2000.0, "HS0001", futureDate, futureDate.plusDays(2), 5, false)); // 10000 total
        upcomingTours.add(new Tour("T00002", "Low Revenue Tour", "2 days 1 night", 
                                 1000.0, "HS0001", futureDate.plusDays(5), futureDate.plusDays(6), 2, false)); // 2000 total
        mockTourRepo.setUpcomingToursByRevenue(upcomingTours);
        
        // Act
        List<Tour> result = tourService.getUpcomingToursByRevenue();
        
        // Assert
        assertEquals(2, result.size());
        // Should be sorted by revenue (high to low)
        assertTrue(result.get(0).getTotalAmount() >= result.get(1).getTotalAmount());
    }
    
    @Test
    public void testGetExpiredTours() {
        // Arrange
        List<Tour> expiredTours = new ArrayList<>();
        expiredTours.add(new Tour("T00003", "Expired Tour", "3 days 2 nights", 
                                1500.0, "HS0001", pastDate, pastDate.plusDays(2), 4, false));
        mockTourRepo.setExpiredTours(expiredTours);
        
        // Act
        List<Tour> result = tourService.getExpiredTours();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("T00003", result.get(0).getTourId());
    }
    
    // ===== STATISTICS TESTS =====
    
    @Test
    public void testGetStatistics() {
        // Arrange
        List<Homestay> homestays = new ArrayList<>();
        homestays.add(new Homestay("HS0001", "Homestay 1", 3, "Address 1", 10));
        homestays.add(new Homestay("HS0002", "Homestay 2", 5, "Address 2", 15));
        mockHomestayRepo.setHomestays(homestays);
        
        List<Tour> homestay1Tours = new ArrayList<>();
        homestay1Tours.add(new Tour("T00001", "Tour 1", "3 days 2 nights", 
                                  1500.0, "HS0001", futureDate, futureDate.plusDays(2), 4, true)); // Booked
        homestay1Tours.add(new Tour("T00002", "Tour 2", "2 days 1 night", 
                                  1000.0, "HS0001", futureDate.plusDays(5), futureDate.plusDays(6), 2, false)); // Not booked
        
        List<Tour> homestay2Tours = new ArrayList<>();
        homestay2Tours.add(new Tour("T00003", "Tour 3", "4 days 3 nights", 
                                  2000.0, "HS0002", futureDate.plusDays(10), futureDate.plusDays(13), 6, true)); // Booked
        
        mockTourRepo.setToursByHomestay("HS0001", homestay1Tours);
        mockTourRepo.setToursByHomestay("HS0002", homestay2Tours);
        
        // Act
        Object[][] result = tourService.getStatistics();
        
        // Assert
        assertEquals(2, result.length);
        
        // Check Homestay 1 statistics (only booked tours count)
        assertEquals("Homestay 1", result[0][0]);
        assertEquals(4, result[0][1]); // Only Tour 1 (4 tourists) is booked
        
        // Check Homestay 2 statistics
        assertEquals("Homestay 2", result[1][0]);
        assertEquals(6, result[1][1]); // Tour 3 (6 tourists) is booked
    }
    
    @Test
    public void testGetStatisticsNoBookedTours() {
        // Arrange
        List<Homestay> homestays = new ArrayList<>();
        homestays.add(new Homestay("HS0001", "Homestay 1", 3, "Address 1", 10));
        mockHomestayRepo.setHomestays(homestays);
        
        List<Tour> unbookedTours = new ArrayList<>();
        unbookedTours.add(new Tour("T00001", "Tour 1", "3 days 2 nights", 
                                 1500.0, "HS0001", futureDate, futureDate.plusDays(2), 4, false)); // Not booked
        mockTourRepo.setToursByHomestay("HS0001", unbookedTours);
        
        // Act
        Object[][] result = tourService.getStatistics();
        
        // Assert
        assertEquals(1, result.length);
        assertEquals("Homestay 1", result[0][0]);
        assertEquals(0, result[0][1]); // No booked tours
    }
    
    @Test
    public void testGetStatisticsEmptyHomestays() {
        // Arrange
        mockHomestayRepo.setHomestays(new ArrayList<>());
        
        // Act
        Object[][] result = tourService.getStatistics();
        
        // Assert
        assertEquals(0, result.length);
    }
    
    // ===== FILE I/O TESTS =====
    
    @Test
    public void testLoadFromFile() {
        // Act
        tourService.loadFromFile();
        
        // Assert
        assertTrue(mockTourRepo.loadFromFileWasCalled);
    }
    
    @Test
    public void testSaveToFile() {
        // Act
        tourService.saveToFile();
        
        // Assert
        assertTrue(mockTourRepo.saveToFileWasCalled);
    }
    
    // ===== MOCK REPOSITORY CLASSES =====
    
    private static class MockTourRepository extends repositories.TourRepository {
        private List<Tour> tours = new ArrayList<>();
        private List<Tour> futureTours = new ArrayList<>();
        private List<Tour> pastTours = new ArrayList<>();
        private List<Tour> upcomingToursByRevenue = new ArrayList<>();
        private List<Tour> expiredTours = new ArrayList<>();
        private boolean saveResult = true;
        
        // Tracking flags
        boolean saveWasCalled = false;
        boolean updateWasCalled = false;
        boolean findByDateAfterWasCalled = false;
        boolean findByDateBeforeWasCalled = false;
        boolean loadFromFileWasCalled = false;
        boolean saveToFileWasCalled = false;
        
        // Last operation data
        Tour lastSavedTour;
        Tour lastUpdatedTour;
        
        void setTours(List<Tour> tours) { this.tours = tours; }
        void addTour(Tour tour) { this.tours.add(tour); }
        void setFutureTours(List<Tour> tours) { this.futureTours = tours; }
        void setPastTours(List<Tour> tours) { this.pastTours = tours; }
        void setUpcomingToursByRevenue(List<Tour> tours) { this.upcomingToursByRevenue = tours; }
        void setExpiredTours(List<Tour> tours) { this.expiredTours = tours; }
        void setSaveResult(boolean result) { this.saveResult = result; }
        
        void setToursByHomestay(String homestayId, List<Tour> tours) {
            // Remove existing tours for this homestay
            this.tours.removeIf(t -> t.getHomeID().equals(homestayId));
            // Add new tours
            this.tours.addAll(tours);
        }
        
        @Override
        public List<Tour> getUpcomingToursByRevenue() {
            return new ArrayList<>(upcomingToursByRevenue);
        }
        
        @Override
        public List<Tour> getExpiredTours() {
            return new ArrayList<>(expiredTours);
        }
        
        @Override
        public boolean hasTimeConflict(Tour tour) {
            return false; // No conflicts in mock
        }
        @Override
        public List<Tour> findAll() { return new ArrayList<>(tours); }
        
        @Override
        public Tour findById(String id) {
            return tours.stream().filter(t -> t.getTourId().equals(id)).findFirst().orElse(null);
        }
        
        @Override
        public boolean save(Tour entity) {
            saveWasCalled = true;
            lastSavedTour = entity;
            if (saveResult) tours.add(entity);
            return saveResult;
        }
        
        @Override
        public boolean update(Tour entity) {
            updateWasCalled = true;
            lastUpdatedTour = entity;
            return true;
        }
        
        @Override
        public boolean delete(String id) { return tours.removeIf(t -> t.getTourId().equals(id)); }
        
        @Override
        public boolean exists(String id) { return findById(id) != null; }
        
        @Override
        public List<Tour> findByHomestayId(String homeId) {
            return tours.stream().filter(t -> t.getHomeID().equals(homeId)).collect(java.util.stream.Collectors.toList());
        }
        
        @Override
        public List<Tour> findByDateAfter(LocalDate date) {
            findByDateAfterWasCalled = true;
            return futureTours;
        }
        
        @Override
        public List<Tour> findByDateBefore(LocalDate date) {
            findByDateBeforeWasCalled = true;
            return pastTours;
        }
        
        @Override
        public List<Tour> findByBooked(boolean isBooked) { return new ArrayList<>(); }
        
        @Override
        public void loadFromFile() { loadFromFileWasCalled = true; }
        
        @Override
        public void saveToFile() { saveToFileWasCalled = true; }
        
        @Override
        public Tour parseLine(String line) { return null; }
    }
    
    private static class MockHomestayRepository implements IHomestayRepository {
        private List<Homestay> homestays = new ArrayList<>();
        
        void setHomestays(List<Homestay> homestays) { this.homestays = homestays; }
        void addHomestay(Homestay homestay) { this.homestays.add(homestay); }
        
        @Override
        public List<Homestay> findAll() { return new ArrayList<>(homestays); }
        
        @Override
        public Homestay findById(String id) {
            return homestays.stream().filter(h -> h.getHomeID().equals(id)).findFirst().orElse(null);
        }
        
        @Override
        public boolean save(Homestay entity) { return homestays.add(entity); }
        
        @Override
        public boolean update(Homestay entity) { return true; }
        
        @Override
        public boolean delete(String id) { return homestays.removeIf(h -> h.getHomeID().equals(id)); }
        
        @Override
        public boolean exists(String id) { return findById(id) != null; }
        
        @Override
        public List<Homestay> findByName(String name) { return new ArrayList<>(); }
        
        @Override
        public void loadFromFile() {}
    }
}