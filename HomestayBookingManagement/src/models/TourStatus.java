package models;

/**
 * Enum Trạng Thái Tour - Quản lý trạng thái tour
 */
public enum TourStatus {
    AVAILABLE("Available"),
    BOOKED("Booked"),
    EXPIRED("Expired"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    TourStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}