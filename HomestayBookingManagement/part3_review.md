# FILE 2: part3_review.md (Ghi chú Bảo vệ/Vấn đáp)

# Ghi chú Bảo vệ Đồ án & Vấn đáp (Oral Defense Notes)

Dưới đây là các điểm trọng tâm bạn cần ghi nhớ để trả lời giảng viên (Examiner/Professor) khi bảo vệ bài LAB/Assignment này.

## 1. Danh sách chức năng (Học thuộc lòng thứ tự)

Giảng viên thường yêu cầu: _"Chạy cho tôi chức năng số 4"_. Bạn cần biết ngay nó là gì.

1. **Add Tour**: Thêm tour, check trùng lặp, check sức chứa.
2. **Update Tour**: Sửa giá, ngày tháng.
3. **List Expired Tours**: Các tour đã đi (Ngày đi < Hôm nay).
4. **List Upcoming Tours**: Các tour sắp tới, **Sắp xếp theo Doanh thu**.
5. **Add Booking**: Đặt chỗ, khóa tour.
6. **Remove Booking**: Hủy đặt, mở khóa tour.
7. **Update Booking**: Sửa thông tin khách.
8. **Search Booking**: Tìm theo tên khách (gần đúng/chứa từ khóa).
9. **Statistics**: Tổng số khách theo từng Homestay.
10. **Quit**: Lưu dữ liệu ra file.

## 2. Các câu hỏi thường gặp (Q&A)

### Q1: Tại sao em lại chia ra Controller, Service, Repository? (Tại sao dùng MVC?)

- **Trả lời**:
- Để code gọn gàng và dễ bảo trì (Clean Code).
- **Controller**: Chỉ lo việc điều hướng nhập xuất, không chứa logic tính toán.
- **Service**: Chứa logic nghiệp vụ (ví dụ: kiểm tra trùng ngày, kiểm tra sức chứa). Nếu sau này có thêm giao diện Web, em vẫn dùng lại được Service này.
- **Repository**: Chuyên lo việc đọc ghi file. Nếu đổi sang Database SQL, em chỉ cần sửa lớp này.

### Q2: Logic kiểm tra trùng lịch (Overlap) hoạt động thế nào? (Câu hỏi KHÓ nhất)

- **Trả lời**:
- Em duyệt qua tất cả các tour đang có tại Homestay đó.
- Với mỗi tour cũ, em so sánh với tour mới định thêm.
- Điều kiện trùng là: `(Ngày_Đi_Mới <= Ngày_Về_Cũ) VÀ (Ngày_Đi_Cũ <= Ngày_Về_Mới)`.
- Hoặc giải thích đơn giản: Thời gian bắt đầu của cái này xảy ra trước khi cái kia kết thúc, và ngược lại.

### Q3: Làm sao em sắp xếp được danh sách Tour (Chức năng 4)?

- **Trả lời**:
- Em sử dụng `Collections.sort()` (hoặc `List.sort()`).
- Em viết một `Comparator` (bộ so sánh) tùy biến.
- Công thức so sánh: `(Giá * Số lượng khách)`. So sánh giảm dần (đảo ngược kết quả so sánh).

### Q4: Dữ liệu được lưu khi nào?

- **Trả lời**:
- Dữ liệu được load vào bộ nhớ (List) khi chương trình bắt đầu (`init`).
- Mọi thao tác Thêm/Sửa/Xóa đều diễn ra trên RAM (tốc độ nhanh).
- Dữ liệu chỉ được ghi đè xuống file (`.txt`) khi chọn chức năng **10. Quit**.

### Q5: Tại sao dùng `ArrayList` mà không dùng mảng thường `[]`?

- **Trả lời**: Vì số lượng Tour và Booking không cố định. `ArrayList` cho phép thêm/xóa phần tử linh hoạt (dynamic size) mà không cần khai báo kích thước trước.

## 3. Cách bảo vệ các lựa chọn thiết kế

- **Về Console**: "Em chọn Console để tập trung hoàn toàn vào logic Java Core và OOP, không bị phân tâm bởi việc kéo thả giao diện GUI."
- **Về File Text**: "Dữ liệu được lưu dạng CSV (cách nhau bởi dấu phẩy hoặc `|`) để dễ dàng đọc và kiểm tra bằng mắt thường. Nó đơn giản và phù hợp với bài Lab."
- **Về Validation**: "Em sử dụng lớp tiện ích `Inputter` và `Validation` riêng để tái sử dụng việc kiểm tra số nguyên, ngày tháng (dùng `SimpleDateFormat` hoặc `LocalDate`), tránh việc crash chương trình khi nhập sai định dạng."

## 4. Các lỗi thường bị trừ điểm (Cần tránh)

1. **Crash khi nhập sai**: Giảng viên nhập chữ vào chỗ yêu cầu số. -> _Phải dùng try-catch để bắt lỗi này._
2. **Quên lưu file**: Thêm tour xong, tắt ngang, bật lại mất tiêu. -> _Giải thích cơ chế Save ở nút Quit, hoặc đảm bảo nút Quit hoạt động đúng._
3. **Ngày tháng vô lý**: Ngày đi sau ngày về (Start > End), hoặc đặt lịch quá khứ. -> _Phải có validation chặn lại._
4. **Trùng ID**: Cho phép tạo 2 tour cùng ID. -> _Phải check ID exists trước khi tạo._

## 5. Checklist trước khi lên bảng (Demo)

- [ ] File `Tours.txt`, `Bookings.txt` có dữ liệu mẫu chưa? (Nên có sẵn vài dòng để demo chức năng List/Search ngay).
- [ ] Chức năng 4 (Sort) có thực sự sắp xếp không? (Test thử với giá trị doanh thu chênh lệch).
- [ ] Chức năng 1 (Add Tour) có chặn được trùng lịch không? (Thử nhập 1 tour y hệt giờ tour cũ xem có báo lỗi không).
- [ ] Format ngày tháng hiển thị có đẹp không? (Nên là `dd/MM/yyyy`).

**Mẹo nhỏ**: Khi demo, hãy chủ động nói: _"Bây giờ em sẽ demo chức năng kiểm tra trùng lịch..."_ và cố tình nhập sai để show ra thông báo lỗi. Giảng viên sẽ đánh giá cao việc bạn xử lý được các trường hợp ngoại lệ (Edge cases).
