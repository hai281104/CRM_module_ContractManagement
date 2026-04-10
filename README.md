#  Phân Hệ Quản Lý Hợp Đồng (Contract Management CRM)

Dự án là một module cốt lõi thuộc hệ thống Quản trị Quan hệ Khách hàng (CRM), được phát triển trên nền tảng Java Servlet & JSP. Module này tự động hóa quy trình chuyển đổi từ Báo giá sang Hợp đồng, đồng thời hỗ trợ thiết kế biểu mẫu động và xuất tệp PDF đạt chuẩn in ấn văn phòng.

---

##  Tính năng nổi bật

* **Quản lý Danh sách & Tìm kiếm:**
    * Hiển thị danh sách hợp đồng tích hợp phân trang động.
    * Bộ lọc đa chiều: Tìm theo từ khóa (Mã HĐ, Tên KH), Tình trạng hợp đồng.
    * Lọc theo mốc thời gian linh hoạt (Ngày tạo, Ngày bắt đầu, Ngày kết thúc) kết hợp nút **Hủy lọc** nhanh.
* **Tự động hóa luồng nghiệp vụ (Workflow):**
    * Chuyển đổi dữ liệu trực tiếp từ Báo giá được duyệt sang Hợp đồng (Convert from Quote).
    * Kế thừa toàn bộ thông tin khách hàng, giá trị và tiền tệ để giảm rủi ro nhập liệu sai.
* **Thao tác hàng loạt (Bulk Actions):**
    * Hỗ trợ Xóa mềm (Soft Delete) nhiều hợp đồng cùng lúc.
    * Chuyển giao người phụ trách (Assign Owner) hàng loạt cho nhân viên Sales khác.
* **Biểu mẫu & Xuất PDF (Template & Export):**
    * Tích hợp trình soạn thảo WYSIWYG (CKEditor 5) để admin tự do thiết kế HTML Template.
    * Tự động điền dữ liệu (Placeholders) như `{{customer_name}}`, `{{contract_value}}` vào biểu mẫu.
    * Xuất file PDF chuẩn XML bằng thư viện `OpenHTMLToPDF` và `Jsoup`, nhúng sẵn bộ Font Unicode (Times New Roman).

---

##  Công nghệ sử dụng

* **Backend:** Java 17, Servlet API, JDBC
* **Frontend:** JSP, JSTL, Bootstrap 5, CKEditor 5
* **Database:** MySQL 8.0+
* **Build Tool:** Maven
* **Libraries:** `openhtmltopdf`, `jsoup`, `mysql-connector-j`

---
## Cấu trúc thư mục cốt lõi
```text
├── src/main/java/com/crm
│   ├── controllers         # Xử lý Request/Response (ContractController, TemplateController)
│   ├── models              # Tầng giao tiếp dữ liệu (ContractDAO, ContractDTO)
│   └── utils               # Tiện ích dùng chung (DBConnection)
├── src/main/webapp
│   ├── views               # Giao diện JSP (contract_list.jsp, contract_edit.jsp)
│   └── fonts               # Chứa tệp TTF nhúng cho PDF
└── pom.xml                 # Cấu hình Dependency Maven
```
## Cấu trúc cơ sở dữ liệu
### Table: Contracts
<img width="1324" height="518" alt="image" src="https://github.com/user-attachments/assets/d7f19e1d-df1a-4938-a1b4-ff883e43a10e" />

## Giao diện
### Giao diện chính
<img width="1850" height="678" alt="image" src="https://github.com/user-attachments/assets/0e358328-0f2c-4015-a4d6-0b4bea110463" />

### Giao diện thiết kế mẫu hợp đồng
<img width="1313" height="773" alt="image" src="https://github.com/user-attachments/assets/851bf028-5901-471b-a511-1d253c625fdf" />

## Giao diện chi tiết hợp đồng
<img width="790" height="572" alt="image" src="https://github.com/user-attachments/assets/1a5a0411-3c9e-4d90-b12c-2fc26ac83666" />


##  Hướng dẫn cài đặt và khởi chạy

### 1. Yêu cầu hệ thống

* **JDK 17** (hoặc tương thích)
* **Apache Tomcat 9.0+**
* **Maven Toolkit**
* **MySQL Workbench** hoặc Server

### 2. Thiết lập Cơ sở dữ liệu

Tạo sẵn một Schema trong hệ quản trị MySQL (ví dụ: `db_crm`). Sau đó, bạn cần thiết lập thông tin kết nối trong lớp `DBConnection.java` của dự án:

```java
// Đường dẫn: src/main/java/com/crm/utils/DBConnection.java
private static final String URL = "jdbc:mysql://localhost:3306/db_crm";
private static final String USER = "root";
private static final String PASSWORD = "mat_khau_cua_ban";
```
Lưu ý: Hãy đảm bảo bạn đã import cấu trúc các bảng contracts, quotes, document_templates vào cơ sở dữ liệu.

### 3. Cấu hình Phông chữ (Dành cho xuất PDF)
Để tính năng xuất PDF hiển thị đúng tiếng Việt, bạn cần cấp phát các tệp Font chữ cho server bằng cách:

Tạo thư mục fonts theo đường dẫn: src/main/webapp/fonts/

Copy 4 tệp phông chữ Times New Roman (TIMES.TTF, TIMESBD.TTF, TIMESI.TTF, TIMESBI.TTF) từ máy tính vào thư mục này.

### 4. Biên dịch và Chạy ứng dụng
Clone dự án về môi trường cục bộ.

Mở dự án thông qua IDE (Eclipse / IntelliJ IDEA).

Cập nhật thư viện Maven: Chuột phải vào dự án -> Maven -> Update Project.

Triển khai dự án lên Server Apache Tomcat.

Truy cập hệ thống qua đường dẫn:
http://localhost:8080/ContractManagement/contracts
