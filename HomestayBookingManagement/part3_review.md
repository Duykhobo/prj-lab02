# NOTES FOR ORAL DEFENSE & PROJECT REVIEW

## 1. FULL FUNCTION LIST (Check against your code!)

_Ensure your `MainController.processMainChoice()` matches this EXACTLY:_

1.  **Add Tour**: `tourController.addTour()`
2.  **Update Tour**: `tourController.updateTour()`
3.  **List Expired Tours**: `tourController.listEarlierThanToday()`
4.  **List Upcoming Tours**: `tourController.listLaterThanToday()`
5.  **Add Booking**: `bookingController.addBooking()`
6.  **Remove Booking**: `bookingController.removeBooking()`
7.  **Update Booking**: `bookingController.updateBooking()`
8.  **Search Booking**: `bookingController.searchBooking()`
9.  **Statistics**: `tourController.showStatistics()`
10. **Quit**: Save & Exit

## 2. Typical Examiner Questions (Per Menu Option)

### Q: How do you validate "Add Tour"?

**A:** "In the Service Layer (`TourService`), I check three things:

1.  **Uniqueness**: ID checks against `TourRepository`.
2.  **Homestay**: Must exist in `HomestayRepository`.
3.  **Business Logic**:
    - **Capacity**: `Tour.TouristCount <= Homestay.MaximumCapacity`.
    - **Date Overlap**: I check `(Start1 <= End2) && (Start2 <= End1)` against all existing tours at the same Homestay.
    - **Homestay Existence**: `homeID` must be valid."

### Q: Explain the precise "Date Overlap" logic.

**A:** "For any two time ranges [StartA, EndA] and [StartB, EndB], they overlap if StartA is before or equal to EndB AND StartB is before or equal to EndA. My Service layer iterates through all tours at the target Homestay and applies this check."

### Q: Why can't I book a tour twice?

**A:** "I implemented a functional constraint: One Tour = One Main Booking. When a Booking is saved, I explicitly set `Tour.isBooked = true`. The `Add Booking` function checks this flag first and rejects the request if it's already true."

### Q: Why isn't "List Tours" just one function?

**A:** "The requirements specified distinct business logic:

- **Expired** needs an iterative filter (`date < now`).
- **Upcoming** needs a filter (`date > now`) AND a sorter (`Comparable` or `Comparator` by revenue). Separating them adheres to the **Single Responsibility Principle**."

### Q: How does the "Search Booking" work?

**A:** "I iterate through `bookingRepository.findAll()`. I use `String.contains()` to match the user's keyword against the `FullName` field. This is an O(n) linear search, which is acceptable for file-based systems."

### Q: What happens when I "Add Booking"?

**A:** "First, I verify the Tour exists and `isBooked` is false. If valid, I save the Booking. **Crucially**, I then update the `Tour` object's state to `isBooked = true` so no one else can book it."

### Q: How is "Statistics" calculated?

**A:** "I iterate through every `Homestay`. For each homestay, I find all matching `Tours`. I sum up the `NumberTourist` field of those tours. I do NOT modify any data here, just read and aggregate."

## 3. Defending Architecture & Design Decisions

### "Why files and not Database?"

**A:** "This is an OOP Lab focusing on core Java constraints. Using files allows the application to be 'self-contained' and portable without requiring a SQL server installation. I encapsulated the file logic in `TextFileHandler` so switching to SQL later would only require changing the Repository layer."

### "Why do you have a Service Layer?"

**A:** "Controllers should only handle user input and routing. If I put validation logic (like Overlap checks) in the Controller, it becomes 'Fat' and hard to read. The Service layer makes the code reusable and testable."

### "Why Console UI?"

**A:** "It forces me to focus on the logical flow and Object-Oriented structure rather than spending time on drag-and-drop UI elements. It ensures the backend logic is solid."

## 4. Why Patterns NOT Used?

- **Singleton**: "I used Dependency Injection for `TourService`, etc., into `MainController`. Singleton makes testing harder and hides dependencies."
- **Observer**: "The system is synchronous (Command -> Response). Observer is better for event-driven systems which we don't have here."

## 5. Common Mistakes & Deductions Checklist

- [ ] **Validation Bypassing**: Can I enter a negative price? (Fix using `do-while` loops).
- [ ] **Null Pointers**: Does the app crash if I search for a non-existent ID? (Fix using checks `if (obj == null)`).
- [ ] **Memory vs File**: Do updates persist after closing? (Ensure `saveToFile` is called on Quit).
- [ ] **Date Formats**: Does entering `30/02/2025` crash the app? (Use `try-catch` on Date parsing).

## 6. Final Defense Checklist

1.  **Run** the app.
2.  **Add** a Tour (Logic: Capacity < Max, Valid Dates).
3.  **Book** that Tour.
4.  Try to **Book** it again (Should fail: "Already Booked").
5.  **List** Upcoming Tours (Verify sorting by Revenue).
6.  **Quit** and Restart.
7.  **Search** the Booking. (Verify persistence).
8.  **Delete** the Booking.
9.  **Check** that the Tour is now available again (isBooked = false).

**GOOD LUCK!**
