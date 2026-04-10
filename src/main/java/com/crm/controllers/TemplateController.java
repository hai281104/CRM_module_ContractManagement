package com.crm.controllers;

import com.crm.models.ContractDAO; 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/templates")
public class TemplateController extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ContractDAO dao = new ContractDAO();

    // Hiển thị form tạo mẫu mới
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            // Danh sách các biến (placeholders) để gợi ý cho người dùng
            String[] placeholders = {"{{contract_number}}", "{{customer_name}}", "{{contract_value}}", "{{start_date}}", "{{end_date}}"};
            request.setAttribute("placeholders", placeholders);
            
            request.getRequestDispatcher("/views/template_form.jsp").forward(request, response);
        } else {
            // TODO: Trả về trang danh sách các template hiện có
        }
    }

    // Nhận dữ liệu từ form và lưu vào DB
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Đảm bảo không lỗi font tiếng Việt khi lưu nội dung
        
        String name = request.getParameter("name");
        String contentHtml = request.getParameter("contentHtml");

        if (name != null && contentHtml != null) {
            dao.insertTemplate(name, contentHtml);
        }

        // Lưu xong thì quay lại trang danh sách (hoặc trang bạn muốn)
        response.sendRedirect(request.getContextPath() + "/contracts"); 
    }
}