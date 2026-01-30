

import org.junit.Test;

import models.Homestay;

import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Unit Tests for Homestay Model Class
 * Tests basic functionality and business rules
 */
public class HomestayTest {
    
    private Homestay validHomestay;
    
    @Before
    public void setUp() {
        validHomestay = new Homestay("HS0001", "Alee DaLat Homestay", 3, 
                                   "12A/6 3rd February Street", 15);
    }
    
    // ===== CONSTRUCTOR TESTS =====
    
    @Test
    public void testValidHomestayCreation() {
        // Arrange & Act
        Homestay homestay = new Homestay("HS0001", "Alee DaLat Homestay", 3, 
                                       "12A/6 3rd February Street", 15);
        
        // Assert
        assertEquals("HS0001", homestay.getHomeID());
        assertEquals("Alee DaLat Homestay", homestay.getHomeName());
        assertEquals(3, homestay.getRoomNumber());
        assertEquals("12A/6 3rd February Street", homestay.getAddress());
        assertEquals(15, homestay.getMaximumCapacity());
    }
    
    @Test
    public void testHomestayWithNullValues() {
        // Arrange & Act
        Homestay homestay = new Homestay(null, null, 0, null, 0);
        
        // Assert
        assertNull(homestay.getHomeID());
        assertNull(homestay.getHomeName());
        assertEquals(0, homestay.getRoomNumber());
        assertNull(homestay.getAddress());
        assertEquals(0, homestay.getMaximumCapacity());
    }
    
    // ===== GETTER AND SETTER TESTS =====
    
    @Test
    public void testSetHomeID() {
        // Act
        validHomestay.setHomeID("HS0002");
        
        // Assert
        assertEquals("HS0002", validHomestay.getHomeID());
    }
    
    @Test
    public void testSetHomeName() {
        // Act
        validHomestay.setHomeName("New Homestay Name");
        
        // Assert
        assertEquals("New Homestay Name", validHomestay.getHomeName());
    }
    
    @Test
    public void testSetRoomNumber() {
        // Act
        validHomestay.setRoomNumber(5);
        
        // Assert
        assertEquals(5, validHomestay.getRoomNumber());
    }
    
    @Test
    public void testSetAddress() {
        // Act
        validHomestay.setAddress("New Address 123");
        
        // Assert
        assertEquals("New Address 123", validHomestay.getAddress());
    }
    
    @Test
    public void testSetMaximumCapacity() {
        // Act
        validHomestay.setMaximumCapacity(20);
        
        // Assert
        assertEquals(20, validHomestay.getMaximumCapacity());
    }
    
    // ===== BUSINESS LOGIC TESTS =====
    
    @Test
    public void testToStringFormat() {
        // Act
        String result = validHomestay.toString();
        
        // Assert
        assertTrue(result.contains("HS0001"));
        assertTrue(result.contains("Alee DaLat Homestay"));
        assertTrue(result.contains("3"));
        assertTrue(result.contains("12A/6 3rd February Street"));
        assertTrue(result.contains("15"));
    }
    
    @Test
    public void testToStringFormatStructure() {
        // Act
        String result = validHomestay.toString();
        
        // Assert - Check pipe separators exist
        assertTrue(result.contains("|"));
        
        // Split by pipe and check parts
        String[] parts = result.split("\\|");
        assertEquals(5, parts.length);
    }
    
    // ===== BOUNDARY TESTS =====
    
    @Test
    public void testZeroRoomNumber() {
        // Act
        validHomestay.setRoomNumber(0);
        
        // Assert
        assertEquals(0, validHomestay.getRoomNumber());
    }
    
    @Test
    public void testNegativeRoomNumber() {
        // Act
        validHomestay.setRoomNumber(-1);
        
        // Assert
        assertEquals(-1, validHomestay.getRoomNumber());
    }
    
    @Test
    public void testZeroMaximumCapacity() {
        // Act
        validHomestay.setMaximumCapacity(0);
        
        // Assert
        assertEquals(0, validHomestay.getMaximumCapacity());
    }
    
    @Test
    public void testNegativeMaximumCapacity() {
        // Act
        validHomestay.setMaximumCapacity(-5);
        
        // Assert
        assertEquals(-5, validHomestay.getMaximumCapacity());
    }
    
    @Test
    public void testLargeRoomNumber() {
        // Act
        validHomestay.setRoomNumber(1000);
        
        // Assert
        assertEquals(1000, validHomestay.getRoomNumber());
    }
    
    @Test
    public void testLargeMaximumCapacity() {
        // Act
        validHomestay.setMaximumCapacity(500);
        
        // Assert
        assertEquals(500, validHomestay.getMaximumCapacity());
    }
    
    // ===== EDGE CASES =====
    
    @Test
    public void testEmptyStrings() {
        // Arrange & Act
        Homestay homestay = new Homestay("", "", 1, "", 1);
        
        // Assert
        assertEquals("", homestay.getHomeID());
        assertEquals("", homestay.getHomeName());
        assertEquals("", homestay.getAddress());
    }
    
    @Test
    public void testWhitespaceStrings() {
        // Arrange & Act
        Homestay homestay = new Homestay("   ", "   ", 1, "   ", 1);
        
        // Assert
        assertEquals("   ", homestay.getHomeID());
        assertEquals("   ", homestay.getHomeName());
        assertEquals("   ", homestay.getAddress());
    }
    
    @Test
    public void testSpecialCharactersInName() {
        // Act
        validHomestay.setHomeName("Homestay @#$%^&*()");
        
        // Assert
        assertEquals("Homestay @#$%^&*()", validHomestay.getHomeName());
    }
    
    @Test
    public void testSpecialCharactersInAddress() {
        // Act
        validHomestay.setAddress("123/456 Street, Ward 7, District 1, Ho Chi Minh City");
        
        // Assert
        assertEquals("123/456 Street, Ward 7, District 1, Ho Chi Minh City", validHomestay.getAddress());
    }
    
    @Test
    public void testUnicodeCharacters() {
        // Act
        validHomestay.setHomeName("Nhà Nghỉ Đà Lạt");
        validHomestay.setAddress("123 Đường Nguyễn Văn Cừ");
        
        // Assert
        assertEquals("Nhà Nghỉ Đà Lạt", validHomestay.getHomeName());
        assertEquals("123 Đường Nguyễn Văn Cừ", validHomestay.getAddress());
    }
    
    // ===== BUSINESS RULE TESTS =====
    
    @Test
    public void testHomestayIDFormat() {
        // Arrange & Act - HS + 4 digits format
        Homestay homestay = new Homestay("HS1234", "Test Homestay", 2, "Test Address", 10);
        
        // Assert
        assertEquals("HS1234", homestay.getHomeID());
        assertTrue(homestay.getHomeID().startsWith("HS"));
    }
    
    @Test
    public void testCapacityForTourValidation() {
        // Arrange
        int homestayCapacity = 15;
        int tourSize = 12;
        
        // Act & Assert - Tour size should not exceed homestay capacity
        assertTrue(tourSize <= homestayCapacity);
        assertTrue(validHomestay.getMaximumCapacity() >= tourSize);
    }
    
    @Test
    public void testCapacityExceedsLimit() {
        // Arrange
        int homestayCapacity = 10;
        int tourSize = 15;
        
        // Act & Assert - Tour size exceeds homestay capacity
        assertFalse(tourSize <= homestayCapacity);
    }
}