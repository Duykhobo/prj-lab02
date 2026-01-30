package main;

import controllers.MainController;
import repositories.BookingRepository;
import repositories.HomestayRepository;
import repositories.TourRepository;
import repositories.interfaces.IBookingRepository;
import repositories.interfaces.IHomestayRepository;
import repositories.interfaces.ITourRepository;
import services.BookingService;
import services.HomestayService;
import services.TourService;

/**
 * Điểm Khởi Đầu Ứng Dụng Chính - Hệ Thống Quản Lý Đặt Phòng Homestay
 *
 * Tổng Quan Kiến Trúc: Ứng dụng này tuân theo kiến trúc phân lớp với
 * dependency injection:
 *
 * 1. LỚP DỮ LIỆU (Repository Pattern): - IHomestayRepository, ITourRepository,
 * IBookingRepository (Interfaces) - HomestayRepository, TourRepository,
 * BookingRepository (Implementations) - Xử lý file I/O và lưu trữ dữ liệu
 *
 * 2. LỚP NGHIỆP VỤ (Service Pattern): - HomestayService, TourService,
 * BookingService - Chứa logic nghiệp vụ và quy tắc xác thực - Điều phối
 * giữa các repository
 *
 * 3. LỚP TRÌNH BÀY (MVC Pattern): - MainController (Điều khiển luồng ứng dụng)
 * - TourController, BookingController (Controllers tính năng) -
 * MainView, TourView, BookingView (Giao diện người dùng)
 *
 * Các Design Pattern Sử Dụng: - Dependency Injection: Tất cả dependencies được inject qua
 * constructors - Repository Pattern: Trừu tượng hóa truy cập dữ liệu - Service Layer
 * Pattern: Đóng gói logic nghiệp vụ - MVC Pattern: Tách biệt các mối quan tâm -
 * Template Method Pattern: Xử lý file
 *
 * Tính Năng Chính: - Quản lý tour với xác thực sức chứa - Hệ thống đặt phòng
 * với cập nhật trạng thái tour - Tạo thống kê - Lưu trữ dữ liệu
 * dựa trên file - Xác thực đầu vào toàn diện
 *
 * @author ThanhDuy
 * @version 1.0
 * @since 2025
 */
public class Main {// dependency injection

    /**
     * Điểm khởi đầu ứng dụng
     *
     * Trình Tự Khởi Tạo (THỨ TỰ QUAN TRỌNG): 1. Tạo các instance Repository
     * (Lớp Dữ Liệu) 2. Tạo các instance Service với DI (Lớp Nghiệp Vụ) 3.
     * Tạo Controller với Services (Lớp Trình Bày) 4. Bắt đầu vòng lặp chính
     * của ứng dụng
     *
     * @param args Tham số dòng lệnh (không sử dụng)
     */
    public static void main(String[] args) {
        // ===== BƯỚC 1: KHỞI TẠO LỚP DỮ LIỆU =====
        // Tạo các instance repository để truy cập dữ liệu
        // Chúng xử lý file I/O và lưu trữ dữ liệu
        IHomestayRepository homestayRepo = new HomestayRepository();
        ITourRepository tourRepo = new TourRepository();
        IBookingRepository bookingRepo = new BookingRepository();

        // ===== BƯỚC 2: KHỞI TẠO LỚP NGHIỆP VỤ =====
        // Tạo các instance service với dependency injection
        // Services chứa logic nghiệp vụ và quy tắc xác thực
        HomestayService homestayService = new HomestayService(homestayRepo);

        // QUAN TRỌNG: TourService cần cả tour và homestay repositories
        // để xác thực sức chứa và kiểm tra sự tồn tại của homestay
        TourService tourService = new TourService(tourRepo, homestayRepo);

        // QUAN TRỌNG: BookingService cần cả booking và tour repositories
        // để cập nhật trạng thái tour khi booking được tạo/xóa
        BookingService bookingService = new BookingService(bookingRepo, tourRepo);

        // ===== BƯỚC 3: KHỞI TẠO LỚP TRÌNH BÀY =====
        // Tạo main controller với tất cả services được inject
        // Controller xử lý luồng ứng dụng và tương tác người dùng
        MainController app = new MainController(homestayService, tourService, bookingService);

        // ===== BƯỚC 4: THỰC THI ỨNG DỤNG =====
        // Bắt đầu vòng lặp chính của ứng dụng
        // Điều này sẽ:
        // 1. Tải dữ liệu từ files
        // 2. Hiển thị menu chính
        // 3. Xử lý tương tác người dùng
        // 4. Lưu dữ liệu khi thoát
        app.run();
    }
}
