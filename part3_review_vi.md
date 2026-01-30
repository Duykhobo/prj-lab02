# PART 3 – GHI CHÚ ĐÁNH GIÁ DỰ ÁN & BẢO VỆ MIỆNG

## Các lĩnh vực giảng viên thường hỏi

**Biện minh kiến trúc**
- Tại sao bạn chọn kiến trúc phân lớp thay vì các mẫu khác?
- Giải thích luồng từ Controller → Service → Repository
- Thiết kế của bạn đảm bảo tách biệt mối quan tâm như thế nào?
- Điều gì xảy ra nếu bạn cần thay đổi từ lưu trữ file sang cơ sở dữ liệu?

**Triển khai logic nghiệp vụ**
- Trình bày từng bước quy trình xác thực booking
- Giải thích cách hoạt động của phát hiện xung đột tour
- Tại sao xác thực ngày booking quan trọng đối với quy tắc nghiệp vụ?
- Bạn xử lý các lần thử booking đồng thời như thế nào?

**Nguyên lý hướng đối tượng**
- Chỉ ra các ví dụ về đóng gói trong các lớp model của bạn
- Bạn thể hiện kế thừa ở đâu và tại sao?
- Hiển thị việc sử dụng đa hình trong các interface repository của bạn
- Giải thích trừu tượng hóa trong thiết kế lớp service của bạn

## Tại sao sử dụng MVC / Service Layer / Repository

**Biện minh mẫu MVC**:
- **Tách biệt mối quan tâm**: View xử lý trình bày, Controller điều phối luồng, Model đại diện dữ liệu
- **Khả năng bảo trì**: Thay đổi trong UI không ảnh hưởng đến logic nghiệp vụ
- **Khả năng kiểm thử**: Mỗi lớp có thể được kiểm thử độc lập
- **Tiêu chuẩn ngành**: Mẫu được chấp nhận rộng rãi trong các ứng dụng doanh nghiệp

**Biện minh lớp Service**:
- **Đóng gói logic nghiệp vụ**: Tất cả quy tắc nghiệp vụ được tập trung trong các lớp service
- **Ranh giới giao dịch**: Services quản lý các hoạt động nghiệp vụ hoàn chỉnh
- **Khả năng tái sử dụng**: Controllers và các client khác có thể tái sử dụng các phương thức service
- **Phối hợp xác thực**: Services phối hợp xác thực qua nhiều thực thể

**Biện minh mẫu Repository**:
- **Trừu tượng hóa truy cập dữ liệu**: Logic nghiệp vụ không biết về chi tiết file/database
- **Tính linh hoạt**: Dễ dàng chuyển từ lưu trữ file sang database
- **Khả năng kiểm thử**: Có thể mock repositories cho unit testing
- **Trách nhiệm đơn lẻ**: Mỗi repository xử lý một loại thực thể

## Tại sao Factory, Strategy, Observer, Singleton KHÔNG được sử dụng

**Factory Pattern - KHÔNG SỬ DỤNG**:
- **Lý do**: Tạo đối tượng đơn giản với constructors
- **Biện hộ**: "Các thực thể của chúng ta có logic tạo đơn giản. Factory sẽ thêm độ phức tạp không cần thiết cho việc tạo POJO cơ bản."
- **Thay thế**: Sử dụng constructor trực tiếp với xác thực trong setters

**Strategy Pattern - KHÔNG SỬ DỤNG**:
- **Lý do**: Không có biến thể thuật toán thay đổi tại runtime
- **Biện hộ**: "Xác thực và quy tắc nghiệp vụ của chúng ta cố định. Chúng ta không cần chuyển đổi thuật toán tại runtime."
- **Thay thế**: Các phương thức tiện ích tĩnh cho xác thực

**Observer Pattern - KHÔNG SỬ DỤNG**:
- **Lý do**: Không có yêu cầu hướng sự kiện hoặc nhu cầu khớp nối lỏng
- **Biện hộ**: "Hệ thống của chúng ta có các lời gọi phương thức trực tiếp. Không cần thông báo sự kiện giữa các đối tượng."
- **Thay thế**: Gọi phương thức trực tiếp trong lớp service

**Singleton Pattern - KHÔNG SỬ DỤNG**:
- **Lý do**: Dependency injection cung cấp khả năng kiểm thử tốt hơn
- **Biện hộ**: "Singletons làm cho unit testing khó khăn. Constructor injection cho chúng ta kiểm soát và khả năng kiểm thử tốt hơn."
- **Thay thế**: Dependency injection dựa trên constructor

## Danh sách kiểm tra tính nhất quán: Code ↔ UML ↔ Pseudocode ↔ DFD

**Xác minh Code với UML**:
- [ ] Tất cả lớp trong code xuất hiện trong sơ đồ lớp
- [ ] Mối quan hệ (associations, dependencies) khớp với triển khai
- [ ] Chữ ký phương thức khớp giữa code và UML
- [ ] Thuộc tính và kiểu của chúng nhất quán

**Xác minh UML với Pseudocode**:
- [ ] Các phương thức pseudocode tương ứng với các hoạt động UML
- [ ] Tương tác lớp trong pseudocode khớp với mối quan hệ UML
- [ ] Luồng dữ liệu trong pseudocode phản ánh các association UML

**Xác minh Pseudocode với DFD**:
- [ ] Các tiến trình trong DFD tương ứng với các hàm pseudocode chính
- [ ] Kho dữ liệu trong DFD khớp với cấu trúc dữ liệu trong pseudocode
- [ ] Luồng dữ liệu khớp với việc truyền tham số trong pseudocode

**Điểm xác minh chéo**:
- [ ] Tên thực thể nhất quán qua tất cả sơ đồ
- [ ] Quy tắc nghiệp vụ được phản ánh trong tất cả biểu diễn
- [ ] Đường dẫn xử lý lỗi được ghi lại ở mọi nơi

## Lỗi thường gặp gây mất điểm

**Lỗi tài liệu**:
- Tên thực thể không nhất quán giữa code và sơ đồ
- Thiếu quy tắc nghiệp vụ trong pseudocode
- Các tiến trình DFD không khớp với chức năng hệ thống thực tế
- Mối quan hệ UML không phản ánh phụ thuộc code

**Vấn đề chất lượng code**:
- Thiếu xác thực đầu vào trong các phương thức quan trọng
- Không xử lý lỗi cho hoạt động file
- Logic nghiệp vụ trộn lẫn với logic trình bày
- Giá trị hard-coded thay vì hằng số

**Vi phạm kiến trúc**:
- Controllers truy cập trực tiếp repositories (bỏ qua services)
- Lớp Model chứa logic nghiệp vụ
- Lớp View thực hiện xác thực dữ liệu
- Phụ thuộc vòng tròn giữa các lớp

**Lỗi logic nghiệp vụ**:
- Xác thực booking không đầy đủ (thiếu kiểm tra ngày)
- Phát hiện xung đột tour không hoạt động đúng
- Xác thực sức chứa không được thực thi
- Cập nhật trạng thái không đồng bộ

## Cách giải thích Repository dựa trên File

**Ưu điểm cần nhấn mạnh**:
- **Đơn giản**: "Không cần thiết lập cơ sở dữ liệu bên ngoài, triển khai dễ dàng hơn"
- **Tính di động**: "File dữ liệu có thể dễ dàng di chuyển giữa các hệ thống"
- **Tính minh bạch**: "Định dạng dữ liệu có thể đọc được bởi con người để debug"
- **Giá trị giáo dục**: "Tập trung vào các khái niệm OOP thay vì độ phức tạp database"

**Giải quyết hạn chế**:
- **Đồng thời**: "Hạn chế được thừa nhận - sẽ sử dụng database locking trong production"
- **Hiệu suất**: "Phù hợp cho tập dữ liệu nhỏ đến trung bình, sẽ tối ưu cho quy mô lớn hơn"
- **Tính toàn vẹn**: "Xác thực định dạng file đảm bảo tính nhất quán dữ liệu"
- **Sao lưu**: "Sao chép file đơn giản cung cấp cơ chế sao lưu"

**Điểm triển khai kỹ thuật**:
- Mẫu Template Method trong TextFileHandler
- Logic parsing tách biệt khỏi logic nghiệp vụ
- Xử lý lỗi cho hoạt động file
- Tính nhất quán định dạng dữ liệu qua tất cả thực thể

## Cách bảo vệ thiết kế dựa trên Console

**Biện hộ tập trung giáo dục**:
- "Giao diện console cho phép tập trung vào nguyên lý OOP và logic nghiệp vụ"
- "Loại bỏ độ phức tạp GUI không thêm vào mục tiêu học tập"
- "Dễ dàng hơn để thể hiện và kiểm thử chức năng cốt lõi"

**Lợi ích phát triển nhanh**:
- "Lặp lại nhanh hơn trên logic nghiệp vụ mà không có overhead thiết kế UI"
- "Phản hồi ngay lập tức để kiểm thử quy tắc nghiệp vụ"
- "Tách biệt rõ ràng giữa lớp trình bày và nghiệp vụ"

**Bối cảnh chuyên nghiệp**:
- "Nhiều hệ thống doanh nghiệp sử dụng giao diện console để quản trị"
- "Thể hiện hiểu biết về các khái niệm cốt lõi áp dụng cho bất kỳ UI nào"
- "Nền tảng có thể được mở rộng với giao diện web hoặc desktop sau này"

## Danh sách kiểm tra cuối cùng trước khi nộp

**Chất lượng code**:
- [ ] Tất cả lớp có comment JavaDoc phù hợp
- [ ] Quy ước đặt tên nhất quán trong toàn bộ
- [ ] Không có import hoặc biến không sử dụng
- [ ] Xử lý exception phù hợp trong tất cả phương thức
- [ ] Xác thực đầu vào trong tất cả phương thức hướng người dùng

**Tuân thủ kiến trúc**:
- [ ] Controllers chỉ điều phối, không chứa logic nghiệp vụ
- [ ] Services chứa tất cả quy tắc nghiệp vụ và xác thực
- [ ] Repositories chỉ xử lý truy cập dữ liệu
- [ ] Models là container dữ liệu thuần túy với xác thực cơ bản

**Độ chính xác tài liệu**:
- [ ] Sơ đồ lớp UML khớp với cấu trúc code thực tế
- [ ] Sơ đồ hoạt động phản ánh luồng chương trình thực tế
- [ ] Các tiến trình DFD tương ứng với chức năng hệ thống
- [ ] Thuật toán pseudocode khớp với logic triển khai

**Triển khai quy tắc nghiệp vụ**:
- [ ] Tất cả quy tắc xác thực booking được thực thi
- [ ] Ràng buộc sức chứa tour hoạt động
- [ ] Xác thực ngày toàn diện
- [ ] Cập nhật trạng thái được đồng bộ đúng

**Sẵn sàng kiểm thử**:
- [ ] File dữ liệu mẫu được chuẩn bị
- [ ] Kịch bản kiểm thử được ghi lại
- [ ] Các trường hợp lỗi được xử lý một cách duyên dáng
- [ ] Đường dẫn thành công hoạt động end-to-end

**Chuẩn bị bảo vệ miệng**:
- [ ] Có thể giải thích bất kỳ dòng code nào
- [ ] Hiểu tất cả quyết định thiết kế
- [ ] Biết tại sao các mẫu được chọn/từ chối
- [ ] Có thể theo dõi qua các kịch bản nghiệp vụ hoàn chỉnh
- [ ] Sẵn sàng sửa đổi code nếu được yêu cầu