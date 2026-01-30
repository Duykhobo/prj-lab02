package utilities;

import java.util.List;

/**
 * Interface chung cho việc xử lý file. Giúp hệ thống có thể chuyển đổi linh
 * hoạt giữa Text, Binary hoặc Database.
 *
 * @param <T> Kiểu dữ liệu
 */
public interface IFileService<T> {

    boolean load(List<T> list, String fileName);

    boolean save(List<T> list, String fileName);
}
