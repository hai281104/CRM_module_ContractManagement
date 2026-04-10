📜 Hệ Thống Quản Lý Hợp Đồng (CRM Module)
Dự án là một phân hệ chuyên sâu trong hệ thống CRM, tập trung vào việc quản lý vòng đời hợp đồng từ giai đoạn báo giá đến khi xuất bản và lưu trữ. Hệ thống hỗ trợ tự động hóa quy trình nghiệp vụ, giúp tối ưu hóa hiệu suất cho đội ngũ Sales và quản lý.

✨ Tính Năng Chính
1. Quản Lý Danh Sách & Bộ Lọc
Hiển thị danh sách hợp đồng với phân trang mượt mà.

Bộ lọc đa năng: Tìm kiếm theo từ khóa (Mã HĐ, Tên KH), tình trạng hợp đồng.

Lọc dữ liệu theo khoảng thời gian linh hoạt (Ngày tạo, Ngày bắt đầu, Ngày kết thúc).

Chức năng Hủy lọc nhanh chóng để quay lại trạng thái mặc định.

2. Chuyển Đổi Nghiệp Vụ (Workflow Automation)
Convert from Quote: Cho phép tạo hợp đồng tự động từ các Báo giá đã được phê duyệt.

Kế thừa dữ liệu khách hàng, giá trị và tiền tệ từ báo giá để giảm thiểu nhập liệu thủ công.

3. Thao Tác Hàng Loạt (Bulk Actions)
Xóa hàng loạt (Soft Delete) các hợp đồng được chọn.

Giao phụ trách (Assign Owner) hàng loạt cho nhân viên kinh doanh chỉ với vài click.

4. Biểu Mẫu & Xuất Tệp (Template & Export)
Quản lý Template: Thiết kế mẫu hợp đồng động bằng trình soạn thảo WYSIWYG (CKEditor 5).

Placeholders: Sử dụng các biến {{customer_name}}, {{contract_value}}... để tự động điền dữ liệu.

Xuất PDF chuyên nghiệp: Sử dụng thư viện OpenHTMLToPDF kết hợp Jsoup để xuất tệp PDF chuẩn XML, hỗ trợ đầy đủ phông chữ tiếng Việt (Times New Roman).

🛠 Công Nghệ Sử Dụng
Backend: Java Servlet, JDBC.

Frontend: JSP, JSTL, Bootstrap 5, Bootstrap Icons.

Database: MySQL.

Libraries: * OpenHTMLToPDF (Xuất PDF).

Jsoup (Chuẩn hóa HTML/XML).

CKEditor 5 (Trình soạn thảo mẫu).

MySQL Connector/J (Kết nối Database).

🚀 Hướng Dẫn Cài Đặt
1. Yêu cầu hệ thống
Java JDK 17+.

Apache Tomcat 9.0+.

Maven 3.6+.

MySQL Server 8.0+.

2. Cấu hình Database
Tạo database mới có tên db_crm.

Import file db_crm.sql (nếu có) hoặc chạy các script tạo bảng contracts, quotes, document_templates.

3. Cấu hình Font chữ (Quan trọng cho PDF)
Để tệp PDF hiển thị đúng tiếng Việt, bạn cần copy các tệp font Unicode vào dự án:

Vị trí: src/main/webapp/fonts/

Các tệp cần thiết: TIMES.TTF, TIMESBD.TTF, TIMESI.TTF, TIMESBI.TTF.

4. Chạy ứng dụng
Clone dự án về máy.

Mở dự án bằng Eclipse hoặc IntelliJ IDEA.

Chuột phải vào dự án -> Maven -> Update Project.

Add dự án vào Server Tomcat và nhấn Start.

Truy cập: http://localhost:8080/ContractManagement/contracts
