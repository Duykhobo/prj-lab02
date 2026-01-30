# Util Package - Reusable Java Utilities

A collection of reusable utility classes for Java console applications with input validation, file handling, menu system, and error handling.

## Components

### 1. **Acceptable** - Validation Patterns Interface
Common regex patterns for input validation.

```java
// Usage example
if (Acceptable.isValid(phone, Acceptable.VN_PHONE_VALID)) {
    // Valid Vietnamese phone number
}

// Create custom ID pattern
String customId = Acceptable.createIdPattern("HS", 4); // ^HS\\d{4}$
```

### 2. **Inputter** - Input Handler with Retry Logic
Handles all user input with automatic retry on error.

```java
// Basic input
int age = Inputter.wrapRetry(() -> 
    Inputter.getAnInteger("Enter age: ", "Invalid age!", 1, 150)
);

// String with regex validation
String email = Inputter.wrapRetry(() -> 
    Inputter.getString("Email: ", "Invalid email!", Acceptable.EMAIL_VALID)
);

// Update with old value
String newName = Inputter.wrapRetry(() -> 
    Inputter.getStringForUpdate("Name", oldName, Acceptable.NAME_VALID, "Invalid!")
);
```

### 3. **Menu** - Generic Menu System
Type-safe menu with automatic input validation.

```java
Menu<String> menu = new Menu<>("Main Menu");
menu.addNewOption("Add Item");
menu.addNewOption("View Items");
menu.addNewOption("Exit");
menu.print();
int choice = menu.getChoice(); // Auto-validates range

// Choose from object list
ArrayList<Product> products = getProducts();
Product selected = menu.getChoiceFromObjectList(products);
```

### 4. **ErrorHandler** - Centralized Error Handling
Handles exceptions with consistent error messages.

```java
ErrorHandler.handle(() -> {
    // Your code that might throw exceptions
    service.addItem(item);
});

// With return value
String result = ErrorHandler.handleWithReturn(() -> {
    return service.getData();
}, "default_value");
```

### 5. **Validation** - Data Validation Utilities
Quick validation checks that throw IllegalArgumentException.

```java
Validation.checkNullOrEmpty(name, "Name cannot be empty!");
Validation.checkExists(foundItem, "Item not found!");
Validation.checkDuplicate(existingItem, "Item already exists!");
```

### 6. **IFileService** - File Handler Interface
Generic interface for file operations.

```java
public interface IFileService<T> {
    boolean load(List<T> list, String fileName);
    boolean save(List<T> list, String fileName);
}
```

### 7. **TextFileHandler** - Text File Handler
Abstract class for reading/writing text files.

```java
public class ProductFileHandler extends TextFileHandler<Product> {
    @Override
    public Product parseLine(String line) {
        String[] parts = line.split(",");
        return new Product(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}

// Usage
IFileService<Product> fileHandler = new ProductFileHandler();
List<Product> products = new ArrayList<>();
fileHandler.load(products, "products.txt");
```

### 8. **BinaryFileHandler** - Binary File Handler
Generic handler for serialized objects.

```java
IFileService<Product> binaryHandler = new BinaryFileHandler<>();
binaryHandler.save(products, "products.dat");
binaryHandler.load(products, "products.dat");
```

### 9. **GeneralView** - Abstract View Base Class
Base class for view layer with common display methods.

```java
public class ProductView extends GeneralView<Product> {
    @Override
    public void displayList(List<Product> list) {
        list.forEach(System.out::println);
    }
    
    public void addProduct() {
        displayMessage("=== Add Product ===");
        String id = inputIdForAdd("ID: ", "^P\\d{4}$", 
            service::isDuplicate, "ID already exists!");
        // ... more input
        displaySuccess("Product added!");
    }
}
```

### 10. **Action** - Functional Interface
Simple functional interface for error handling.

```java
@FunctionalInterface
public interface Action {
    void execute() throws Exception;
}
```

## Quick Start

### 1. Copy the entire `util` package to your project

### 2. Create your domain-specific patterns
```java
public interface AppPatterns extends Acceptable {
    String STUDENT_ID = "^SE\\d{6}$";  // SE123456
    String COURSE_ID = "^CL\\d{4}$";   // CL1234
}
```

### 3. Implement file handler
```java
public class StudentFileHandler extends TextFileHandler<Student> {
    @Override
    public Student parseLine(String line) {
        // Parse logic
    }
}
```

### 4. Create view layer
```java
public class StudentView extends GeneralView<Student> {
    @Override
    public void displayList(List<Student> list) {
        // Display logic
    }
}
```

## Best Practices

1. **Always use wrapRetry** for user input to handle errors gracefully
2. **Extend Acceptable** for project-specific patterns instead of modifying it
3. **Use ErrorHandler.handle()** for service layer operations
4. **Implement IFileService** for consistent file operations
5. **Extend GeneralView** for consistent UI across views

## Dependencies
- Java 8+ (uses Lambda, Stream API, LocalDate)
- No external libraries required

## License
Free to use and modify for educational and commercial purposes.
