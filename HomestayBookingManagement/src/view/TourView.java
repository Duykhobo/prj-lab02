package view;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import models.Tour;
import utilities.Acceptable;
import utilities.DateValidator;
import utilities.GeneralView;
import utilities.Inputter;
import utilities.TimeValidator;

public class TourView extends GeneralView<Tour> {

        @Override
        public void displayList(List<Tour> list) {
                if (list.isEmpty()) {
                        displayMessage("No tours found.");
                        return;
                }

                // Kẻ đường viền chuẩn
                String border = "-----------------------------------------------------------------------------------------------------------------------";

                System.out.println(border);
                // Header: Cột Booked dùng %-8s
                System.out.printf("| %-8s | %-20s | %-18s | %10s | %-8s | %-12s | %-12s | %-8s |\n",
                                "ID", "Name", "Time", "Price", "HomeID", "Departure", "End", "Booked");
                System.out.println(border);

                for (Tour t : list) {
                        System.out.printf("| %-8s | %-20s | %-18s | %10.0f | %-8s | %-12s | %-12s | %-8s |\n",
                                        t.getTourId(),
                                        t.getTourName(),
                                        t.getTime(),
                                        t.getPrice(),
                                        t.getHomeID(),
                                        utilities.Inputter.formatDate(t.getDepartureDate()),
                                        utilities.Inputter.formatDate(t.getEndDate()),
                                        t.isBooked() ? "TRUE" : "FALSE");
                }
                System.out.println(border);
        }

        // --- 2. CÁC HÀM NHẬP LIỆU RIÊNG (Input Details) ---
        /**
         * Nhập thông tin chi tiết cho Tour mới.
         *
         * @param id              ID đã được validate từ Controller/GeneralView
         * @param isHomeStayExist Predicate để kiểm tra Homestay có tồn tại không
         * @return
         */
        public Tour inputTourDetails(String id, Predicate<String> isHomeStayExist) {
                System.out.println("--- ENTER TOUR DETAILS ---");

                String name = Inputter
                                .wrapRetry(() -> Inputter.getString("Enter Tour Name: ", "Invalid name",
                                                Acceptable.NAME_VALID));

                // Nhập time với validation format "X days Y nights"
                // Nhập time với validation format "X days Y nights"
                String time = Inputter.wrapRetry(() -> {
                        String t = Inputter.getString("Enter Time (e.g. 3 days 2 nights): ", "Cannot be empty", ".+");
                        return TimeValidator.validateTimeFormat(t);
                });

                // Giá phải dương
                double price = Inputter
                                .wrapRetry(() -> Inputter.getADouble("Enter Price: ", "Must be positive", 0,
                                                Double.MAX_VALUE));

                // Nhập HomeID: Phải kiểm tra xem ID này có trong file Homestays.txt không
                String homeID = Inputter.wrapRetry(() -> {
                        String hId = Inputter.getString("Enter Homestay ID: ", "Invalid format",
                                        Acceptable.HOME_ID_VALID);
                        if (!isHomeStayExist.test(hId)) {
                                throw new Exception("Homestay ID " + hId + " does not exist!");
                        }
                        return hId;
                });

                // Nhập ngày đi với validation
                LocalDate depDate = Inputter
                                .wrapRetry(() -> {
                                        LocalDate d = Inputter.getLocalDate("Enter Departure Date: ", "Invalid date");
                                        DateValidator.validateDate(d);
                                        return d;
                                });

                // Tự động tính end date dựa trên time
                LocalDate endDate = calculateEndDate(depDate, time);
                System.out.println("Calculated End Date: " + Inputter.formatDate(endDate));

                int num = Inputter
                                .wrapRetry(() -> Inputter.getAnInteger("Enter Number of Tourists: ", "Must be positive",
                                                1, 1000));

                // Tour mới thì booked = false
                return new Tour(id, name, time, price, homeID, depDate, endDate, num, false);
        }

        /**
         * Nhập thông tin Update (Enter để bỏ qua/giữ nguyên giá trị cũ).
         *
         * @param oldTour Đối tượng cũ để lấy giá trị hiển thị mặc định
         * @return
         */
        public Tour inputUpdateDetails(Tour oldTour) {
                System.out.println("--- UPDATE TOUR (Press Enter to skip) ---");

                // Dùng các hàm ...ForUpdate trong Inputter
                String newName = Inputter.wrapRetry(() -> Inputter.getStringForUpdate("New Name", oldTour.getTourName(),
                                Acceptable.NAME_VALID, "Invalid name"));
                String newTime = Inputter
                                .wrapRetry(() -> Inputter.getStringForUpdate("New Time", oldTour.getTime(), ".+",
                                                "Invalid"));
                double newPrice = Inputter.wrapRetry(
                                () -> Inputter.getDoubleForUpdate("New Price", oldTour.getPrice(), 0, Double.MAX_VALUE,
                                                "Invalid"));
                int newNum = Inputter.wrapRetry(
                                () -> Inputter.getIntForUpdate("New Num Tourists", oldTour.getNumberTourist(), 1, 1000,
                                                "Invalid"));

                // Tạo object mới (ID, HomeID, Date giữ nguyên vì đề bài/logic thường ít cho sửa
                // khóa ngoại/ngày tháng phức tạp)
                return new Tour(oldTour.getTourId(), newName, newTime, newPrice, oldTour.getHomeID(),
                                oldTour.getDepartureDate(), oldTour.getEndDate(), newNum, oldTour.isBooked());
        }

        public void displayStatistics(Object[][] stats) {
                String border = "---------------------------------------------";
                System.out.println(border);
                System.out.printf("| %-30s | %-10s |\n", "Homestay Name", "Tourists");
                System.out.println(border);
                for (Object[] stat : stats) {
                        System.out.printf("| %-30s | %10d |\n", stat[0], stat[1]);
                }
                System.out.println(border);
        }

        /**
         * Tính end date dựa trên departure date và time
         * 
         * @param departureDate Ngày khởi hành
         * @param time          Thời gian tour (format: "X days Y nights")
         * @return End date
         */
        private LocalDate calculateEndDate(LocalDate departureDate, String time) {
                // Extract số ngày từ time string
                String[] parts = time.split("\\s+");
                int days = Integer.parseInt(parts[0]);

                // End date = departure date + (days - 1) since tour ends on the last day
                return departureDate.plusDays(days - 1);
        }
}
