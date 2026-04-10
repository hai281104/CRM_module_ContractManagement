<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tạo Mẫu Hợp Đồng Mới</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/classic/ckeditor.js"></script>
    
    <style>
        /* Tăng chiều cao mặc định cho khung soạn thảo */
        .ck-editor__editable_inline {
            min-height: 400px;
        }
    </style>
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="card shadow-sm">
        <div class="card-header bg-white">
            <h4 class="mb-0">Thiết Kế Mẫu Hợp Đồng</h4>
        </div>
        <div class="card-body">
            
            <form action="${pageContext.request.contextPath}/templates" method="POST">
                
                <div class="mb-3">
                    <label class="form-label fw-bold">Tên Mẫu Hợp Đồng <span class="text-danger">*</span></label>
                    <input type="text" name="name" class="form-control" placeholder="VD: Hợp đồng Dịch vụ B2B (Mẫu chuẩn)" required>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Nội dung Hợp đồng <span class="text-danger">*</span></label>
                    
                    <div class="alert alert-info py-2" role="alert">
                        <strong>Lưu ý:</strong> Sử dụng các biến sau trong nội dung để hệ thống tự điền thông tin khi xuất file:
                        <c:forEach var="ph" items="${placeholders}">
                            <span class="badge bg-secondary ms-1">${ph}</span>
                        </c:forEach>
                    </div>

                    <textarea name="contentHtml" id="editor"></textarea>
                </div>

                <div class="d-flex justify-content-end gap-2">
                    <a href="${pageContext.request.contextPath}/contracts" class="btn btn-outline-secondary">Hủy bỏ</a>
                    <button type="submit" class="btn btn-primary">Lưu Mẫu</button>
                </div>
            </form>

        </div>
    </div>
</div>

<script>
    // Khởi tạo CKEditor
    ClassicEditor
        .create( document.querySelector( '#editor' ), {
            toolbar: [ 'heading', '|', 'bold', 'italic', 'underline', '|', 'bulletedList', 'numberedList', '|', 'insertTable', 'blockQuote', '|', 'undo', 'redo' ]
        } )
        .catch( error => {
            console.error( error );
        } );
</script>
</body>
</html>