

import org.junit.Test;

import models.TourStatus;

import static org.junit.Assert.*;

/**
 * Unit Tests for TourStatus Enum
 * Tests enum values and methods
 */
public class TourStatusTest {
    
    // ===== ENUM VALUES TESTS =====
    
    @Test
    public void testAllEnumValues() {
        // Act
        TourStatus[] values = TourStatus.values();
        
        // Assert
        assertEquals(4, values.length);
        assertEquals(TourStatus.AVAILABLE, values[0]);
        assertEquals(TourStatus.BOOKED, values[1]);
        assertEquals(TourStatus.EXPIRED, values[2]);
        assertEquals(TourStatus.CANCELLED, values[3]);
    }
    
    @Test
    public void testValueOf() {
        // Act & Assert
        assertEquals(TourStatus.AVAILABLE, TourStatus.valueOf("AVAILABLE"));
        assertEquals(TourStatus.BOOKED, TourStatus.valueOf("BOOKED"));
        assertEquals(TourStatus.EXPIRED, TourStatus.valueOf("EXPIRED"));
        assertEquals(TourStatus.CANCELLED, TourStatus.valueOf("CANCELLED"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalidName() {
        // Act & Assert
        TourStatus.valueOf("INVALID");
    }
    
    // ===== DISPLAY NAME TESTS =====
    
    @Test
    public void testGetDisplayNameAvailable() {
        // Act & Assert
        assertEquals("Available", TourStatus.AVAILABLE.getDisplayName());
    }
    
    @Test
    public void testGetDisplayNameBooked() {
        // Act & Assert
        assertEquals("Booked", TourStatus.BOOKED.getDisplayName());
    }
    
    @Test
    public void testGetDisplayNameExpired() {
        // Act & Assert
        assertEquals("Expired", TourStatus.EXPIRED.getDisplayName());
    }
    
    @Test
    public void testGetDisplayNameCancelled() {
        // Act & Assert
        assertEquals("Cancelled", TourStatus.CANCELLED.getDisplayName());
    }
    
    // ===== TO STRING TESTS =====
    
    @Test
    public void testToStringAvailable() {
        // Act & Assert
        assertEquals("Available", TourStatus.AVAILABLE.toString());
    }
    
    @Test
    public void testToStringBooked() {
        // Act & Assert
        assertEquals("Booked", TourStatus.BOOKED.toString());
    }
    
    @Test
    public void testToStringExpired() {
        // Act & Assert
        assertEquals("Expired", TourStatus.EXPIRED.toString());
    }
    
    @Test
    public void testToStringCancelled() {
        // Act & Assert
        assertEquals("Cancelled", TourStatus.CANCELLED.toString());
    }
    
    // ===== CONSISTENCY TESTS =====
    
    @Test
    public void testToStringEqualsDisplayName() {
        // Act & Assert - toString() should equal getDisplayName()
        for (TourStatus status : TourStatus.values()) {
            assertEquals(status.getDisplayName(), status.toString());
        }
    }
    
    // ===== ENUM COMPARISON TESTS =====
    
    @Test
    public void testEnumEquality() {
        // Act & Assert
        assertEquals(TourStatus.AVAILABLE, TourStatus.AVAILABLE);
        assertNotEquals(TourStatus.AVAILABLE, TourStatus.BOOKED);
        assertNotEquals(TourStatus.BOOKED, TourStatus.EXPIRED);
        assertNotEquals(TourStatus.EXPIRED, TourStatus.CANCELLED);
    }
    
    @Test
    public void testEnumOrdinal() {
        // Act & Assert - Test enum order
        assertEquals(0, TourStatus.AVAILABLE.ordinal());
        assertEquals(1, TourStatus.BOOKED.ordinal());
        assertEquals(2, TourStatus.EXPIRED.ordinal());
        assertEquals(3, TourStatus.CANCELLED.ordinal());
    }
    
    @Test
    public void testEnumName() {
        // Act & Assert
        assertEquals("AVAILABLE", TourStatus.AVAILABLE.name());
        assertEquals("BOOKED", TourStatus.BOOKED.name());
        assertEquals("EXPIRED", TourStatus.EXPIRED.name());
        assertEquals("CANCELLED", TourStatus.CANCELLED.name());
    }
}