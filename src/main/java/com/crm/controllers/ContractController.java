package com.crm.controllers;

// Import DAO để xử lý DB
import com.crm.models.ContractDAO;

// Import DTO để chứa dữ liệu contract
import com.crm.models.ContractDTO;

// Thư viện Servlet
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

// Thư viện xử lý IO
import java.io.IOException;

// List để chứa dữ liệu
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.OutputStream;

import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
// Mapping URL: khi truy cập /contracts sẽ chạy class này
@WebServlet("/contracts")
public class ContractController extends HttpServlet {

    // ID mặc định của Serializable (không quan trọng lắm, để tránh warning)
    private static final long serialVersionUID = 1L;

    // Tạo đối tượng DAO để gọi DB
    private ContractDAO contractDAO = new ContractDAO();

    
    // 1. HANDLE GET REQUEST (load trang lên)
    // =================================================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	String action = request.getParameter("action");
    	// ======= LUỒNG: XUẤT FILE PDF HOÀN CHỈNH =======
    	if ("export_pdf".equals(action)) {
    	    try {
    	        Long id = Long.parseLong(request.getParameter("id"));
    	        Long templateId = Long.parseLong(request.getParameter("templateId"));

    	        ContractDTO contract = contractDAO.getContractById(id);
    	        String templateHtml = contractDAO.getTemplateHtmlById(templateId);

    	        // 1. Thay thế dữ liệu
    	        String replacedHtml = templateHtml
    	            .replace("{{contract_number}}", contract.getContractNumber() != null ? contract.getContractNumber() : "")
    	            .replace("{{customer_name}}", contract.getCustomerName() != null ? contract.getCustomerName() : "")
    	            .replace("{{contract_value}}", contract.getContractValue() != null ? String.format("%,.0f", contract.getContractValue()) : "0")
    	            .replace("{{start_date}}", contract.getStartDate() != null ? contract.getStartDate().toString() : "___/___/____")
    	            .replace("{{end_date}}", contract.getEndDate() != null ? contract.getEndDate().toString() : "___/___/____");

    	        // 2. Bọc HTML chuẩn và chỉ định font-family là 'TimesNewRoman'
    	        String rawHtml = "<!DOCTYPE html>\n" +
    	                 "<html>\n" +
    	                 "<head>\n" +
    	                 "    <meta charset='UTF-8'/>\n" +
    	                 "    <style>\n" +
    	                 "        body { font-family: 'TimesNewRoman', serif; font-size: 14px; }\n" +
    	                 "    </style>\n" +
    	                 "</head>\n" +
    	                 "<body>\n" +
    	                 replacedHtml +
    	                 "\n</body>\n" +
    	                 "</html>";

    	        // 3. Dùng Jsoup chuẩn hóa XML
    	        Document doc = Jsoup.parse(rawHtml, "UTF-8");
    	        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
    	        String xhtml = doc.html();

    	        // 4. Chuẩn bị Response
    	        response.reset();
    	        response.setContentType("application/pdf");
    	        response.setHeader("Content-Disposition", "attachment; filename=\"HopDong_" + contract.getContractNumber() + ".pdf\"");

    	        // 5. Build PDF với Font Unicode
    	        try (OutputStream os = response.getOutputStream()) {
    	            PdfRendererBuilder builder = new PdfRendererBuilder();
    	            builder.useFastMode();
    	            
    	            // Đường dẫn gốc đến thư mục fonts
    	            String fontFolder = getServletContext().getRealPath("/fonts/");
    	            
    	            // 1. Nhúng Font thường (Regular)
    	            builder.useFont(new java.io.File(fontFolder + "TIMES.TTF"), "TimesNewRoman");
    	            
    	            // 2. Nhúng Font in đậm (Bold)
    	            builder.useFont(new java.io.File(fontFolder + "TIMESBD.TTF"), "TimesNewRoman", 700, BaseRendererBuilder.FontStyle.NORMAL, true);
    	            
    	            // 3. Nhúng Font in nghiêng (Italic)
    	            builder.useFont(new java.io.File(fontFolder + "TIMESI.TTF"), "TimesNewRoman", 400, BaseRendererBuilder.FontStyle.ITALIC, true);
    	            
    	            // 4. Nhúng Font vừa đậm vừa nghiêng (Bold Italic)
    	            builder.useFont(new java.io.File(fontFolder + "TIMESBI.TTF"), "TimesNewRoman", 700, BaseRendererBuilder.FontStyle.ITALIC, true);
    	            
    	            builder.withHtmlContent(xhtml, "");
    	            builder.toStream(os);
    	            builder.run();
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	    return;
    	}
        // ======= LUỒNG: HIỂN THỊ TRANG CHỈNH SỬA =======
        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                Long id = Long.parseLong(idStr);
                ContractDTO contract = contractDAO.getContractById(id);
                
                request.setAttribute("contract", contract);
                request.setAttribute("users", contractDAO.getSalesUsers()); // Danh sách nv để đổi người phụ trách
                
             // Lấy danh sách mẫu hợp đồng truyền sang JSP
                request.setAttribute("templates", contractDAO.getContractTemplates());
               
                
                request.getRequestDispatcher("/views/contract_edit.jsp").forward(request, response);
                return; // KẾT THÚC, KHÔNG chạy xuống phần load danh sách nữa
            }
        }

    	String search = request.getParameter("search");
    	String status = request.getParameter("status"); 

    	// Bổ sung lấy loại ngày và khoảng thời gian
    	String dateType = request.getParameter("dateType"); // "created_at", "start_date", hoặc "end_date"
    	String fromDate = request.getParameter("fromDate"); 
    	String toDate = request.getParameter("toDate");     

    	int page = 1;
    	int pageSize = 10;
    	if (request.getParameter("page") != null) {
    	    page = Integer.parseInt(request.getParameter("page"));
    	}

    	// Truyền thêm dateType, fromDate, toDate vào DAO
    	List<ContractDTO> contracts = contractDAO.getContracts(page, pageSize, search, status, dateType, fromDate, toDate);
    	int totalRecords = contractDAO.getTotalContracts(search, status, dateType, fromDate, toDate);

    	int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

    	// ... (các setAttribute cũ giữ nguyên)
    	request.setAttribute("contracts", contracts);
    	request.setAttribute("currentPage", page);
    	request.setAttribute("totalPages", totalPages);
    	request.setAttribute("searchKeyword", search);
    	request.setAttribute("statusFilter", status); 

    	// Giữ lại giá trị thời gian trên UI
    	request.setAttribute("dateTypeFilter", dateType); 
    	request.setAttribute("fromDateFilter", fromDate); 
    	request.setAttribute("toDateFilter", toDate);     

    	

    	// Lấy danh sách nhân viên (để hiển thị dropdown assign)
    	List<String[]> users = contractDAO.getSalesUsers();
    	request.setAttribute("users", users);

    	// THÊM MỚI: Lấy danh sách Báo giá đổ vào Modal
    	List<String[]> approvedQuotes = contractDAO.getApprovedQuotes();
    	request.setAttribute("approvedQuotes", approvedQuotes);

    	// Forward sang JSP để render giao diện
    	request.getRequestDispatcher("/views/contract_list.jsp").forward(request, response);
    }

    
 // 2. HANDLE POST REQUEST (xử lý các hành động actions)
    // =========================================================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy các tham số hành động từ giao diện
        // action: dùng cho các form độc lập (như Modal Convert Quote)
        // bulkAction: dùng cho form xử lý hàng loạt trên danh sách
        String action = request.getParameter("action"); 
        String bulkAction = request.getParameter("bulkAction");

        // ====================================================================
        // LUỒNG 1: XỬ LÝ CHUYỂN ĐỔI BÁO GIÁ THÀNH HỢP ĐỒNG (Từ Modal)
        // ====================================================================
        if ("convert_quote".equals(action)) {
            String quoteIdStr = request.getParameter("quoteId");
            
            if (quoteIdStr != null && !quoteIdStr.isEmpty()) {
                Long quoteId = Long.parseLong(quoteIdStr);
                
                // TODO: Trong thực tế, bạn sẽ lấy ID của user đang đăng nhập từ Session
                // Ví dụ: User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
                // Long currentUserId = loginUser.getId();
                Long currentUserId = 1L; // Tạm thời gán cứng ID = 1 để test

                // Gọi DAO để thực hiện chuyển đổi
                contractDAO.createContractFromQuote(quoteId, currentUserId);
            }
            
            // Xử lý xong thì tải lại trang và KẾT THÚC HÀM
            response.sendRedirect(request.getContextPath() + "/contracts");
            return; 
        }


        // ====================================================================
        // LUỒNG 2: XỬ LÝ THAO TÁC HÀNG LOẠT (Xóa, Giao phụ trách)
        // ====================================================================
        String[] selectedIds = request.getParameterValues("selectedIds");

        // Nếu người dùng có tick chọn ít nhất 1 dòng trong bảng
        if (selectedIds != null && selectedIds.length > 0) {

            // ====== ACTION: DELETE ======
            if ("delete".equals(bulkAction)) {
                // Gọi DAO để soft delete
                contractDAO.bulkDelete(selectedIds);

            // ====== ACTION: ASSIGN ======
            } else if ("assign".equals(bulkAction)) {
                // Lấy ID nhân viên được chọn từ dropdown
                String assigneeIdStr = request.getParameter("assigneeId");

                // Kiểm tra xem đã chọn nhân viên chưa
                if (assigneeIdStr != null && !assigneeIdStr.isEmpty()) {
                    Long assigneeId = Long.parseLong(assigneeIdStr);
                    // Gọi DAO để update người phụ trách hàng loạt
                    contractDAO.bulkAssign(selectedIds, assigneeId);
                }
            }
        }
        // ======= LUỒNG: LƯU CẬP NHẬT HỢP ĐỒNG =======
        if ("update".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            String status = request.getParameter("status");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            Long ownerId = Long.parseLong(request.getParameter("ownerId"));

            // Gọi hàm DAO để lưu xuống DB
            contractDAO.updateContractDetails(id, status, startDate, endDate, ownerId);
            
            // Cập nhật xong thì quay về trang danh sách
            response.sendRedirect(request.getContextPath() + "/contracts");
            return;
        }
        // Sau khi xử lý xong tất cả -> redirect về lại trang danh sách
        // (Sử dụng sendRedirect để tránh lỗi submit lại form khi người dùng ấn F5)
        response.sendRedirect(request.getContextPath() + "/contracts");
    }
}