package utilities;

/**
 * Class tiện ích dùng để kiểm tra dữ liệu và ném Exception Giúp code Service
 * ngắn gọn hơn.
 */
public class Validation {

    // Kiểm tra chuỗi có bị null hoặc rỗng không
    public static void checkNullOrEmpty(String value, String errorMsg) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    // Kiểm tra Object có null không (dùng cho việc truyền tham số)
    public static void checkNullObject(Object obj, String errorMsg) {
        if (obj == null) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    // Check trùng: Nếu object đã tồn tại (khác null) thì BÁO LỖI
    // (Dùng cho chức năng Add)
    public static void checkDuplicate(Object obj, String errorMsg) {
        if (obj != null) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    // Check tồn tại: Nếu object không tìm thấy (bằng null) thì BÁO LỖI
    // (Dùng cho chức năng Update, Delete)
    public static void checkExists(Object obj, String errorMsg) {
        if (obj == null) {
            throw new IllegalArgumentException(errorMsg);
        }
    }
}
