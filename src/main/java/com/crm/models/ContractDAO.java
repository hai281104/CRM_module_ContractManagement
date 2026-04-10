package com.crm.models;

// Import class kết nối database
import com.crm.utils.DBConnection;

// Import thư viện SQL
import java.sql.*;

// Import List để lưu danh sách
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {

    
    // 1. lấy ds hợp đồng( phân trang với tìm)
    // =================================================================================================

	public List<ContractDTO> getContracts(int page, int pageSize, String searchKeyword, String status, String dateType, String fromDate, String toDate) {
	    List<ContractDTO> list = new ArrayList<>();
	    int offset = (page - 1) * pageSize;
	    
	    String sql = "SELECT c.id, c.contract_number, c.status, u.full_name as owner_name, " +
	                 "cus.name as customer_name, c.contract_value, c.start_date, c.end_date, c.created_at " +
	                 "FROM contracts c " +
	                 "LEFT JOIN users u ON c.owner_id = u.id " +
	                 "LEFT JOIN customers cus ON c.customer_id = cus.id " +
	                 "WHERE c.deleted_at IS NULL ";

	    if (searchKeyword != null && !searchKeyword.isEmpty()) {
	        sql += "AND (c.contract_number LIKE ? OR cus.name LIKE ?) ";
	    }
	    if (status != null && !status.isEmpty()) {
	        sql += "AND c.status = ? ";
	    }
	    
	    // Xử lý bộ lọc theo Loại ngày (dateType)
	    // Kiểm tra xem dateType có hợp lệ không để tránh SQL Injection
	    if (dateType != null && !dateType.isEmpty() && 
	       (dateType.equals("created_at") || dateType.equals("start_date") || dateType.equals("end_date"))) {
	        
	        if (fromDate != null && !fromDate.isEmpty()) {
	            sql += "AND DATE(c." + dateType + ") >= ? ";
	        }
	        if (toDate != null && !toDate.isEmpty()) {
	            sql += "AND DATE(c." + dateType + ") <= ? ";
	        }
	    }

	    sql += "ORDER BY c.created_at DESC LIMIT ? OFFSET ?";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        int paramIndex = 1;
	        if (searchKeyword != null && !searchKeyword.isEmpty()) {
	            ps.setString(paramIndex++, "%" + searchKeyword + "%");
	            ps.setString(paramIndex++, "%" + searchKeyword + "%");
	        }
	        if (status != null && !status.isEmpty()) {
	            ps.setString(paramIndex++, status);
	        }
	        // Gắn giá trị ngày tháng vào PreparedStatement
	        if (dateType != null && !dateType.isEmpty() && 
	           (dateType.equals("created_at") || dateType.equals("start_date") || dateType.equals("end_date"))) {
	            if (fromDate != null && !fromDate.isEmpty()) {
	                ps.setString(paramIndex++, fromDate);
	            }
	            if (toDate != null && !toDate.isEmpty()) {
	                ps.setString(paramIndex++, toDate);
	            }
	        }
	        
	        ps.setInt(paramIndex++, pageSize);
	        ps.setInt(paramIndex, offset);

	       
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	        	// 1. Khởi tạo đối tượng DTO mới cho mỗi dòng dữ liệu
	            ContractDTO c = new ContractDTO();

	            
	            c.setId(rs.getLong("id"));
	            
	            c.setContractNumber(rs.getString("contract_number"));
	            c.setStatus(rs.getString("status"));
	            c.setOwnerName(rs.getString("owner_name"));       // alias u.full_name as owner_name
	            c.setCustomerName(rs.getString("customer_name")); // alias cus.name as customer_name
	            c.setContractValue(rs.getDouble("contract_value"));
	            c.setStartDate(rs.getDate("start_date"));
	            c.setEndDate(rs.getDate("end_date"));
	            c.setCreatedAt(rs.getDate("created_at"));

	            // 3. Thêm đối tượng đã có đầy đủ dữ liệu vào danh sách
	            list.add(c);	
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	
	// 2. Đếm tổng số record (phục vụ cho việc phân trang)
    // =================================================================================================
    public int getTotalContracts(String searchKeyword, String status, String dateType, String fromDate, String toDate) {

        // Câu SQL đếm số lượng, Join với bảng customers để tìm theo tên khách hàng
        String sql = "SELECT COUNT(*) FROM contracts c " +
                     "LEFT JOIN customers cus ON c.customer_id = cus.id " +
                     "WHERE c.deleted_at IS NULL ";

        // Điều kiện 1: Tìm kiếm theo từ khóa
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            sql += "AND (c.contract_number LIKE ? OR cus.name LIKE ?) ";
        }

        // Điều kiện 2: Lọc theo tình trạng hợp đồng
        if (status != null && !status.isEmpty()) {
            sql += "AND c.status = ? ";
        }

        // Điều kiện 3 & 4: Lọc theo Loại ngày (Ngày tạo, Ngày BĐ, Ngày KT)
        // Kiểm tra whitelist để chống SQL Injection
        if (dateType != null && !dateType.isEmpty() && 
           (dateType.equals("created_at") || dateType.equals("start_date") || dateType.equals("end_date"))) {
            
            if (fromDate != null && !fromDate.isEmpty()) {
                sql += "AND DATE(c." + dateType + ") >= ? ";
            }
            if (toDate != null && !toDate.isEmpty()) {
                sql += "AND DATE(c." + dateType + ") <= ? ";
            }
        }

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            int paramIndex = 1;

            // Truyền giá trị cho tham số tìm kiếm
            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchKeyword + "%");
                ps.setString(paramIndex++, "%" + searchKeyword + "%");
            }

            // Truyền giá trị cho tham số tình trạng
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            // Truyền giá trị cho khoảng thời gian nếu dateType hợp lệ
            if (dateType != null && !dateType.isEmpty() && 
               (dateType.equals("created_at") || dateType.equals("start_date") || dateType.equals("end_date"))) {
                
                if (fromDate != null && !fromDate.isEmpty()) {
                    ps.setString(paramIndex++, fromDate);
                }
                if (toDate != null && !toDate.isEmpty()) {
                    ps.setString(paramIndex++, toDate);
                }
            }

            // Thực thi câu lệnh
            ResultSet rs = ps.executeQuery();

            // Nếu có kết quả -> trả về số lượng
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // Trả về 0 nếu có lỗi xảy ra
    }
    // 3. xóa nhiều (soft delete)
    // ===========================================================================================
    public void bulkDelete(String[] ids) {

        // Nếu không có id -> bỏ qua
        if (ids == null || ids.length == 0) return;

        // Tạo chuỗi ?,?,? tương ứng số lượng id
        String placeholders = String.join(",", java.util.Collections.nCopies(ids.length, "?"));

        // Soft delete: chỉ cập nhật deleted_at
        String sql = "UPDATE contracts SET deleted_at = NOW() WHERE id IN (" + placeholders + ")";
        
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            // Gán từng id vào dấu ?
            for (int i = 0; i < ids.length; i++) {
                ps.setLong(i + 1, Long.parseLong(ids[i]));
            }

            // Thực thi update
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 4. lấy ds nv (cho dropdown)
    // =====================================================================================
    public List<String[]> getSalesUsers() {

        List<String[]> users = new ArrayList<>();

        // Lấy user đang ACTIVE
        String sql = "SELECT id, full_name FROM users WHERE status = 'ACTIVE'";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                // Lưu dạng mảng [id, tên]
                users.add(new String[]{
                    rs.getString("id"),
                    rs.getString("full_name")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }


    // 5. gán người phụ trách hàng loạt
    // =================================================================================================
    public void bulkAssign(String[] ids, Long newOwnerId) {

        // Nếu thiếu dữ liệu -> bỏ qua
        if (ids == null || ids.length == 0 || newOwnerId == null) return;
        
        // Tạo ?,?,? tương ứng số id
        String placeholders = String.join(",", java.util.Collections.nCopies(ids.length, "?"));

        // Update owner_id
        String sql = "UPDATE contracts SET owner_id = ? WHERE id IN (" + placeholders + ")";
        
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            // Set owner mới
            ps.setLong(1, newOwnerId);

            // Set danh sách id phía sau
            for (int i = 0; i < ids.length; i++) {
                ps.setLong(i + 2, Long.parseLong(ids[i]));
            }

            // Thực thi
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 // 6. Tạo hợp đồng tự động từ Báo giá (Convert from Quote)
    // =================================================================================================
    public boolean createContractFromQuote(Long quoteId, Long currentUserId) {
        // Câu lệnh SQL: Lấy dữ liệu từ Quotes và tạo một bản ghi mới trong Contracts
        // Giả sử logic tạo mã hợp đồng: Lấy chữ "HD-" ghép với ID báo giá hoặc sinh ngẫu nhiên (ở đây dùng HD-QUOTE_ID)
        String sql = "INSERT INTO contracts (contract_number, customer_id, quote_id, contract_value, currency_code, status, owner_id, created_by) " +
                     "SELECT CONCAT('HD-Q', id, '-', UNIX_TIMESTAMP()), customer_id, id, total_amount, currency_code, 'DRAFT', created_by, ? " +
                     "FROM quotes WHERE id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            // Tham số 1: ID của user đang đăng nhập (người thực hiện thao tác tạo)
            ps.setLong(1, currentUserId);
            // Tham số 2: ID của Báo giá cần chuyển đổi
            ps.setLong(2, quoteId);

            // Thực thi câu lệnh, nếu số dòng ảnh hưởng > 0 tức là insert thành công
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
 // 7. Lấy danh sách Báo giá đã phê duyệt (để đổ vào Dropdown)
    // =================================================================================================
    public List<String[]> getApprovedQuotes() {
        List<String[]> quotes = new ArrayList<>();
        
        // Câu lệnh SQL lấy ID, Mã báo giá và Tên khách hàng để hiển thị cho trực quan
        String sql = "SELECT q.id, q.quote_number, cus.name AS customer_name " +
                     "FROM quotes q " +
                     "LEFT JOIN customers cus ON q.customer_id = cus.id " +
                     "WHERE q.deleted_at IS NULL " +
                     "ORDER BY q.created_at DESC"; 
                     // TODO: Thêm điều kiện AND q.status_id = [ID_TRẠNG_THÁI_APPROVED] vào đây

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                // Định dạng chuỗi hiển thị: Ví dụ "Q-2026-001 - Công ty Shopee"
                String quoteNumber = rs.getString("quote_number");
                String customerName = rs.getString("customer_name");
                String displayString = quoteNumber + " - " + (customerName != null ? customerName : "Khách lẻ");
                
                // Lưu vào mảng: phần tử 0 là value (ID), phần tử 1 là text hiển thị
                quotes.add(new String[]{
                    rs.getString("id"),
                    displayString
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quotes;
    }
 // 8. Lấy chi tiết 1 Hợp đồng theo ID
    // =================================================================================================
    public ContractDTO getContractById(Long id) {
        String sql = "SELECT c.id, c.contract_number, c.status, u.full_name as owner_name, c.owner_id, " +
                     "cus.name as customer_name, c.contract_value, c.start_date, c.end_date, c.created_at " +
                     "FROM contracts c " +
                     "LEFT JOIN users u ON c.owner_id = u.id " +
                     "LEFT JOIN customers cus ON c.customer_id = cus.id " +
                     "WHERE c.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                ContractDTO c = new ContractDTO();
                c.setId(rs.getLong("id"));
                c.setContractNumber(rs.getString("contract_number"));
                c.setStatus(rs.getString("status"));
                c.setOwnerName(rs.getString("owner_name"));
                c.setCustomerName(rs.getString("customer_name"));
                c.setContractValue(rs.getDouble("contract_value"));
                c.setStartDate(rs.getDate("start_date"));
                c.setEndDate(rs.getDate("end_date"));
                c.setCreatedAt(rs.getDate("created_at"));
                
               
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 9. Cập nhật thông tin Hợp đồng (Ngày tháng, Trạng thái, Người phụ trách)
    // =================================================================================================
    public boolean updateContractDetails(Long id, String status, String startDateStr, String endDateStr, Long ownerId) {
        String sql = "UPDATE contracts SET status = ?, start_date = ?, end_date = ?, owner_id = ?, updated_at = NOW() WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            
            // Xử lý Ngày bắt đầu (Nếu rỗng thì set NULL)
            if (startDateStr != null && !startDateStr.isEmpty()) {
                ps.setDate(2, java.sql.Date.valueOf(startDateStr));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
            
            // Xử lý Ngày kết thúc (Nếu rỗng thì set NULL)
            if (endDateStr != null && !endDateStr.isEmpty()) {
                ps.setDate(3, java.sql.Date.valueOf(endDateStr));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }
            
            ps.setLong(4, ownerId);
            ps.setLong(5, id);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
 // 10. Lấy danh sách các mẫu hợp đồng (Type = 'CONTRACT')
    // =================================================================================================
    public List<String[]> getContractTemplates() {
        List<String[]> templates = new ArrayList<>();
       
        String sql = "SELECT id, name FROM document_templates WHERE type = 'CONTRACT' AND is_active = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                templates.add(new String[]{rs.getString("id"), rs.getString("name")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return templates;
    }

    // 11. Lấy nội dung HTML của một mẫu cụ thể
    // =================================================================================================
    public String getTemplateHtmlById(Long templateId) {
        String sql = "SELECT content_html FROM document_templates WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, templateId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("content_html");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
 // Thêm mới một Mẫu tài liệu (Template)
    // =================================================================================================
    public boolean insertTemplate(String name, String contentHtml) {
        // Mặc định type là 'CONTRACT' theo như thiết kế của bạn
        String sql = "INSERT INTO document_templates (type, name, content_html, is_active) VALUES ('CONTRACT', ?, ?, 1)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, name);
            ps.setString(2, contentHtml); // Lưu toàn bộ chuỗi HTML được sinh ra từ trình soạn thảo
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}