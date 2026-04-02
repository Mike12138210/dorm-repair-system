package com.Mike12138210.pojo;

import java.time.LocalDateTime;

public class RepairOrder {
    // id,account,deviceType,description,status,createTime,updateTime
    private Integer id;
    private String account;
    private String deviceType;
    private String description;
    private String status;           // 等待中, 进行中, 已完成, 已取消
    private String imageUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public RepairOrder(){}

    public RepairOrder(String account, String deviceType, String description, String status) {
        this.account = account;
        this.deviceType = deviceType;
        this.description = description;
        this.status = status;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getAccount() {return account;}

    public void setAccount(String account) {this.account = account;}

    public String getDeviceType() {return deviceType;}

    public void setDeviceType(String deviceType) {this.deviceType = deviceType;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public LocalDateTime getCreateTime() {return createTime;}

    public void setCreateTime(LocalDateTime createTime) {this.createTime = createTime;}

    public LocalDateTime getUpdateTime() {return updateTime;}

    public void setUpdateTime(LocalDateTime updateTime) {this.updateTime = updateTime;}

    public String getImageUrl() {return imageUrl;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}
}