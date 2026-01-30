package repositories.interfaces;

import java.util.List;

import models.Homestay;

/**
 * Homestay Repository Interface - Specific queries cho Homestay
 */
public interface IHomestayRepository extends IRepository<Homestay> {

    List<Homestay> findByName(String name);

    void loadFromFile();
}
