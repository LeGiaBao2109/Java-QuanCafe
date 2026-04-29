☕ QuanCafe - Coffee Management System (POS)
📌 Giới thiệu dự án
Dự án được xây dựng nhằm cung cấp giải pháp quản lý bán hàng và vận hành cho các quán cà phê vừa và nhỏ. Hệ thống tập trung vào việc tối ưu hóa quy trình gọi món (Order), quản lý trạng thái bàn thực tế và báo cáo doanh thu chính xác.

🏗 Kiến trúc hệ thống
Dự án được thực hiện với mô hình 3 lớp (3-Tier Architecture). Đây là lựa chọn chiến lược để đảm bảo hệ thống có hiệu năng ổn định và dễ dàng bảo trì:

Presentation Tier (Lớp Giao diện): Sử dụng Java Swing kết hợp thư viện FlatLaf để tạo giao diện hiện đại, thân thiện với người dùng.

Business Logic Tier (Lớp Xử lý): Quản lý các quy tắc nghiệp vụ như tính toán hóa đơn, áp dụng khuyến mãi và điều phối trạng thái bàn (trống/có khách).

Data Access Tier (Lớp Dữ liệu): Kết nối và truy xuất dữ liệu từ SQL Server thông qua các lớp DAO (Data Access Object), giúp tách biệt hoàn toàn logic xử lý và lưu trữ.

🚀 Tính năng chính
Quản lý Sơ đồ bàn: Hiển thị trực quan trạng thái bàn theo thời gian thực.

Order & Thanh toán: Giao diện gọi món nhanh chóng, hỗ trợ in hóa đơn.

Quản lý Thực đơn: Thêm, sửa, xóa món ăn và danh mục linh hoạt.

Báo cáo & Thống kê: Xuất dữ liệu doanh thu ra file Excel.

🛠 Công nghệ sử dụng
Ngôn ngữ: Java 21 (LTS)

Quản lý dự án: Maven

Cơ sở dữ liệu: Microsoft SQL Server

Thư viện UI: FlatLaf, Ikonli (Icon Font)

Công cụ khác: Apache POI (Excel), ZXing (QR Code)

📂 Cấu trúc thư mục
Plaintext
src/main/java/com/quanlycafe/
├── controller/  # Điều hướng và xử lý nghiệp vụ
├── dao/         # Truy vấn cơ sở dữ liệu (SQL)
├── entity/      # Định nghĩa các đối tượng (Model)
├── ui/          # Giao diện người dùng (JFrame/JPanel)
└── util/        # Các công cụ hỗ trợ (DB Connect, Helpers)