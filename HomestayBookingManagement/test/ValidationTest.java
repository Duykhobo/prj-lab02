

import utilities.Validation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit Tests for Validation Utility Class
 * Tests general validation methods and edge cases
 */
public class ValidationTest {
    
    // ===== CHECK NULL OR EMPTY TESTS =====
    
    @Test
    public void testCheckNullOrEmptyWithValidString() {
        // Act & Assert - No exception should be thrown
        Validation.checkNullOrEmpty("Valid String", "Error message");
    }
    
    @Test
    public void testCheckNullOrEmptyWithNonEmptyString() {
        // Act & Assert - No exception should be thrown
        Validation.checkNullOrEmpty("Test", "Error message");
        Validation.checkNullOrEmpty("A", "Error message");
        Validation.checkNullOrEmpty("123", "Error message");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullOrEmptyWithNull() {
        // Act & Assert
        Validation.checkNullOrEmpty(null, "String cannot be null");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullOrEmptyWithEmptyString() {
        // Act & Assert
        Validation.checkNullOrEmpty("", "String cannot be empty");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullOrEmptyWithWhitespaceOnly() {
        // Act & Assert
        Validation.checkNullOrEmpty("   ", "String cannot be whitespace only");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullOrEmptyWithTabsAndSpaces() {
        // Act & Assert
        Validation.checkNullOrEmpty("\t\n  ", "String cannot be whitespace only");
    }
    
    @Test
    public void testCheckNullOrEmptyErrorMessage() {
        // Arrange
        String customMessage = "Custom error message";
        
        // Act & Assert
        try {
            Validation.checkNullOrEmpty(null, customMessage);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(customMessage, e.getMessage());
        }
    }
    
    // ===== CHECK NULL OBJECT TESTS =====
    
    @Test
    public void testCheckNullObjectWithValidObject() {
        // Arrange
        Object validObject = new Object();
        String validString = "Test";
        Integer validInteger = 123;
        
        // Act & Assert - No exception should be thrown
        Validation.checkNullObject(validObject, "Error message");
        Validation.checkNullObject(validString, "Error message");
        Validation.checkNullObject(validInteger, "Error message");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullObjectWithNull() {
        // Act & Assert
        Validation.checkNullObject(null, "Object cannot be null");
    }
    
    @Test
    public void testCheckNullObjectErrorMessage() {
        // Arrange
        String customMessage = "Custom null object error";
        
        // Act & Assert
        try {
            Validation.checkNullObject(null, customMessage);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(customMessage, e.getMessage());
        }
    }
    
    // ===== CHECK DUPLICATE TESTS =====
    
    @Test
    public void testCheckDuplicateWithNull() {
        // Act & Assert - No exception should be thrown when object is null (not duplicate)
        Validation.checkDuplicate(null, "Duplicate found");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckDuplicateWithExistingObject() {
        // Arrange
        Object existingObject = new Object();
        
        // Act & Assert
        Validation.checkDuplicate(existingObject, "Object already exists");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckDuplicateWithString() {
        // Act & Assert
        Validation.checkDuplicate("Existing String", "String already exists");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckDuplicateWithInteger() {
        // Act & Assert
        Validation.checkDuplicate(123, "Integer already exists");
    }
    
    @Test
    public void testCheckDuplicateErrorMessage() {
        // Arrange
        String customMessage = "Custom duplicate error";
        Object existingObject = new Object();
        
        // Act & Assert
        try {
            Validation.checkDuplicate(existingObject, customMessage);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(customMessage, e.getMessage());
        }
    }
    
    // ===== CHECK EXISTS TESTS =====
    
    @Test
    public void testCheckExistsWithValidObject() {
        // Arrange
        Object existingObject = new Object();
        String existingString = "Test";
        Integer existingInteger = 123;
        
        // Act & Assert - No exception should be thrown
        Validation.checkExists(existingObject, "Error message");
        Validation.checkExists(existingString, "Error message");
        Validation.checkExists(existingInteger, "Error message");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckExistsWithNull() {
        // Act & Assert
        Validation.checkExists(null, "Object does not exist");
    }
    
    @Test
    public void testCheckExistsErrorMessage() {
        // Arrange
        String customMessage = "Custom not found error";
        
        // Act & Assert
        try {
            Validation.checkExists(null, customMessage);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(customMessage, e.getMessage());
        }
    }
    
    // ===== BUSINESS SCENARIO TESTS =====
    
    @Test
    public void testAddOperationScenario() {
        // Arrange - Simulating Add operation: check for null input and no duplicates
        String newId = "NEW001";
        String existingId = null; // Simulating ID not found in database
        
        // Act & Assert - Should pass validation for Add operation
        Validation.checkNullOrEmpty(newId, "ID cannot be null or empty");
        Validation.checkDuplicate(existingId, "ID already exists");
    }
    
    @Test
    public void testUpdateOperationScenario() {
        // Arrange - Simulating Update operation: check for null input and object exists
        String updateId = "UPDATE001";
        Object existingObject = new Object(); // Simulating object found in database
        
        // Act & Assert - Should pass validation for Update operation
        Validation.checkNullOrEmpty(updateId, "ID cannot be null or empty");
        Validation.checkExists(existingObject, "Object not found for update");
    }
    
    @Test
    public void testDeleteOperationScenario() {
        // Arrange - Simulating Delete operation: check object exists
        Object existingObject = new Object(); // Simulating object found in database
        
        // Act & Assert - Should pass validation for Delete operation
        Validation.checkExists(existingObject, "Object not found for deletion");
    }
    
    @Test
    public void testFailedAddOperationDuplicate() {
        // Arrange - Simulating failed Add operation: duplicate found
        String newId = "DUPLICATE001";
        Object existingObject = new Object(); // Simulating duplicate found
        
        // Act & Assert
        try {
            Validation.checkNullOrEmpty(newId, "ID cannot be null or empty");
            Validation.checkDuplicate(existingObject, "ID already exists");
            fail("Expected IllegalArgumentException for duplicate");
        } catch (IllegalArgumentException e) {
            assertEquals("ID already exists", e.getMessage());
        }
    }
    
    @Test
    public void testFailedUpdateOperationNotFound() {
        // Arrange - Simulating failed Update operation: object not found
        String updateId = "NOTFOUND001";
        Object existingObject = null; // Simulating object not found
        
        // Act & Assert
        try {
            Validation.checkNullOrEmpty(updateId, "ID cannot be null or empty");
            Validation.checkExists(existingObject, "Object not found for update");
            fail("Expected IllegalArgumentException for not found");
        } catch (IllegalArgumentException e) {
            assertEquals("Object not found for update", e.getMessage());
        }
    }
    
    // ===== EDGE CASES =====
    
    @Test
    public void testCheckNullOrEmptyWithStringContainingOnlyNewlines() {
        // Act & Assert
        try {
            Validation.checkNullOrEmpty("\n\n\n", "String cannot be newlines only");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String cannot be newlines only", e.getMessage());
        }
    }
    
    @Test
    public void testCheckNullOrEmptyWithStringContainingValidContent() {
        // Act & Assert - Should pass
        Validation.checkNullOrEmpty("  Valid Content  ", "Error message");
        Validation.checkNullOrEmpty("\tValid\t", "Error message");
        Validation.checkNullOrEmpty("\nValid\n", "Error message");
    }
    
    @Test
    public void testMultipleValidationsCombined() {
        // Arrange
        String validString = "Valid";
        Object validObject = new Object();
        Object nullObject = null;
        Object existingObject = new Object();
        
        // Act & Assert - Multiple validations should all pass
        Validation.checkNullOrEmpty(validString, "String error");
        Validation.checkNullObject(validObject, "Object error");
        Validation.checkDuplicate(nullObject, "Duplicate error");
        Validation.checkExists(existingObject, "Exists error");
    }
    
    @Test
    public void testValidationWithEmptyErrorMessage() {
        // Act & Assert - Empty error message should still work
        try {
            Validation.checkNullOrEmpty(null, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("", e.getMessage());
        }
    }
    
    @Test
    public void testValidationWithNullErrorMessage() {
        // Act & Assert - Null error message should still work
        try {
            Validation.checkNullOrEmpty(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertNull(e.getMessage());
        }
    }
}