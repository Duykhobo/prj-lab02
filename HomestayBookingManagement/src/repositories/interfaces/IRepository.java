package repositories.interfaces;

import java.util.List;

/**
 * Generic Repository Interface - Contract cho tất cả repositories
 * Tách data access logic khỏi business logic
 */
public interface IRepository<T> {
    
    List<T> findAll();
    
    T findById(String id);
    
    boolean save(T entity);
    
    boolean update(T entity);
    
    boolean delete(String id);
    
    boolean exists(String id);
}
