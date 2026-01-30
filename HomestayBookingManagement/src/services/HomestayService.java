package services;

import java.util.List;

import models.Homestay;
import repositories.interfaces.IHomestayRepository;
import services.interfaces.IService;

public class HomestayService implements IService<Homestay> {

    private final IHomestayRepository homestayRepository;

    // Dependency Injection via Constructor
    public HomestayService(IHomestayRepository homestayRepository) {
        this.homestayRepository = homestayRepository;
    }

    @Override
    public List<Homestay> getAll() {
        return homestayRepository.findAll();
    }

    @Override
    public Homestay getById(String id) {
        return homestayRepository.findById(id);
    }

    @Override
    public void add(Homestay item) {
        homestayRepository.save(item);
    }

    @Override
    public void update(Homestay item) {
        homestayRepository.update(item);
    }
    
    // ===== BUSINESS METHODS =====
    
    /**
     * Check if homestay can accommodate the number of tourists
     */
    public boolean canAccommodate(String homeId, int numberOfTourists) {
        Homestay homestay = homestayRepository.findById(homeId);
        return homestay != null && homestay.getMaximumCapacity() >= numberOfTourists;
    }
    
    /**
     * Find homestays by name (partial match)
     */
    public List<Homestay> findByName(String name) {
        return homestayRepository.findByName(name);
    }

    public void loadFromFile() {
        homestayRepository.loadFromFile();
    }
}
