package models;

/**
 * Lớp Model Homestay - Đại diện cho các cơ sở lưu trú
 * 
 * Định Dạng File: HomeID-HomeName-RoomNumber-Address-MaxCapacity
 * Ví dụ: HS0001-Alee DaLat Homestay-3-12A/6 3rd February Street-15
 * 
 * Quy Tắc Nghiệp Vụ:
 * - Định dạng HomeID: HS + 4 chữ số (HS0001)
 * - Số phòng phải dương
 * - Sức chứa tối đa quyết định giới hạn kích thước tour
 * - Địa chỉ nên đầy đủ và mô tả
 * 
 * @author ThanhDuy
 * @version 1.0
 * @since 2025
 */
public class Homestay {

    // ===== CÁC THUỘC TÍNH CHÍNH =====
    private String homeID;          // Mã định danh duy nhất (định dạng HS0001)
    private String homeName;        // Tên hiển thị homestay
    private int roomNumber;         // Số phòng có sẵn
    private String address;         // Địa chỉ đầy đủ
    private int maximumCapacity;    // Số khách tối đa cho phép (QUAN TRỌNG cho xác thực tour)

    /**
     * Constructor cho việc tạo Homestay
     * 
     * @param homeID Mã định danh homestay duy nhất (định dạng HS0001)
     * @param homeName Tên hiển thị của homestay
     * @param roomNumber Số phòng có sẵn
     * @param address Chuỗi địa chỉ đầy đủ
     * @param maximumCapacity Số khách tối đa (sử dụng cho xác thực sức chứa tour)
     */
    public Homestay(String homeID, String homeName, int roomNumber, String address, int maximumCapacity) {
        this.homeID = homeID;
        this.homeName = homeName;
        this.roomNumber = roomNumber;
        this.address = address;
        this.maximumCapacity = maximumCapacity; // QUAN TRỌNG: Sử dụng trong xác thực tour
    }

    // ===== CÁC GETTERS VÀ SETTERS =====
    
    /**
     * Lấy mã định danh homestay duy nhất
     * @return HomeID theo định dạng HS0001
     */
    public String getHomeID() {
        return homeID;
    }

    /**
     * Đặt mã định danh homestay
     * @param homeID Phải tuân theo định dạng HS0001
     */
    public void setHomeID(String homeID) {
        this.homeID = homeID;
    }

    /**
     * Lấy tên hiển thị homestay
     * @return Tên homestay
     */
    public String getHomeName() {
        return homeName;
    }

    /**
     * Đặt tên hiển thị homestay
     * @param homeName Tên mô tả
     */
    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    /**
     * Lấy số phòng
     * @return Số lượng phòng
     */
    public int getRoomNumber() {
        return roomNumber;
    }

    /**
     * Đặt số phòng
     * @param roomNumber Phải dương
     */
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * Lấy địa chỉ đầy đủ
     * @return Chuỗi địa chỉ
     */
    public String getAddress() {
        return address;
    }

    /**
     * Đặt địa chỉ đầy đủ
     * @param address Mô tả địa chỉ đầy đủ
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Lấy sức chứa khách tối đa
     * QUAN TRỌNG: Giá trị này được sử dụng cho xác thực tour
     * 
     * @return Số khách tối đa cho phép
     */
    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    /**
     * Đặt sức chứa khách tối đa
     * QUAN TRỌNG: Ảnh hưởng xác thực sức chứa tour
     * 
     * @param maximumCapacity Số khách tối đa (phải dương)
     */
    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    /**
     * Định dạng homestay để hiển thị
     * Sử dụng trong các màn hình liệt kê và lựa chọn
     * 
     * @return Chuỗi đã định dạng cho hiển thị console
     */
    @Override
    public String toString() {
        return String.format("%-10s | %-25s | %-5d | %-40s | %-5d",
                homeID, homeName, roomNumber, address, maximumCapacity);
    }
}
