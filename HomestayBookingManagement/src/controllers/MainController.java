package controllers;

import middlewares.MiddlewareChain;
import utilities.ErrorHandler;
import services.BookingService;
import services.HomestayService;
import services.TourService;
import view.MainView;

/**
 * Controller Chính - Điểm Khởi Đầu Ứng Dụng và Điều Hướng Menu
 * 
 * Trách nhiệm:
 * - Quản lý vòng đời ứng dụng (khởi động, tắt)
 * - Điều phối tải và lưu dữ liệu
 * - Điều hướng menu chính và định tuyến
 * - Điều phối các sub-controller
 * - Xử lý lỗi cho tất cả các thao tác
 * 
 * Các Design Pattern Sử Dụng:
 * - MVC Pattern: Lớp Controller
 * - Dependency Injection: Nhận services qua constructor
 * - Command Pattern: Lựa chọn menu được ánh xạ tới các hành động controller
 * 
 * @author ThanhDuy
 */
public class MainController {

    // ===== LỚP VIEW =====
    private MainView view; // Hiển thị menu chính

    // ===== CÁC SUB-CONTROLLERS =====
    private TourController tourController; // Các thao tác quản lý tour
    private BookingController bookingController; // Các thao tác quản lý booking

    // ===== LỚP SERVICE =====
    private TourService tourService; // Logic nghiệp vụ tour
    private BookingService bookingService; // Logic nghiệp vụ booking
    private HomestayService homestayService; // Logic nghiệp vụ homestay

    /**
     * Constructor với Dependency Injection
     * Khởi tạo tất cả services và tải dữ liệu từ files
     * 
     * @param homestayService Service logic nghiệp vụ homestay
     * @param tourService     Service logic nghiệp vụ tour
     * @param bookingService  Service logic nghiệp vụ booking
     */
    public MainController(HomestayService homestayService, TourService tourService, BookingService bookingService) {
        this.view = new MainView();
        this.homestayService = homestayService;
        this.tourService = tourService;
        this.bookingService = bookingService;

        // ===== GIẢI ĐOẠN TẢI DỮ LIỆU =====
        // Quan trọng: Tải dữ liệu trước khi tạo sub-controllers
        try {
            System.out.println(">> System: Loading data...");
            homestayService.loadFromFile(); // Tải homestays trước (được tham chiếu bởi tours)
            tourService.loadFromFile(); // Tải tours thứ hai (được tham chiếu bởi bookings)
            bookingService.loadFromFile(); // Tải bookings cuối cùng
            System.out.println(">> System: Data loaded successfully.");
        } catch (Exception e) {
            // Lỗi nghiêm trọng: Không thể tiếp tục mà không có dữ liệu
            ErrorHandler.logError(
                    new Exception("[CRITICAL ERROR]: Mandatory file is missing or invalid! Detail: " + e.getMessage()));
            // amazonq-ignore-next-line
            System.exit(1); // Kết thúc ứng dụng
        }

        // ===== KHỞI TẠO SUB-CONTROLLER =====
        // Tạo sub-controllers sau khi dữ liệu đã được tải
        this.tourController = new TourController(tourService, homestayService);
        this.bookingController = new BookingController(bookingService, tourService);
    }

    /**
     * Vòng lặp chính của ứng dụng
     * Hiển thị menu, lấy lựa chọn người dùng, và định tuyến tới controller thích
     * hợp
     * Tiếp tục cho đến khi người dùng chọn thoát (tùy chọn 10)
     */
    public void run() {
        int choice;
        do {
            // Lấy lựa chọn menu người dùng từ lớp view
            choice = view.getMainMenuChoice();
            final int finalChoice = choice;

            // MiddlewareChain xử lý exception tập trung
            // Bắt và hiển thị lỗi mà không làm crash ứng dụng
            MiddlewareChain.perform(() -> processMainChoice(finalChoice));

        } while (choice != 10); // Tiếp tục cho đến khi thoát
    }

    /**
     * Xử lý lựa chọn menu người dùng và định tuyến tới controller thích hợp
     * 
     * Cấu Trúc Menu:
     * 1-4: Quản Lý Tour (TourController)
     * 5-8: Quản Lý Booking (BookingController)
     * 9: Thống Kê (TourController - cần cả dữ liệu tour và homestay)
     * 10: Lưu và Thoát
     * 
     * @param choice Lựa chọn menu của người dùng (1-10)
     * @throws Exception Nếu lựa chọn không hợp lệ hoặc thao tác thất bại
     */
    private void processMainChoice(int choice) throws Exception {
        switch (choice) {
            // ===== PHẦN QUẢN LÝ TOUR =====
            case 1:
                // Thêm tour mới với xác thực
                tourController.addTour();
                break;
            case 2:
                // Cập nhật tour hiện tại theo ID
                tourController.updateTour();
                break;
            case 3:
                // Liệt kê các tour đã hết hạn (ngày khởi hành < hôm nay)
                tourController.listEarlierThanToday();
                break;
            case 4:
                // Liệt kê các tour sắp tới (ngày khởi hành > hôm nay) sắp xếp theo tổng tiền
                tourController.listLaterThanToday();
                break;

            // ===== PHẦN QUẢN LÝ BOOKING =====
            case 5:
                // Thêm booking mới và đánh dấu tour đã được đặt
                bookingController.addBooking();
                break;
            case 6:
                // Xóa booking và đánh dấu tour có sẵn
                bookingController.removeBooking();
                break;
            case 7:
                // Cập nhật chi tiết booking
                bookingController.updateBooking();
                break;
            case 8:
                // Tìm kiếm booking theo tên khách hàng (khớp một phần)
                bookingController.searchBooking();
                break;

            // ===== PHẦN THỐNG KÊ =====
            case 9:
                // Tạo thống kê: Tổng số khách du lịch mỗi homestay
                // Được xử lý bởi TourController vì cần cả dữ liệu tour và homestay
                tourController.showStatistics();
                break;

            // ===== VÒNG ĐỜI ỨNG DỤNG =====
            case 10:
                // Lưu tất cả dữ liệu và thoát ứng dụng
                System.out.println(">> Saving data...");

                try {
                    // Lưu tất cả dữ liệu đã sửa đổi vào files
                    tourService.saveToFile(); // Lưu thay đổi tour
                    bookingService.saveToFile(); // Lưu thay đổi booking
                    // Lưu ý: Dữ liệu Homestay chỉ đọc trong ứng dụng này
                    // Nếu thêm chỉnh sửa homestay sau này, bỏ comment:
                    // homestayService.saveToFile();

                    System.out.println(">> Data saved successfully!");
                } catch (Exception e) {
                    ErrorHandler.logError(new Exception("Warning: Failed to save some data: " + e.getMessage()));
                }

                System.out.println(">> Goodbye!");
                System.exit(0); // Kết thúc ứng dụng một cách nhẹ nhàng
                break;

            default:
                // Lựa chọn menu không hợp lệ
                throw new Exception("Invalid choice! Please select 1-10.");
        }
    }
}
