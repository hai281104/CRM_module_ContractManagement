<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Hợp đồng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
   
</head>
<body class="bg-light">
<div class="container-fluid mt-4">
    <div class="card shadow-sm">
	     <div class="card-header bg-white d-flex justify-content-between align-items-center">
		    <h4 class="mb-0">Danh sách Hợp đồng</h4>
		    
		    <div class="d-flex gap-2">
		        <a href="${pageContext.request.contextPath}/templates?action=create" class="btn btn-outline-primary btn-sm">
		            <i class="bi bi-plus-circle"></i> Tạo mẫu hợp đồng mới
		        </a>
		
		        <button type="button" class="btn btn-success btn-sm" data-bs-toggle="modal" data-bs-target="#convertQuoteModal">
		            <i class="bi bi-file-earmark-plus"></i> Tạo từ Báo giá
		        </button>
		    </div>
		</div>
        <div class="card-body">
            
            <form action="${pageContext.request.contextPath}/contracts" method="POST" id="mainForm">
            
                <div class="row mb-3 align-items-center">
                    <div class="col-lg-3 col-md-4 mb-2 mb-md-0"> 
                        <div class="input-group input-group-sm">
                            <select name="bulkAction" id="bulkAction" class="form-select" onchange="toggleAssignee()">
                                <option value="">Hành động...</option>
                                <option value="assign">Giao phụ trách</option>
                                <option value="delete">Xóa bỏ</option>
                            </select>
                
                            <select name="assigneeId" id="assigneeDropdown" class="form-select" style="display: none;">
                                <option value="">-- Chọn nhân viên --</option>
                                <c:forEach var="u" items="${users}">
                                    <option value="${u[0]}">${u[1]}</option>
                                </c:forEach>
                            </select>
                
                            <button type="submit" class="btn btn-outline-secondary" onclick="return validateBulkAction();">Áp dụng</button>
                        </div>
                    </div>
                
						<div class="col-lg-9 col-md-8 d-flex justify-content-end">
						    <div class="d-flex gap-2 flex-wrap justify-content-end align-items-center">
						        
						        <select name="dateType" form="searchForm" class="form-select form-select-sm" style="width: auto;">
						            <option value="created_at" ${dateTypeFilter == 'created_at' || empty dateTypeFilter ? 'selected' : ''}>Ngày tạo</option>
						            <option value="start_date" ${dateTypeFilter == 'start_date' ? 'selected' : ''}>Ngày bắt đầu</option>
						            <option value="end_date" ${dateTypeFilter == 'end_date' ? 'selected' : ''}>Ngày kết thúc</option>
						        </select>
						
						        <div class="input-group input-group-sm" style="width: auto;">
						            <span class="input-group-text text-muted">Từ</span>
						            <input type="date" name="fromDate" form="searchForm" class="form-control" value="${fromDateFilter}">
						        </div>
						
						        <div class="input-group input-group-sm" style="width: auto;">
						            <span class="input-group-text text-muted">Đến</span>
						            <input type="date" name="toDate" form="searchForm" class="form-control" value="${toDateFilter}">
						        </div>
						
						        <select name="status" form="searchForm" class="form-select form-select-sm" style="width: auto; min-width: 130px;">
						            <option value="">Tất cả trạng thái</option>
						            <option value="DRAFT" ${statusFilter == 'DRAFT' ? 'selected' : ''}>DRAFT</option>

									<option value="SIGNED" ${statusFilter == 'SIGNED' ? 'selected' : ''}>SIGNED</option>
									
									<option value="ACTIVE" ${statusFilter == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
									
									<option value="COMPLETED" ${statusFilter == 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
									
									<option value="CANCELLED" ${statusFilter == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
						            </select>


						            
						
						        <input type="text" name="search" form="searchForm" class="form-control form-control-sm" placeholder="Mã HĐ, Tên KH..." value="${searchKeyword}" style="max-width: 140px;">

								<div class="btn-group" role="group">
								    <button type="submit" form="searchForm" class="btn btn-primary btn-sm text-nowrap">
								        <i class="bi bi-funnel-fill"></i> Lọc
								    </button>
								    
								    <a href="${pageContext.request.contextPath}/contracts" class="btn btn-outline-secondary btn-sm text-nowrap" title="Hủy tất cả bộ lọc">
								        <i class="bi bi-x-circle"></i> Hủy
								    </a>
								</div>
						    </div>
						</div>
                </div>
                             
                <div class="table-responsive">
                    <table class="table table-hover table-bordered align-middle">
                        <thead class="table-light">
                            <tr>
                                <th style="width: 40px;"><input type="checkbox" id="selectAll"></th>
                                <th>Mã Hợp đồng</th>
                                <th>Khách hàng</th>
                                <th>Tình trạng</th>
                                <th>Giao cho</th>
                                <th>Giá trị</th>
                                <th>Ngày BĐ</th>
                                <th>Ngày KT</th>
                                <th>Ngày tạo</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${contracts}">
                                <tr>
                                    <td><input type="checkbox" name="selectedIds" value="${item.id}" class="row-checkbox"></td>
                                    <td>
	                                    <strong><a href="${pageContext.request.contextPath}/contracts?action=edit&id=${item.id}" class="text-decoration-none fw-bold">
	      									  ${item.contractNumber}</a>
	      								</strong>
   									</td>
                                    
                                    <td>${item.customerName}</td>
                                    <td><span class="badge bg-info">${item.status}</span></td>
                                    <td>${item.ownerName}</td>
                                    <td><fmt:formatNumber value="${item.contractValue}" type="currency" currencySymbol="₫"/></td>
                                    <td>${item.startDate}</td>
                                    <td>${item.endDate}</td>
                                    <td>${item.createdAt}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty contracts}">
                                <tr><td colspan="9" class="text-center text-muted">Không tìm thấy dữ liệu.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </form>

            <form id="searchForm" action="${pageContext.request.contextPath}/contracts" method="GET"></form>

           



			<div class="card-footer bg-white border-top-0 pt-3">
			    <div class="d-flex justify-content-between align-items-center">
			        <div class="text-muted small">
			            Hiển thị trang ${currentPage} / ${totalPages}
			        </div>
			        <nav aria-label="Page navigation">
			            <ul class="pagination pagination-sm mb-0">
			                <%-- Nút Previous --%>
			                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
			                    <a class="page-link" href="?page=${currentPage - 1}&search=${searchKeyword}&status=${statusFilter}&dateType=${dateTypeFilter}&fromDate=${fromDateFilter}&toDate=${toDateFilter}">
			                        <i class="bi bi-chevron-left"></i>
			                    </a>
			                </li>
			
			                <c:forEach begin="1" end="${totalPages}" var="i">
			                    <li class="page-item ${currentPage == i ? 'active' : ''}">
			                        <a class="page-link" href="?page=${i}&search=${searchKeyword}&status=${statusFilter}&dateType=${dateTypeFilter}&fromDate=${fromDateFilter}&toDate=${toDateFilter}">${i}</a>
			                    </li>
			                </c:forEach>
			
			                <%-- Nút Next --%>
			                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
			                    <a class="page-link" href="?page=${currentPage + 1}&search=${searchKeyword}&status=${statusFilter}&dateType=${dateTypeFilter}&fromDate=${fromDateFilter}&toDate=${toDateFilter}">
			                        <i class="bi bi-chevron-right"></i>
			                    </a>
			                </li>
			            </ul>
			        </nav>
			    </div>
			</div>




            
        </div>
    </div>
</div>

<script>
//Xử lý check all
document.getElementById('selectAll').addEventListener('change', function() {
    let checkboxes = document.querySelectorAll('.row-checkbox');
    checkboxes.forEach(cb => cb.checked = this.checked);
});

// Ẩn hiện ô chọn nhân viên dựa vào Hành động
function toggleAssignee() {
    var action = document.getElementById("bulkAction").value;
    var assigneeDropdown = document.getElementById("assigneeDropdown");
    if (action === 'assign') {
        assigneeDropdown.style.display = 'block'; // Hiện
    } else {
        assigneeDropdown.style.display = 'none';  // Ẩn
        assigneeDropdown.value = ''; // Reset giá trị
    }
}

// Kiểm tra dữ liệu trước khi Submit
function validateBulkAction() {
    var action = document.getElementById("bulkAction").value;
    if (action === '') {
        alert('Vui lòng chọn một hành động!');
        return false;
    }
    
    let checkedCount = document.querySelectorAll('.row-checkbox:checked').length;
    if (checkedCount === 0) {
        alert('Vui lòng chọn ít nhất 1 hợp đồng để thao tác!');
        return false;
    }

    if (action === 'assign') {
        var assigneeId = document.getElementById("assigneeDropdown").value;
        if (assigneeId === '') {
            alert('Vui lòng chọn nhân viên để giao phụ trách!');
            return false;
        }
    }
    return confirm('Bạn có chắc chắn muốn thực hiện thao tác này cho ' + checkedCount + ' hợp đồng?');
}
</script>

<div class="modal fade" id="convertQuoteModal" tabindex="-1" aria-labelledby="convertQuoteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/contracts" method="POST">
                <input type="hidden" name="action" value="convert_quote">
                
                <div class="modal-header">
                    <h5 class="modal-title" id="convertQuoteModalLabel">Tạo Hợp đồng từ Báo giá</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                
                <div class="modal-body">
				    <div class="mb-3">
				        <label for="quoteId" class="form-label">Chọn Báo giá (Quote) <span class="text-danger">*</span></label>
				        
				        <select class="form-select" id="quoteId" name="quoteId" required>
				            <option value="">-- Chọn Báo giá cần chuyển đổi --</option>
				            <c:forEach var="quote" items="${approvedQuotes}">
				                <option value="${quote[0]}">${quote[1]}</option>
				            </c:forEach>
				        </select>
				        
				        <div class="form-text">Hệ thống sẽ lấy dữ liệu từ Báo giá đã chọn để tạo bản thảo Hợp đồng mới.</div>
				    </div>
				</div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-success">Thực hiện Chuyển đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>