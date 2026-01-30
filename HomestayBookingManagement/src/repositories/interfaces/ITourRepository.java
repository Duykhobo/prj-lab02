package repositories.interfaces;

import java.time.LocalDate;
import java.util.List;

import models.Tour;

/**
 * Tour Repository Interface - Specific queries cho Tour
 */
public interface ITourRepository extends IRepository<Tour> {
    
    List<Tour> findByHomestayId(String homeId);
    
    List<Tour> findByDateAfter(LocalDate date);
    
    List<Tour> findByDateBefore(LocalDate date);
    
    List<Tour> findByBooked(boolean isBooked);
    
    void loadFromFile();
    
    void saveToFile();
}
