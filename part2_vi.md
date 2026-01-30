# PART 2 – VIETNAMESE PROJECT REPORT

## 1. Giới thiệu

Dự án này triển khai một hệ thống quản lý đặt phòng homestay toàn diện sử dụng các nguyên lý lập trình hướng đối tượng và thiết kế kiến trúc phân lớp. Hệ thống hỗ trợ các hoạt động đặt tour thông qua giao diện console, quản lý mối quan hệ giữa homestay, tour và đặt phòng của khách hàng trong khi thực thi các quy tắc nghiệp vụ nghiêm ngặt và ràng buộc xác thực dữ liệu.

Việc triển khai thể hiện các khái niệm Java OOP nâng cao bao gồm kế thừa, đa hình, đóng gói và trừu tượng hóa thông qua codebase có cấu trúc tốt, tách biệt các mối quan tâm qua nhiều lớp kiến trúc. Hệ thống sử dụng cơ chế lưu trữ dựa trên file và framework xác thực toàn diện để đảm bảo tính toàn vẹn dữ liệu và tuân thủ quy tắc nghiệp vụ.

## 2. Tổng quan hệ thống

Hệ thống quản lý đặt phòng homestay hoạt động như một ứng dụng dựa trên console được thiết kế để xử lý các hoạt động đặt chỗ tour cho các cơ sở lưu trú homestay. Hệ thống quản lý ba thực thể chính: homestay (nhà cung cấp chỗ ở), tour (gói du lịch), và booking (đặt chỗ của khách hàng).

Chức năng cốt lõi bao gồm tạo và quản lý tour, xác thực sức chứa homestay, xử lý đặt chỗ với ràng buộc ngày tháng, và khả năng báo cáo toàn diện. Hệ thống thực thi các quy tắc nghiệp vụ bao gồm giới hạn sức chứa, xác thực ngày tháng, tính duy nhất của đặt chỗ, và quản lý tình trạng có sẵn của tour.

Kiến trúc ứng dụng tuân theo các mẫu thiết kế doanh nghiệp đã được thiết lập với sự tách biệt rõ ràng giữa các lớp trình bày, logic nghiệp vụ và truy cập dữ liệu. Lưu trữ dựa trên file cung cấp tính bền vững trong khi duy trì sự đơn giản và tránh các phụ thuộc cơ sở dữ liệu bên ngoài.

## 3. Kiến trúc hệ thống

Hệ thống triển khai mẫu kiến trúc phân lớp với bốn lớp riêng biệt:

**Lớp trình bày**: Các thành phần giao diện người dùng dựa trên console bao gồm các lớp view và hệ thống menu xử lý tương tác người dùng và xác thực đầu vào.

**Lớp điều khiển**: Điều phối các hoạt động nghiệp vụ bằng cách phối hợp giữa các lớp trình bày và dịch vụ, quản lý luồng ứng dụng và trạng thái phiên người dùng.

**Lớp dịch vụ**: Chứa triển khai logic nghiệp vụ, thực thi các quy tắc nghiệp vụ, và phối hợp các hoạt động qua nhiều repository trong khi duy trì ranh giới giao dịch.

**Lớp repository**: Cung cấp trừu tượng hóa truy cập dữ liệu với triển khai lưu trữ dựa trên file, xử lý các hoạt động CRUD và serialization/deserialization dữ liệu.

**Lớp tiện ích**: Các mối quan tâm xuyên suốt bao gồm framework xác thực, tiện ích xử lý file, và các hàm helper chung được sử dụng qua tất cả các lớp.

## 4. Các mẫu thiết kế được sử dụng

**Model-View-Controller (MVC)**: Tách biệt logic trình bày khỏi logic nghiệp vụ thông qua các thành phần controller, view và model riêng biệt.

**Repository Pattern**: Trừu tượng hóa các hoạt động truy cập dữ liệu thông qua các interface repository, cho phép khớp nối lỏng giữa logic nghiệp vụ và cơ chế lưu trữ dữ liệu.

**Service Layer Pattern**: Đóng gói logic nghiệp vụ trong các lớp dịch vụ chuyên dụng, cung cấp ranh giới giao dịch và thực thi quy tắc nghiệp vụ.

**Dependency Injection**: Dependency injection dựa trên constructor cho phép khớp nối lỏng giữa các lớp dịch vụ và repository.

**Template Method Pattern**: Các hoạt động xử lý file sử dụng template method cho các hoạt động đọc/ghi chung với các triển khai parsing chuyên biệt.

## 5. Sơ đồ lớp UML (Chỉ mô tả bằng văn bản)

**Các lớp trong gói Model**:
- Lớp Booking với các thuộc tính: bookingID, fullName, tourID, bookingDate, phone
- Lớp Tour với các thuộc tính: tourId, tourName, time, price, homeID, departureDate, endDate, numberTourist, isBooked
- Lớp Homestay với các thuộc tính: homeID, homeName, roomNumber, address, maximumCapacity
- Enum TourStatus với các giá trị: AVAILABLE, BOOKED, EXPIRED, CANCELLED

**Các lớp trong gói Service**:
- Lớp BookingService triển khai interface IService với các phụ thuộc vào IBookingRepository và ITourRepository
- Lớp TourService triển khai interface IService với các phụ thuộc vào ITourRepository và IHomestayRepository
- Lớp HomestayService triển khai interface IService với phụ thuộc vào IHomestayRepository

**Các lớp trong gói Repository**:
- Lớp BookingRepository triển khai interface IBookingRepository, kế thừa TextFileHandler
- Lớp TourRepository triển khai interface ITourRepository, kế thừa TextFileHandler
- Lớp HomestayRepository triển khai interface IHomestayRepository, kế thừa TextFileHandler

**Các lớp trong gói Utility**:
- Lớp DateValidator với các phương thức xác thực tĩnh
- Lớp TimeValidator với các phương thức xác thực định dạng tĩnh
- Lớp Validation với các phương thức tiện ích tĩnh
- Lớp trừu tượng TextFileHandler với các template method cho hoạt động file

## 6. Sơ đồ hoạt động UML (Chỉ mô tả bằng văn bản)

**Luồng hoạt động thêm Booking**:
Nút khởi tạo dẫn đến nút quyết định "Xác thực tham số đầu vào". Nếu xác thực thất bại, luồng tiến tới "Hiển thị thông báo lỗi" và kết thúc. Nếu xác thực thành công, luồng tiếp tục đến nút quyết định "Kiểm tra tình trạng có sẵn của Tour".

Từ kiểm tra tình trạng có sẵn tour, nếu tour không có sẵn, luồng tiến tới "Hiển thị thông báo Tour không có sẵn" và kết thúc. Nếu tour có sẵn, luồng tiếp tục đến nút quyết định "Xác thực ngày đặt chỗ".

Từ xác thực ngày đặt chỗ, nếu ngày không hợp lệ, luồng tiến tới "Hiển thị thông báo lỗi ngày" và kết thúc. Nếu ngày hợp lệ, luồng tiếp tục đến nút hành động "Lưu bản ghi Booking".

Sau khi lưu booking, luồng tiến tới nút hành động "Cập nhật trạng thái Tour", sau đó đến nút hành động "Hiển thị thông báo thành công", và cuối cùng đến nút kết thúc.

**Luồng hoạt động quản lý Tour**:
Nút khởi tạo dẫn đến nút quyết định "Xác thực dữ liệu Tour". Nếu xác thực thất bại, luồng tiến tới xử lý lỗi. Nếu xác thực thành công, luồng tiếp tục đến nút quyết định "Kiểm tra sức chứa Homestay".

Từ kiểm tra sức chứa, nếu sức chứa bị vượt quá, luồng tiến tới xử lý lỗi sức chứa. Nếu sức chứa đủ, luồng tiếp tục đến nút quyết định "Kiểm tra xung đột ngày".

Từ kiểm tra xung đột, nếu có xung đột, luồng tiến tới xử lý lỗi xung đột. Nếu không có xung đột, luồng tiếp tục đến nút hành động "Lưu bản ghi Tour" và kết thúc thành công.

## 7. Sơ đồ luồng dữ liệu (Chỉ mô tả bằng văn bản)

**DFD cấp 0 (Sơ đồ ngữ cảnh)**:
Thực thể bên ngoài "Người dùng" tương tác với tiến trình "Hệ thống quản lý đặt phòng Homestay" thông qua các luồng dữ liệu "Lệnh người dùng" (đầu vào) và "Phản hồi hệ thống" (đầu ra). Hệ thống tương tác với kho dữ liệu "Lưu trữ File" thông qua các luồng dữ liệu "Hoạt động đọc/ghi".

**DFD cấp 1**:
Tiến trình 1.0 "Quản lý Tour" nhận "Dữ liệu Tour" từ Người dùng và trao đổi "Bản ghi Tour" với kho dữ liệu D1 "File Tour". Tiến trình 2.0 "Quản lý Booking" nhận "Dữ liệu Booking" từ Người dùng và trao đổi "Bản ghi Booking" với kho dữ liệu D2 "File Booking". Tiến trình 3.0 "Quản lý Homestay" nhận "Dữ liệu Homestay" từ Người dùng và trao đổi "Bản ghi Homestay" với kho dữ liệu D3 "File Homestay".

Luồng dữ liệu "Thông tin Tour" kết nối Tiến trình 1.0 với Tiến trình 2.0 để xác thực booking. Luồng dữ liệu "Sức chứa Homestay" kết nối Tiến trình 3.0 với Tiến trình 1.0 để xác thực sức chứa.

**DFD cấp 2 (Phân rã tiến trình 2.0)**:
Tiến trình 2.1 "Xác thực Booking" nhận "Yêu cầu Booking" từ Người dùng và "Trạng thái Tour" từ D1 "File Tour". Tiến trình 2.2 "Tạo Booking" nhận "Booking đã xác thực" từ Tiến trình 2.1 và lưu "Bản ghi Booking" vào D2 "File Booking". Tiến trình 2.3 "Cập nhật trạng thái Tour" nhận "Cập nhật Tour" từ Tiến trình 2.2 và sửa đổi "Bản ghi Tour" trong D1 "File Tour".

## 8. Luồng chức năng và mã giả

**Hoạt động thêm Booking**:
```
BẮT ĐẦU ThêmBooking
    NHẬP dữLiệuBooking
    
    NẾU XácThựcDữLiệuBooking(dữLiệuBooking) = SAI THÌ
        XUẤT "Dữ liệu booking không hợp lệ"
        TRẢ VỀ SAI
    KẾT THÚC NẾU
    
    tour = TìmTourTheoId(dữLiệuBooking.tourId)
    NẾU tour = NULL THÌ
        XUẤT "Không tìm thấy tour"
        TRẢ VỀ SAI
    KẾT THÚC NẾU
    
    NẾU tour.isBooked = ĐÚNG THÌ
        XUẤT "Tour đã được đặt"
        TRẢ VỀ SAI
    KẾT THÚC NẾU
    
    NẾU dữLiệuBooking.bookingDate >= tour.departureDate THÌ
        XUẤT "Ngày đặt chỗ không hợp lệ"
        TRẢ VỀ SAI
    KẾT THÚC NẾU
    
    booking = TạoBooking(dữLiệuBooking)
    NẾU LưuBooking(booking) = SAI THÌ
        XUẤT "Không thể lưu booking"
        TRẢ VỀ SAI
    KẾT THÚC NẾU
    
    tour.isBooked = ĐÚNG
    CậpNhậtTour(tour)
    
    XUẤT "Tạo booking thành công"
    TRẢ VỀ ĐÚNG
KẾT THÚC ThêmBooking
```

**Thuật toán xác thực Tour**:
```
BẮT ĐẦU XácThựcTour
    NHẬP dữLiệuTour
    
    homestay = TìmHomestayTheoId(dữLiệuTour.homeId)
    NẾU homestay = NULL THÌ
        TRẢ VỀ SAI
    KẾT THÚC NẾU
    
    NẾU dữLiệuTour.numberTourist > homestay.maximumCapacity THÌ
        TRẢ VỀ SAI
    KẾT THÚC NẾU
    
    NẾU dữLiệuTour.departureDate >= dữLiệuTour.endDate THÌ
        TRẢ VỀ SAI
    KẾT THÚC NẾU
    
    tourXungĐột = TìmTourTheoHomestayVàKhoảngNgày(
        dữLiệuTour.homeId, 
        dữLiệuTour.departureDate, 
        dữLiệuTour.endDate
    )
    
    NẾU tourXungĐột.size > 0 THÌ
        TRẢ VỀ SAI
    KẾT THÚC NẾU
    
    TRẢ VỀ ĐÚNG
KẾT THÚC XácThựcTour
```

## 9. Xác thực và quy tắc nghiệp vụ

**Quy tắc xác thực Booking**:
- ID Booking phải tuân theo định dạng "B" theo sau bởi năm chữ số
- Tên khách hàng phải từ 2 đến 50 ký tự
- Số điện thoại phải chứa chính xác 10 chữ số
- Ngày đặt chỗ phải trước ngày khởi hành tour
- Mỗi tour chỉ có thể có một booking

**Quy tắc xác thực Tour**:
- ID Tour phải tuân theo định dạng "T" theo sau bởi năm chữ số
- Giá phải không âm
- Số lượng khách du lịch không được vượt quá sức chứa tối đa của homestay
- Ngày khởi hành phải trước ngày kết thúc
- Các tour sử dụng cùng homestay không thể có khoảng ngày trùng lặp
- Thời gian tour không thể vượt quá 30 ngày

**Quy tắc xác thực ngày**:
- Tất cả ngày phải sử dụng định dạng dd/MM/yyyy
- Ngày không thể ở quá khứ
- Ngày không thể vượt quá 5 năm trong tương lai
- Ngày kết thúc phải sau ngày khởi hành

**Quy tắc xác thực định dạng thời gian**:
- Định dạng thời gian phải tuân theo mẫu "X ngày Y đêm"
- Ngày phải từ 1 đến 30
- Đêm phải từ 0 đến 29
- Đêm phải bằng ngày trừ một

## 10. Quyết định thiết kế và đánh đổi

**Quyết định lưu trữ dựa trên File**:
Được chọn thay vì hệ thống cơ sở dữ liệu để duy trì sự đơn giản và loại bỏ các phụ thuộc bên ngoài. Đánh đổi bao gồm khả năng truy cập đồng thời hạn chế và quản lý tính toàn vẹn dữ liệu thủ công, nhưng cung cấp triển khai và bảo trì dễ dàng hơn.

**Quyết định giao diện Console**:
Được chọn thay vì GUI để tập trung vào triển khai logic nghiệp vụ và giảm độ phức tạp. Đánh đổi bao gồm trải nghiệm người dùng hạn chế nhưng cho phép phát triển và kiểm thử nhanh chóng chức năng cốt lõi.

**Triển khai lớp Service**:
Triển khai lớp service toàn diện để đóng gói logic nghiệp vụ và cung cấp ranh giới giao dịch. Đánh đổi bao gồm độ phức tạp bổ sung nhưng đảm bảo tách biệt mối quan tâm và khả năng bảo trì phù hợp.

**Chiến lược xác thực**:
Triển khai xác thực cả phía client và lớp service để đảm bảo tính toàn vẹn dữ liệu. Đánh đổi bao gồm trùng lặp mã nhưng cung cấp xử lý lỗi mạnh mẽ và phản hồi người dùng.

**Triển khai mẫu Repository**:
Sử dụng mẫu repository với trừu tượng interface để cho phép thay đổi cơ chế lưu trữ trong tương lai. Đánh đổi bao gồm các lớp trừu tượng bổ sung nhưng cung cấp tính linh hoạt và khả năng kiểm thử.

## 11. Kết luận

Hệ thống quản lý đặt phòng homestay thành công thể hiện các nguyên lý lập trình hướng đối tượng nâng cao thông qua thiết kế phân lớp có kiến trúc tốt. Việc triển khai hiệu quả tách biệt các mối quan tâm qua các lớp trình bày, logic nghiệp vụ và truy cập dữ liệu trong khi duy trì chất lượng mã và khả năng bảo trì.

Hệ thống thực thi các quy tắc nghiệp vụ toàn diện thông qua framework xác thực mạnh mẽ và cung cấp cơ chế lưu trữ dựa trên file đáng tin cậy. Giao diện dựa trên console cho phép tương tác người dùng hiệu quả trong khi lớp service đảm bảo đóng gói logic nghiệp vụ và quản lý giao dịch phù hợp.

Các quyết định kiến trúc ưu tiên sự đơn giản, khả năng bảo trì và giá trị giáo dục trong khi thể hiện các mẫu thiết kế tiêu chuẩn ngành và thực hành tốt nhất. Việc triển khai phục vụ như một nền tảng hiệu quả để hiểu các nguyên lý phát triển ứng dụng doanh nghiệp và phương pháp thiết kế hướng đối tượng.