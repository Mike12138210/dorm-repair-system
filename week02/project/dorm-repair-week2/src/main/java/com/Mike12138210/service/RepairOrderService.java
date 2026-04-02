package com.Mike12138210.service;

import com.Mike12138210.pojo.RepairOrder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RepairOrderService {
    // 根据id查询报修单
    RepairOrder selectById(Integer id);

    // 查询该账号所有报修单
    List<RepairOrder> selectByAccount(String account);

    // 根据状态查询报修单列表
    List<RepairOrder> selectByStatus(String status);

    // 插入新的报修单
    boolean insertOrder(RepairOrder order);

    // 创建报修单时，上传图片
    String saveImage(MultipartFile image, String account) throws IOException;

    // 更新图片
    String uploadImage(Integer id, String account, String role, MultipartFile file) throws IOException;

    // 取消报修单
    boolean cancelOrder(Integer id,String account,String role);

    // 更新报修单
    boolean updateOrder(Integer id,String status);

    // 删除报修单
    boolean deleteOrder(Integer id);
}