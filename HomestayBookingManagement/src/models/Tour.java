package models;

import java.time.LocalDate;

/**
 * Lớp Model Tour - Đại diện cho gói tour
 * 
 * Quy Tắc Nghiệp Vụ:
 * - Định dạng ID Tour: T + 5 chữ số (T00001)
 * - Giá phải dương
 * - Ngày khởi hành phải trước ngày kết thúc
 * - Số khách du lịch không thể vượt quá sức chứa homestay
 * - Các tour cùng homestay không thể trùng thời gian
 * 
 * @author ThanhDuy
 */
public class Tour implements Comparable<Tour> {

    // ===== CÁC THUỘC TÍNH =====
    private String tourId; // Định dạng: T00001
    private String tourName; // Mô tả tour
    private String time; // Thời lượng (ví dụ: "3 ngày 2 đêm")
    private double price; // Giá mỗi người
    private String homeID; // Liên kết tới homestay (HS0001)
    private LocalDate departureDate; // Ngày bắt đầu
    private LocalDate endDate; // Ngày kết thúc
    private int numberTourist; // Số khách du lịch
    private boolean isBooked; // Trạng thái đặt chỗ

    // Formatter tĩnh cho định dạng ngày nhất quán
    // private static final DateTimeFormatter DATE_FMT =
    // DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constructor mặc định
     */
    public Tour() {
    }

    /**
     * Constructor đầy đủ cho việc tạo Tour
     * 
     * @param tourId        Mã định danh tour (định dạng T00001)
     * @param tourName      Mô tả tour
     * @param time          Chuỗi thời lượng
     * @param price         Giá mỗi người
     * @param homeID        Mã định danh homestay
     * @param departureDate Ngày bắt đầu
     * @param endDate       Ngày kết thúc
     * @param numberTourist Số khách du lịch
     * @param isBooked      Trạng thái đặt chỗ
     */
    public Tour(String tourId, String tourName, String time, double price, String homeID, LocalDate departureDate,
            LocalDate endDate, int numberTourist, boolean isBooked) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.time = time;
        this.price = price;
        this.homeID = homeID;
        this.departureDate = departureDate;
        this.endDate = endDate;
        this.numberTourist = numberTourist;
        this.isBooked = isBooked;
    }

    // ... [Giữ nguyên các Getters và Setters của bạn] ...
    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public String getHomeID() {
        return homeID;
    }

    public void setHomeID(String homeID) {
        this.homeID = homeID;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getNumberTourist() {
        return numberTourist;
    }

    public void setNumberTourist(int numberTourist) {
        if (numberTourist < 0) {
            throw new IllegalArgumentException("Number of tourists cannot be negative");
        }
        this.numberTourist = numberTourist;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    /**
     * Tính tổng doanh thu cho tour này
     * Quy Tắc Nghiệp Vụ: Tổng Tiền = Giá × Số Khách Du Lịch
     * 
     * @return Tổng tiền cho tour
     */
    public double getTotalAmount() {
        return this.price * this.numberTourist;
    }

    // ===== CÁC PHƯƠNG THỨC LOGIC NGHIỆP VỤ =====

    /**
     * Kiểm tra tour có hết hạn (ngày khởi hành đã qua)
     * Quy Tắc Nghiệp Vụ: Tour hết hạn nếu ngày khởi hành < hôm nay
     * 
     * @return true nếu tour đã hết hạn, false nếu ngược lại
     */
    public boolean isExpired() {
        return departureDate.isBefore(LocalDate.now());
    }

    /**
     * Kiểm tra tour có sắp tới (ngày khởi hành trong tương lai)
     * Quy Tắc Nghiệp Vụ: Tour sắp tới nếu ngày khởi hành > hôm nay
     * 
     * @return true nếu tour sắp tới, false nếu ngược lại
     */
    public boolean isUpcoming() {
        return departureDate.isAfter(LocalDate.now());
    }

    /**
     * Kiểm tra tour này có trùng với tour khác
     * Quy Tắc Nghiệp Vụ: Các tour sử dụng cùng homestay không thể trùng thời gian
     * 
     * Thuật Toán: Hai khoảng ngày trùng nếu (start1 <= end2) && (start2 <= end1)
     * Đã sửa: Loại bỏ plusDays(1) để sử dụng logic trùng đúng
     * 
     * @param other Tour khác để kiểm tra trùng
     * @return true nếu các tour trùng, false nếu ngược lại
     */
    public boolean isOverlapWith(Tour other) {
        if (other == null || !this.homeID.equals(other.homeID)) {
            return false; // Homestay khác nhau hoặc null, không xung đột
        }

        // Trùng khoảng chuẩn: (start1 <= end2) && (start2 <= end1)
        // Các tour kết thúc vào ngày mà tour khác bắt đầu KHÔNG trùng
        return (this.departureDate.isBefore(other.endDate) || this.departureDate.isEqual(other.endDate)) &&
                (other.departureDate.isBefore(this.endDate) || other.departureDate.isEqual(this.endDate));
    }

    /**
     * Xác thực ngày tour có hợp lý
     * Quy Tắc Nghiệp Vụ: Ngày khởi hành phải trước ngày kết thúc
     * 
     * @return true nếu ngày hợp lệ, false nếu ngược lại
     */
    public boolean hasValidDates() {
        return departureDate.isBefore(endDate);
    }

    /**
     * Lấy trạng thái tour hiện tại dựa trên quy tắc nghiệp vụ
     * 
     * Ưu Tiên Trạng Thái:
     * 1. EXPIRED - nếu ngày khởi hành đã qua
     * 2. BOOKED - nếu tour được khách hàng đặt
     * 3. AVAILABLE - trạng thái mặc định
     * 
     * @return Trạng thái tour hiện tại
     */
    public TourStatus getStatus() {
        if (isExpired()) {
            return TourStatus.EXPIRED;
        }
        if (isBooked) {
            return TourStatus.BOOKED;
        }
        return TourStatus.AVAILABLE;
    }

    /**
     * So sánh các tour theo tổng tiền (thứ tự giảm dần)
     * Sử dụng để sắp xếp các tour sắp tới theo doanh thu
     * 
     * @param that Tour để so sánh
     * @return Kết quả so sánh để sắp xếp
     */
    @Override
    public int compareTo(Tour that) {
        return Double.compare(that.getTotalAmount(), this.getTotalAmount());
    }

    /**
     * Chuyển đổi tour thành chuỗi định dạng file
     * Định dạng:
     * TourID,TourName,Time,Price,HomeID,DepartureDate,EndDate,NumberTourist,IsBooked
     * 
     * @return Chuỗi đã định dạng để lưu file
     */
    @Override
    public String toString() {
        try {
            String bookedStr = isBooked() ? "TRUE" : "FALSE";
            String sDeparture = departureDate.format(utilities.AppConstants.DATE_FMT);
            String sEnd = endDate.format(utilities.AppConstants.DATE_FMT);

            return String.format("%s,%s,%s,%.1f,%s,%s,%s,%d,%s",
                    tourId, tourName, time, price, homeID, sDeparture, sEnd, numberTourist, bookedStr);
        } catch (Exception e) {
            System.err.println("Error formatting tour: " + e.getMessage());
            return tourId + ",ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR,ERROR";
        }
    }
}
