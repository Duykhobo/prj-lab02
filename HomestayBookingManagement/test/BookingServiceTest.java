

import org.junit.Test;

import models.Booking;
import models.Tour;
import repositories.interfaces.IBookingRepository;
import repositories.interfaces.ITourRepository;
import services.BookingService;

import org.junit.Before;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit Tests for BookingService Class
 * Tests business logic with mock repositories
 */
public class BookingServiceTest {
    
    private BookingService bookingService;
    private MockBookingRepository mockBookingRepo;
    private MockTourRepository mockTourRepo;
    private LocalDate validBookingDate;
    private LocalDate validDepartureDate;
    
    @Before
    public void setUp() {
        mockBookingRepo = new MockBookingRepository();
        mockTourRepo = new MockTourRepository();
        bookingService = new BookingService(mockBookingRepo, mockTourRepo);
        
        validBookingDate = LocalDate.now().plusDays(1);
        validDepartureDate = LocalDate.now().plusDays(5);
    }
    
    // ===== BASIC CRUD TESTS =====
    
    @Test
    public void testGetAll() {
        // Arrange
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789"));
        mockBookingRepo.setBookings(expectedBookings);
        
        // Act
        List<Booking> result = bookingService.getAll();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("B00001", result.get(0).getBookingID());
    }
    
    @Test
    public void testGetById() {
        // Arrange
        Booking expectedBooking = new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789");
        mockBookingRepo.addBooking(expectedBooking);
        
        // Act
        Booking result = bookingService.getById("B00001");
        
        // Assert
        assertNotNull(result);
        assertEquals("B00001", result.getBookingID());
        assertEquals("John Doe", result.getFullName());
    }
    
    @Test
    public void testGetByIdNotFound() {
        // Act
        Booking result = bookingService.getById("NOTFOUND");
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testAdd() {
        // Arrange
        Booking booking = new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789");
        
        // Act
        bookingService.add(booking);
        
        // Assert
        assertTrue(mockBookingRepo.saveWasCalled);
        assertEquals(booking, mockBookingRepo.lastSavedBooking);
    }
    
    @Test
    public void testUpdate() {
        // Arrange
        Booking booking = new Booking("B00001", "John Doe Updated", "T00001", validBookingDate, "0123456789");
        
        // Act
        bookingService.update(booking);
        
        // Assert
        assertTrue(mockBookingRepo.updateWasCalled);
        assertEquals(booking, mockBookingRepo.lastUpdatedBooking);
    }
    
    // ===== BUSINESS VALIDATION TESTS =====
    
    @Test
    public void testAddBookingWithValidationSuccess() {
        // Arrange
        Tour availableTour = new Tour("T00001", "Test Tour", "3 days 2 nights", 
                                    1500.0, "HS0001", validDepartureDate, validDepartureDate.plusDays(2), 4, false);
        mockTourRepo.addTour(availableTour);
        
        Booking validBooking = new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789");
        
        // Act
        boolean result = bookingService.addBookingWithValidation(validBooking);
        
        // Assert
        assertTrue(result);
        assertTrue(mockBookingRepo.saveWasCalled);
        assertTrue(mockTourRepo.updateWasCalled);
        assertTrue(availableTour.isBooked()); // Tour should be marked as booked
    }
    
    @Test
    public void testAddBookingWithValidationTourNotFound() {
        // Arrange
        Booking booking = new Booking("B00001", "John Doe", "NOTFOUND", validBookingDate, "0123456789");
        
        // Act
        boolean result = bookingService.addBookingWithValidation(booking);
        
        // Assert
        assertFalse(result);
        assertFalse(mockBookingRepo.saveWasCalled);
    }
    
    @Test
    public void testAddBookingWithValidationTourAlreadyBooked() {
        // Arrange
        Tour bookedTour = new Tour("T00001", "Test Tour", "3 days 2 nights", 
                                 1500.0, "HS0001", validDepartureDate, validDepartureDate.plusDays(2), 4, true);
        mockTourRepo.addTour(bookedTour);
        
        Booking booking = new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789");
        
        // Act
        boolean result = bookingService.addBookingWithValidation(booking);
        
        // Assert
        assertFalse(result);
        assertFalse(mockBookingRepo.saveWasCalled);
    }
    
    @Test
    public void testAddBookingWithValidationInvalidBookingDate() {
        // Arrange
        Tour tour = new Tour("T00001", "Test Tour", "3 days 2 nights", 
                           1500.0, "HS0001", validDepartureDate, validDepartureDate.plusDays(2), 4, false);
        mockTourRepo.addTour(tour);
        
        // Booking date is after departure date (invalid)
        LocalDate invalidBookingDate = validDepartureDate.plusDays(1);
        Booking invalidBooking = new Booking("B00001", "John Doe", "T00001", invalidBookingDate, "0123456789");
        
        // Act
        boolean result = bookingService.addBookingWithValidation(invalidBooking);
        
        // Assert
        assertFalse(result);
        assertFalse(mockBookingRepo.saveWasCalled);
    }
    
    @Test
    public void testAddBookingWithValidationBookingDateSameAsDeparture() {
        // Arrange
        Tour tour = new Tour("T00001", "Test Tour", "3 days 2 nights", 
                           1500.0, "HS0001", validDepartureDate, validDepartureDate.plusDays(2), 4, false);
        mockTourRepo.addTour(tour);
        
        // Booking date same as departure date (invalid - must be before)
        Booking invalidBooking = new Booking("B00001", "John Doe", "T00001", validDepartureDate, "0123456789");
        
        // Act
        boolean result = bookingService.addBookingWithValidation(invalidBooking);
        
        // Assert
        assertFalse(result);
        assertFalse(mockBookingRepo.saveWasCalled);
    }
    
    @Test
    public void testAddBookingWithValidationDuplicateBookingID() {
        // Arrange
        Tour tour = new Tour("T00001", "Test Tour", "3 days 2 nights", 
                           1500.0, "HS0001", validDepartureDate, validDepartureDate.plusDays(2), 4, false);
        mockTourRepo.addTour(tour);
        
        Booking booking = new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789");
        mockBookingRepo.setSaveResult(false); // Simulate duplicate ID
        
        // Act
        boolean result = bookingService.addBookingWithValidation(booking);
        
        // Assert
        assertFalse(result);
        assertTrue(mockBookingRepo.saveWasCalled);
        assertFalse(mockTourRepo.updateWasCalled); // Tour should not be updated if booking save fails
    }
    
    // ===== REMOVE BOOKING TESTS =====
    
    @Test
    public void testRemoveBookingWithValidationSuccess() {
        // Arrange
        Booking existingBooking = new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789");
        mockBookingRepo.addBooking(existingBooking);
        
        Tour bookedTour = new Tour("T00001", "Test Tour", "3 days 2 nights", 
                                 1500.0, "HS0001", validDepartureDate, validDepartureDate.plusDays(2), 4, true);
        mockTourRepo.addTour(bookedTour);
        
        mockBookingRepo.setDeleteResult(true);
        
        // Act
        boolean result = bookingService.removeBookingWithValidation("B00001");
        
        // Assert
        assertTrue(result);
        assertTrue(mockBookingRepo.deleteWasCalled);
        assertTrue(mockTourRepo.updateWasCalled);
        assertFalse(bookedTour.isBooked()); // Tour should be marked as available
    }
    
    @Test
    public void testRemoveBookingWithValidationBookingNotFound() {
        // Act
        boolean result = bookingService.removeBookingWithValidation("NOTFOUND");
        
        // Assert
        assertFalse(result);
        assertFalse(mockBookingRepo.deleteWasCalled);
    }
    
    @Test
    public void testRemoveBookingWithValidationTourNotFound() {
        // Arrange
        Booking existingBooking = new Booking("B00001", "John Doe", "NOTFOUND", validBookingDate, "0123456789");
        mockBookingRepo.addBooking(existingBooking);
        mockBookingRepo.setDeleteResult(true);
        
        // Act
        boolean result = bookingService.removeBookingWithValidation("B00001");
        
        // Assert
        assertTrue(result); // Should still succeed even if tour not found
        assertTrue(mockBookingRepo.deleteWasCalled);
    }
    
    // ===== SEARCH TESTS =====
    
    @Test
    public void testFindByCustomerName() {
        // Arrange
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(new Booking("B00001", "John Doe", "T00001", validBookingDate, "0123456789"));
        expectedBookings.add(new Booking("B00002", "John Smith", "T00002", validBookingDate, "0987654321"));
        mockBookingRepo.setSearchResults(expectedBookings);
        
        // Act
        List<Booking> result = bookingService.findByCustomerName("John");
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(mockBookingRepo.findByCustomerNameWasCalled);
        assertEquals("John", mockBookingRepo.lastSearchName);
    }
    
    @Test
    public void testFindByCustomerNameNoResults() {
        // Arrange
        mockBookingRepo.setSearchResults(new ArrayList<>());
        
        // Act
        List<Booking> result = bookingService.findByCustomerName("NotFound");
        
        // Assert
        assertEquals(0, result.size());
        assertTrue(mockBookingRepo.findByCustomerNameWasCalled);
    }
    
    // ===== FILE I/O TESTS =====
    
    @Test
    public void testLoadFromFile() {
        // Act
        bookingService.loadFromFile();
        
        // Assert
        assertTrue(mockBookingRepo.loadFromFileWasCalled);
    }
    
    @Test
    public void testSaveToFile() {
        // Act
        bookingService.saveToFile();
        
        // Assert
        assertTrue(mockBookingRepo.saveToFileWasCalled);
    }
    
    // ===== MOCK REPOSITORY CLASSES =====
    
    private static class MockBookingRepository implements IBookingRepository {
        private List<Booking> bookings = new ArrayList<>();
        private List<Booking> searchResults = new ArrayList<>();
        private boolean saveResult = true;
        private boolean deleteResult = false;
        
        // Tracking flags
        boolean saveWasCalled = false;
        boolean updateWasCalled = false;
        boolean deleteWasCalled = false;
        boolean findByCustomerNameWasCalled = false;
        boolean loadFromFileWasCalled = false;
        boolean saveToFileWasCalled = false;
        
        // Last operation data
        Booking lastSavedBooking;
        Booking lastUpdatedBooking;
        String lastSearchName;
        
        void setBookings(List<Booking> bookings) { this.bookings = bookings; }
        void addBooking(Booking booking) { this.bookings.add(booking); }
        void setSearchResults(List<Booking> results) { this.searchResults = results; }
        void setSaveResult(boolean result) { this.saveResult = result; }
        void setDeleteResult(boolean result) { this.deleteResult = result; }
        
        @Override
        public List<Booking> findAll() { return new ArrayList<>(bookings); }
        
        @Override
        public Booking findById(String id) {
            return bookings.stream().filter(b -> b.getBookingID().equals(id)).findFirst().orElse(null);
        }
        
        @Override
        public boolean save(Booking entity) {
            saveWasCalled = true;
            lastSavedBooking = entity;
            if (saveResult) bookings.add(entity);
            return saveResult;
        }
        
        @Override
        public boolean update(Booking entity) {
            updateWasCalled = true;
            lastUpdatedBooking = entity;
            return true;
        }
        
        @Override
        public boolean delete(String id) {
            deleteWasCalled = true;
            return deleteResult;
        }
        
        @Override
        public boolean exists(String id) { return findById(id) != null; }
        
        @Override
        public List<Booking> findByTourId(String tourId) { return new ArrayList<>(); }
        
        @Override
        public List<Booking> findByCustomerName(String name) {
            findByCustomerNameWasCalled = true;
            lastSearchName = name;
            return searchResults;
        }
        
        @Override
        public void loadFromFile() { loadFromFileWasCalled = true; }
        
        @Override
        public void saveToFile() { saveToFileWasCalled = true; }
    }
    
    private static class MockTourRepository implements ITourRepository {
        private List<Tour> tours = new ArrayList<>();
        
        // Tracking flags
        boolean updateWasCalled = false;
        
        void addTour(Tour tour) { this.tours.add(tour); }
        
        @Override
        public List<Tour> findAll() { return new ArrayList<>(tours); }
        
        @Override
        public Tour findById(String id) {
            return tours.stream().filter(t -> t.getTourId().equals(id)).findFirst().orElse(null);
        }
        
        @Override
        public boolean save(Tour entity) { return tours.add(entity); }
        
        @Override
        public boolean update(Tour entity) {
            updateWasCalled = true;
            Tour existing = findById(entity.getTourId());
            if (existing != null) {
                int index = tours.indexOf(existing);
                tours.set(index, entity);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean delete(String id) { return tours.removeIf(t -> t.getTourId().equals(id)); }
        
        @Override
        public boolean exists(String id) { return findById(id) != null; }
        
        @Override
        public List<Tour> findByHomestayId(String homeId) { return new ArrayList<>(); }
        
        @Override
        public List<Tour> findByDateAfter(LocalDate date) { return new ArrayList<>(); }
        
        @Override
        public List<Tour> findByDateBefore(LocalDate date) { return new ArrayList<>(); }
        
        @Override
        public List<Tour> findByBooked(boolean isBooked) { return new ArrayList<>(); }
        
        @Override
        public void loadFromFile() {}
        
        @Override
        public void saveToFile() {}
    }
}