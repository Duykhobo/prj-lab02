package services.interfaces;

import java.util.List;

/**
 * Interface chung cho các lớp Service (Business Logic).
 *
 * @param <T> Kiểu dữ liệu model
 */
public interface IService<T> {

    List<T> getAll();

    T getById(String id);

    void add(T item);

    void update(T item);

}