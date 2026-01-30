

import org.junit.Test;

import models.Homestay;
import repositories.interfaces.IHomestayRepository;
import services.HomestayService;

import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit Tests for HomestayService Class
 * Tests business logic with mock repository
 */
public class HomestayServiceTest {
    
    private HomestayService homestayService;
    private MockHomestayRepository mockHomestayRepo;
    
    @Before
    public void setUp() {
        mockHomestayRepo = new MockHomestayRepository();
        homestayService = new HomestayService(mockHomestayRepo);
    }
    
    // ===== BASIC CRUD TESTS =====
    
    @Test
    public void testGetAll() {
        // Arrange
        List<Homestay> expectedHomestays = new ArrayList<>();
        expectedHomestays.add(new Homestay("HS0001", "Alee DaLat Homestay", 3, "12A/6 3rd February Street", 15));
        expectedHomestays.add(new Homestay("HS0002", "Mountain View Homestay", 5, "456 Highland Road", 20));
        mockHomestayRepo.setHomestays(expectedHomestays);
        
        // Act
        List<Homestay> result = homestayService.getAll();
        
        // Assert
        assertEquals(2, result.size());
        assertEquals("HS0001", result.get(0).getHomeID());
        assertEquals("HS0002", result.get(1).getHomeID());
    }
    
    @Test
    public void testGetAllEmpty() {
        // Arrange
        mockHomestayRepo.setHomestays(new ArrayList<>());
        
        // Act
        List<Homestay> result = homestayService.getAll();
        
        // Assert
        assertEquals(0, result.size());
    }
    
    @Test
    public void testGetById() {
        // Arrange
        Homestay expectedHomestay = new Homestay("HS0001", "Alee DaLat Homestay", 3, "12A/6 3rd February Street", 15);
        mockHomestayRepo.addHomestay(expectedHomestay);
        
        // Act
        Homestay result = homestayService.getById("HS0001");
        
        // Assert
        assertNotNull(result);
        assertEquals("HS0001", result.getHomeID());
        assertEquals("Alee DaLat Homestay", result.getHomeName());
        assertEquals(3, result.getRoomNumber());
        assertEquals("12A/6 3rd February Street", result.getAddress());
        assertEquals(15, result.getMaximumCapacity());
    }
    
    @Test
    public void testGetByIdNotFound() {
        // Act
        Homestay result = homestayService.getById("NOTFOUND");
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testGetByIdCaseInsensitive() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Test Homestay", 2, "Test Address", 10);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        Homestay result = homestayService.getById("hs0001"); // lowercase
        
        // Assert
        assertNotNull(result);
        assertEquals("HS0001", result.getHomeID());
    }
    
    @Test
    public void testAdd() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "New Homestay", 4, "New Address", 12);
        
        // Act
        homestayService.add(homestay);
        
        // Assert
        assertTrue(mockHomestayRepo.saveWasCalled);
        assertEquals(homestay, mockHomestayRepo.lastSavedHomestay);
    }
    
    @Test
    public void testUpdate() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Updated Homestay", 5, "Updated Address", 18);
        
        // Act
        homestayService.update(homestay);
        
        // Assert
        assertTrue(mockHomestayRepo.updateWasCalled);
        assertEquals(homestay, mockHomestayRepo.lastUpdatedHomestay);
    }
    
    // ===== BUSINESS LOGIC TESTS =====
    
    @Test
    public void testCanAccommodateSuccess() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Large Homestay", 5, "Test Address", 20);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        boolean result = homestayService.canAccommodate("HS0001", 15);
        
        // Assert
        assertTrue(result); // 15 <= 20
    }
    
    @Test
    public void testCanAccommodateExactCapacity() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Exact Homestay", 3, "Test Address", 10);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        boolean result = homestayService.canAccommodate("HS0001", 10);
        
        // Assert
        assertTrue(result); // 10 == 10
    }
    
    @Test
    public void testCanAccommodateExceedsCapacity() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Small Homestay", 2, "Test Address", 8);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        boolean result = homestayService.canAccommodate("HS0001", 12);
        
        // Assert
        assertFalse(result); // 12 > 8
    }
    
    @Test
    public void testCanAccommodateHomestayNotFound() {
        // Act
        boolean result = homestayService.canAccommodate("NOTFOUND", 5);
        
        // Assert
        assertFalse(result); // Homestay doesn't exist
    }
    
    @Test
    public void testCanAccommodateZeroTourists() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Test Homestay", 3, "Test Address", 10);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        boolean result = homestayService.canAccommodate("HS0001", 0);
        
        // Assert
        assertTrue(result); // 0 <= 10
    }
    
    @Test
    public void testCanAccommodateNegativeTourists() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Test Homestay", 3, "Test Address", 10);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        boolean result = homestayService.canAccommodate("HS0001", -5);
        
        // Assert
        assertTrue(result); // -5 <= 10 (though negative tourists is illogical)
    }
    
    @Test
    public void testCanAccommodateZeroCapacityHomestay() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Zero Capacity", 0, "Test Address", 0);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        boolean result = homestayService.canAccommodate("HS0001", 1);
        
        // Assert
        assertFalse(result); // 1 > 0
    }
    
    @Test
    public void testCanAccommodateZeroCapacityZeroTourists() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Zero Capacity", 0, "Test Address", 0);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        boolean result = homestayService.canAccommodate("HS0001", 0);
        
        // Assert
        assertTrue(result); // 0 == 0
    }
    
    // ===== SEARCH TESTS =====
    
    @Test
    public void testFindByName() {
        // Arrange
        List<Homestay> searchResults = new ArrayList<>();
        searchResults.add(new Homestay("HS0001", "DaLat Mountain View", 3, "Address 1", 15));
        searchResults.add(new Homestay("HS0002", "DaLat Valley Homestay", 4, "Address 2", 12));
        mockHomestayRepo.setSearchResults(searchResults);
        
        // Act
        List<Homestay> result = homestayService.findByName("DaLat");
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(mockHomestayRepo.findByNameWasCalled);
        assertEquals("DaLat", mockHomestayRepo.lastSearchName);
    }
    
    @Test
    public void testFindByNameNoResults() {
        // Arrange
        mockHomestayRepo.setSearchResults(new ArrayList<>());
        
        // Act
        List<Homestay> result = homestayService.findByName("NotFound");
        
        // Assert
        assertEquals(0, result.size());
        assertTrue(mockHomestayRepo.findByNameWasCalled);
        assertEquals("NotFound", mockHomestayRepo.lastSearchName);
    }
    
    @Test
    public void testFindByNameEmptyString() {
        // Arrange
        List<Homestay> allHomestays = new ArrayList<>();
        allHomestays.add(new Homestay("HS0001", "Homestay 1", 3, "Address 1", 15));
        mockHomestayRepo.setSearchResults(allHomestays);
        
        // Act
        List<Homestay> result = homestayService.findByName("");
        
        // Assert
        assertEquals(1, result.size());
        assertTrue(mockHomestayRepo.findByNameWasCalled);
        assertEquals("", mockHomestayRepo.lastSearchName);
    }
    
    @Test
    public void testFindByNamePartialMatch() {
        // Arrange
        List<Homestay> searchResults = new ArrayList<>();
        searchResults.add(new Homestay("HS0001", "Mountain View Homestay", 3, "Address 1", 15));
        mockHomestayRepo.setSearchResults(searchResults);
        
        // Act
        List<Homestay> result = homestayService.findByName("Mountain");
        
        // Assert
        assertEquals(1, result.size());
        assertEquals("Mountain View Homestay", result.get(0).getHomeName());
    }
    
    // ===== FILE I/O TESTS =====
    
    @Test
    public void testLoadFromFile() {
        // Act
        homestayService.loadFromFile();
        
        // Assert
        assertTrue(mockHomestayRepo.loadFromFileWasCalled);
    }
    
    // ===== BOUNDARY TESTS =====
    
    @Test
    public void testCanAccommodateLargeNumbers() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Large Homestay", 10, "Test Address", Integer.MAX_VALUE);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        boolean result = homestayService.canAccommodate("HS0001", 1000000);
        
        // Assert
        assertTrue(result); // 1000000 < Integer.MAX_VALUE
    }
    
    @Test
    public void testCanAccommodateMaxIntegerCapacity() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Max Capacity", 1, "Test Address", Integer.MAX_VALUE);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        boolean result = homestayService.canAccommodate("HS0001", Integer.MAX_VALUE);
        
        // Assert
        assertTrue(result); // MAX_VALUE == MAX_VALUE
    }
    
    @Test
    public void testCanAccommodateExceedsMaxInteger() {
        // Arrange
        Homestay homestay = new Homestay("HS0001", "Limited Capacity", 1, "Test Address", 100);
        mockHomestayRepo.addHomestay(homestay);
        
        // Act
        boolean result = homestayService.canAccommodate("HS0001", Integer.MAX_VALUE);
        
        // Assert
        assertFalse(result); // MAX_VALUE > 100
    }
    
    // ===== MOCK REPOSITORY CLASS =====
    
    private static class MockHomestayRepository implements IHomestayRepository {
        private List<Homestay> homestays = new ArrayList<>();
        private List<Homestay> searchResults = new ArrayList<>();
        
        // Tracking flags
        boolean saveWasCalled = false;
        boolean updateWasCalled = false;
        boolean findByNameWasCalled = false;
        boolean loadFromFileWasCalled = false;
        
        // Last operation data
        Homestay lastSavedHomestay;
        Homestay lastUpdatedHomestay;
        String lastSearchName;
        
        void setHomestays(List<Homestay> homestays) { this.homestays = homestays; }
        void addHomestay(Homestay homestay) { this.homestays.add(homestay); }
        void setSearchResults(List<Homestay> results) { this.searchResults = results; }
        
        @Override
        public List<Homestay> findAll() { return new ArrayList<>(homestays); }
        
        @Override
        public Homestay findById(String id) {
            return homestays.stream()
                .filter(h -> h.getHomeID().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
        }
        
        @Override
        public boolean save(Homestay entity) {
            saveWasCalled = true;
            lastSavedHomestay = entity;
            return homestays.add(entity);
        }
        
        @Override
        public boolean update(Homestay entity) {
            updateWasCalled = true;
            lastUpdatedHomestay = entity;
            return true;
        }
        
        @Override
        public boolean delete(String id) {
            return homestays.removeIf(h -> h.getHomeID().equalsIgnoreCase(id));
        }
        
        @Override
        public boolean exists(String id) { return findById(id) != null; }
        
        @Override
        public List<Homestay> findByName(String name) {
            findByNameWasCalled = true;
            lastSearchName = name;
            return searchResults;
        }
        
        @Override
        public void loadFromFile() { loadFromFileWasCalled = true; }
    }
}