# PART 1 – ENGLISH PROJECT REPORT

## 1. Introduction

This project implements a comprehensive homestay booking management system utilizing object-oriented programming principles and layered architecture design. The system facilitates tour booking operations through a console-based interface, managing relationships between homestays, tours, and customer bookings while enforcing strict business rules and data validation constraints.

The implementation demonstrates advanced Java OOP concepts including inheritance, polymorphism, encapsulation, and abstraction through a well-structured codebase that separates concerns across multiple architectural layers. The system employs file-based persistence mechanisms and comprehensive validation frameworks to ensure data integrity and business rule compliance.

## 2. System Overview

The homestay booking management system operates as a console-based application designed to handle tour reservation operations for homestay accommodations. The system manages three primary entities: homestays (accommodation providers), tours (travel packages), and bookings (customer reservations).

Core functionality encompasses tour creation and management, homestay capacity validation, booking processing with date constraints, and comprehensive reporting capabilities. The system enforces business rules including capacity limitations, date validation, booking uniqueness, and tour availability management.

The application architecture follows established enterprise patterns with clear separation between presentation, business logic, and data access layers. File-based storage provides persistence while maintaining simplicity and avoiding external database dependencies.

## 3. System Architecture

The system implements a layered architecture pattern with four distinct layers:

**Presentation Layer**: Console-based user interface components including view classes and menu systems that handle user interaction and input validation.

**Controller Layer**: Orchestrates business operations by coordinating between presentation and service layers, managing application flow and user session state.

**Service Layer**: Contains business logic implementation, enforces business rules, and coordinates operations across multiple repositories while maintaining transaction boundaries.

**Repository Layer**: Provides data access abstraction with file-based persistence implementation, handling CRUD operations and data serialization/deserialization.

**Utility Layer**: Cross-cutting concerns including validation frameworks, file handling utilities, and common helper functions used across all layers.

## 4. Design Patterns Used

**Model-View-Controller (MVC)**: Separates presentation logic from business logic through distinct controller, view, and model components.

**Repository Pattern**: Abstracts data access operations through repository interfaces, enabling loose coupling between business logic and data persistence mechanisms.

**Service Layer Pattern**: Encapsulates business logic within dedicated service classes, providing transaction boundaries and business rule enforcement.

**Dependency Injection**: Constructor-based dependency injection enables loose coupling between service and repository layers.

**Template Method Pattern**: File handling operations utilize template methods for common read/write operations with specialized parsing implementations.

## 5. UML Class Diagram (Textual Description Only)

**Model Package Classes**:
- Booking class with attributes: bookingID, fullName, tourID, bookingDate, phone
- Tour class with attributes: tourId, tourName, time, price, homeID, departureDate, endDate, numberTourist, isBooked
- Homestay class with attributes: homeID, homeName, roomNumber, address, maximumCapacity
- TourStatus enumeration with values: AVAILABLE, BOOKED, EXPIRED, CANCELLED

**Service Package Classes**:
- BookingService class implementing IService interface with dependencies on IBookingRepository and ITourRepository
- TourService class implementing IService interface with dependencies on ITourRepository and IHomestayRepository
- HomestayService class implementing IService interface with dependency on IHomestayRepository

**Repository Package Classes**:
- BookingRepository class implementing IBookingRepository interface, extending TextFileHandler
- TourRepository class implementing ITourRepository interface, extending TextFileHandler
- HomestayRepository class implementing IHomestayRepository interface, extending TextFileHandler

**Utility Package Classes**:
- DateValidator class with static validation methods
- TimeValidator class with static format validation methods
- Validation class with static utility methods
- TextFileHandler abstract class with template methods for file operations

## 6. UML Activity Diagram (Textual Description Only)

**Add Booking Activity Flow**:
Initial node leads to "Validate Input Parameters" decision node. If validation fails, flow proceeds to "Display Error Message" and terminates. If validation succeeds, flow continues to "Check Tour Availability" decision node.

From tour availability check, if tour is unavailable, flow proceeds to "Display Tour Unavailable Message" and terminates. If tour is available, flow continues to "Validate Booking Date" decision node.

From booking date validation, if date is invalid, flow proceeds to "Display Date Error Message" and terminates. If date is valid, flow continues to "Save Booking Record" action node.

After saving booking, flow proceeds to "Update Tour Status" action node, then to "Display Success Message" action node, and finally to the final node.

**Tour Management Activity Flow**:
Initial node leads to "Validate Tour Data" decision node. If validation fails, flow proceeds to error handling. If validation succeeds, flow continues to "Check Homestay Capacity" decision node.

From capacity check, if capacity is exceeded, flow proceeds to capacity error handling. If capacity is sufficient, flow continues to "Check Date Conflicts" decision node.

From conflict check, if conflicts exist, flow proceeds to conflict error handling. If no conflicts exist, flow continues to "Save Tour Record" action node and terminates successfully.

## 7. Data Flow Diagram (Textual Description Only)

**Level 0 DFD (Context Diagram)**:
External entity "User" interacts with process "Homestay Booking Management System" through data flows "User Commands" (input) and "System Responses" (output). The system interacts with data store "File Storage" through data flows "Read/Write Operations".

**Level 1 DFD**:
Process 1.0 "Manage Tours" receives "Tour Data" from User and exchanges "Tour Records" with data store D1 "Tours File". Process 2.0 "Manage Bookings" receives "Booking Data" from User and exchanges "Booking Records" with data store D2 "Bookings File". Process 3.0 "Manage Homestays" receives "Homestay Data" from User and exchanges "Homestay Records" with data store D3 "Homestays File".

Data flow "Tour Information" connects Process 1.0 to Process 2.0 for booking validation. Data flow "Homestay Capacity" connects Process 3.0 to Process 1.0 for capacity validation.

**Level 2 DFD (Process 2.0 Decomposition)**:
Process 2.1 "Validate Booking" receives "Booking Request" from User and "Tour Status" from D1 "Tours File". Process 2.2 "Create Booking" receives "Validated Booking" from Process 2.1 and stores "Booking Record" to D2 "Bookings File". Process 2.3 "Update Tour Status" receives "Tour Update" from Process 2.2 and modifies "Tour Record" in D1 "Tours File".

## 8. Functional Flow and Pseudocode

**Add Booking Operation**:
```
BEGIN AddBooking
    INPUT bookingData
    
    IF ValidateBookingData(bookingData) = FALSE THEN
        OUTPUT "Invalid booking data"
        RETURN FALSE
    END IF
    
    tour = FindTourById(bookingData.tourId)
    IF tour = NULL THEN
        OUTPUT "Tour not found"
        RETURN FALSE
    END IF
    
    IF tour.isBooked = TRUE THEN
        OUTPUT "Tour already booked"
        RETURN FALSE
    END IF
    
    IF bookingData.bookingDate >= tour.departureDate THEN
        OUTPUT "Invalid booking date"
        RETURN FALSE
    END IF
    
    booking = CreateBooking(bookingData)
    IF SaveBooking(booking) = FALSE THEN
        OUTPUT "Failed to save booking"
        RETURN FALSE
    END IF
    
    tour.isBooked = TRUE
    UpdateTour(tour)
    
    OUTPUT "Booking created successfully"
    RETURN TRUE
END AddBooking
```

**Tour Validation Algorithm**:
```
BEGIN ValidateTour
    INPUT tourData
    
    homestay = FindHomestayById(tourData.homeId)
    IF homestay = NULL THEN
        RETURN FALSE
    END IF
    
    IF tourData.numberTourist > homestay.maximumCapacity THEN
        RETURN FALSE
    END IF
    
    IF tourData.departureDate >= tourData.endDate THEN
        RETURN FALSE
    END IF
    
    conflictingTours = FindToursByHomestayAndDateRange(
        tourData.homeId, 
        tourData.departureDate, 
        tourData.endDate
    )
    
    IF conflictingTours.size > 0 THEN
        RETURN FALSE
    END IF
    
    RETURN TRUE
END ValidateTour
```

## 9. Validation and Business Rules

**Booking Validation Rules**:
- Booking ID must follow format "B" followed by five digits
- Customer name must be between 2 and 50 characters
- Phone number must contain exactly 10 digits
- Booking date must precede tour departure date
- Each tour can have only one booking

**Tour Validation Rules**:
- Tour ID must follow format "T" followed by five digits
- Price must be non-negative
- Number of tourists must not exceed homestay maximum capacity
- Departure date must precede end date
- Tours using the same homestay cannot have overlapping date ranges
- Tour duration cannot exceed 30 days

**Date Validation Rules**:
- All dates must use dd/MM/yyyy format
- Dates cannot be in the past
- Dates cannot exceed 5 years in the future
- End dates must be after departure dates

**Time Format Validation Rules**:
- Time format must follow "X days Y nights" pattern
- Days must be between 1 and 30
- Nights must be between 0 and 29
- Nights must equal days minus one

## 10. Design Decisions and Trade-offs

**File-Based Storage Decision**:
Chosen over database systems to maintain simplicity and eliminate external dependencies. Trade-off includes limited concurrent access capabilities and manual data integrity management, but provides easier deployment and maintenance.

**Console Interface Decision**:
Selected over GUI to focus on business logic implementation and reduce complexity. Trade-off includes limited user experience but enables rapid development and testing of core functionality.

**Service Layer Implementation**:
Implemented comprehensive service layer to encapsulate business logic and provide transaction boundaries. Trade-off includes additional complexity but ensures proper separation of concerns and maintainability.

**Validation Strategy**:
Implemented both client-side and service-layer validation to ensure data integrity. Trade-off includes code duplication but provides robust error handling and user feedback.

**Repository Pattern Implementation**:
Utilized repository pattern with interface abstraction to enable future storage mechanism changes. Trade-off includes additional abstraction layers but provides flexibility and testability.

## 11. Conclusion

The homestay booking management system successfully demonstrates advanced object-oriented programming principles through a well-architected, layered design. The implementation effectively separates concerns across presentation, business logic, and data access layers while maintaining code quality and maintainability.

The system enforces comprehensive business rules through robust validation frameworks and provides reliable file-based persistence mechanisms. The console-based interface enables efficient user interaction while the service layer ensures proper business logic encapsulation and transaction management.

The architectural decisions prioritize simplicity, maintainability, and educational value while demonstrating industry-standard design patterns and best practices. The implementation serves as an effective foundation for understanding enterprise application development principles and object-oriented design methodologies.