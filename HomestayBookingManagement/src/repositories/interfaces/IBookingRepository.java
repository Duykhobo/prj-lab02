package repositories.interfaces;

import java.util.List;

import models.Booking;

/**
 * Booking Repository Interface - Specific queries cho Booking
 */
public interface IBookingRepository extends IRepository<Booking> {
    
    List<Booking> findByTourId(String tourId);
    
    List<Booking> findByCustomerName(String name);
    
    void loadFromFile();
    
    void saveToFile();
}
