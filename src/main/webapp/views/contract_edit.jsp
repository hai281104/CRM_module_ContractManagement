<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh sửa Hợp đồng - ${contract.contractNumber}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-4" style="max-width: 800px;">
    
    <a href="${pageContext.request.contextPath}/contracts" class="btn btn-outline-secondary mb-3">
        <i class="bi bi-arrow-left"></i> Quay lại danh sách
    </a>

    <div class="card shadow-sm">
        <div class="card-header bg-white">
            <h4 class="mb-0">Chi tiết Hợp đồng: <span class="text-primary">${contract.contractNumber}</span></h4>
        </div>
        <div class="card-body">
            
            <form action="${pageContext.request.contextPath}/contracts" method="POST">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${contract.id}">

                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label text-muted">Khách hàng</label>
                        <input type="text" class="form-control" value="${contract.customerName}" readonly>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label text-muted">Giá trị Hợp đồng</label>
                        <input type="text" class="form-control text-success fw-bold" 
                               value="<fmt:formatNumber value="${contract.contractValue}" type="currency" currencySymbol="₫"/>" readonly>
                    </div>
                </div>

                <hr class="my-4">

                <div class="row mb-3">
                    <div class="col-md-6">
                        <label class="form-label fw-bold">Trạng thái Hợp đồng</label>
                        <select name="status" class="form-select">
                            <option value="DRAFT" ${contract.status == 'DRAFT' ? 'selected' : ''}>Bản thảo (DRAFT)</option>
                            <option value="SIGNED" ${contract.status == 'SIGNED' ? 'selected' : ''}>Đã ký (SIGNED)</option>
                            <option value="ACTIVE" ${contract.status == 'ACTIVE' ? 'selected' : ''}>Đang hiệu lực (ACTIVE)</option>
                            <option value="COMPLETED" ${contract.status == 'COMPLETED' ? 'selected' : ''}>Hoàn thành (COMPLETED)</option>
                            <option value="CANCELLED" ${contract.status == 'CANCELLED' ? 'selected' : ''}>Đã hủy (CANCELLED)</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-bold">Người phụ trách</label>
                        <select name="ownerId" class="form-select">
                            <c:forEach var="u" items="${users}">
                                <option value="${u[0]}" ${contract.ownerName == u[1] ? 'selected' : ''}>${u[1]}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="row mb-4">
                    <div class="col-md-6">
                        <label class="form-label fw-bold">Ngày Bắt Đầu (Start Date)</label>
                        <input type="date" name="startDate" class="form-control" value="${contract.startDate}">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-bold">Ngày Kết Thúc (End Date)</label>
                        <input type="date" name="endDate" class="form-control" value="${contract.endDate}">
                    </div>
                </div>

                <div class="row mb-4">
				    <div class="col-md-6 offset-md-6">
				        <label class="form-label fw-bold">Chọn Mẫu In Hợp Đồng</label>
				        <select id="templateSelector" class="form-select form-select-sm">
				            <option value="">-- Chọn mẫu hợp đồng --</option>
				            <c:forEach var="t" items="${templates}">
				                <option value="${t[0]}">${t[1]}</option>
				            </c:forEach>
				        </select>
				    </div>
				</div>
				
				<div class="d-flex justify-content-end gap-2">
				    <a href="#" id="exportPdfBtn" class="btn btn-danger disabled">
				        <i class="bi bi-file-earmark-pdf"></i> Xuất PDF
				    </a>
				    
				    <button type="submit" class="btn btn-primary">
				        <i class="bi bi-save"></i> Lưu Thay Đổi
				    </button>
				</div>
				
				
            </form>

        </div>
    </div>
</div>
<script>
    // JavaScript để cập nhật Link xuất PDF động khi chọn mẫu
    const selector = document.getElementById('templateSelector');
    const btn = document.getElementById('exportPdfBtn');
    const contractId = "${contract.id}";
    const contextPath = "${pageContext.request.contextPath}";

    selector.addEventListener('change', function() {
        const templateId = this.value;
        if (templateId) {
            // Cập nhật href và kích hoạt nút
            btn.href = contextPath + "/contracts?action=export_pdf&id=" + contractId + "&templateId=" + templateId;
            btn.classList.remove('disabled');
        } else {
            // Vô hiệu hóa nút nếu chưa chọn mẫu
            btn.href = "#";
            btn.classList.add('disabled');
        }
    });
</script>
</body>
</html>