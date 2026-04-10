package com.crm.models;

import java.sql.Date;
//DTO là viết tắt của Data Transfer Object (Đối tượng truyền tải dữ liệu).
public class ContractDTO {
	private Long id;
    private String contractNumber;
    private String status;
    private String ownerName; // Giao cho
    private String customerName; // Account
    private Double contractValue;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Double getContractValue() {
		return contractValue;
	}
	public void setContractValue(Double contractValue) {
		this.contractValue = contractValue;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public ContractDTO() {
		super();
	}
    
    
}
