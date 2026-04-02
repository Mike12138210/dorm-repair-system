package com.Mike12138210.service.impl;

import com.Mike12138210.mapper.RepairOrderMapper;
import com.Mike12138210.pojo.RepairOrder;
import com.Mike12138210.service.RepairOrderService;
import com.Mike12138210.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class RepairOrderServiceImpl implements RepairOrderService {
    @Autowired
    private RepairOrderMapper repairOrderMapper;

    @Value("${upload.path}")
    private String uploadPath;

    private String saveFile(MultipartFile file, String prefix) throws IOException {
        String filename = prefix + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dest = new File(uploadPath, filename);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);
        return filename;
    }

    @Override
    public RepairOrder selectById(Integer id) {
        return repairOrderMapper.selectById(id);
    }

    @Override
    public List<RepairOrder> selectByAccount(String account){
        return repairOrderMapper.selectByAccount(account);
    }

    @Override
    public List<RepairOrder> selectByStatus(String status) {
        if(status == null || status.isEmpty()){
            return repairOrderMapper.selectAll();
        }else{
            return repairOrderMapper.selectByStatus(status);
        }
    }

    @Override
    public boolean insertOrder(RepairOrder order) {
        Map<String, Object>map = ThreadLocalUtil.get();
        String account = (String)map.get("account");

        order.setAccount(account);
        order.setStatus("待处理");
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        int rows = repairOrderMapper.insertOrder(order);
        return rows > 0;
    }

    @Override
    public String saveImage(MultipartFile file, String account) throws IOException {
        String filename = saveFile(file, account);
        return "/uploads/repair_images/" + filename;
    }

    @Override
    public String uploadImage(Integer oldId, String account, String role, MultipartFile file) throws IOException {
        RepairOrder order = repairOrderMapper.selectById(oldId);
        if(order == null){
            throw new RuntimeException("报修单不存在，请稍后重试。");
        }

        if("student".equals(role) && !order.getAccount().equals(account)){
            throw new RuntimeException("不是你的报修单，无法修改。");
        }

        // 保存文件，文件名前缀使用 oldId
        String filename = saveFile(file,oldId.toString());
        String imageUrl = "/uploads/repair_images/" + filename;

        // 更新数据库
        int rows = repairOrderMapper.uploadImageUrl(oldId,imageUrl);
        if(rows != 1){
            throw new RuntimeException("更新图片地址失败。");
        }
        return imageUrl;
    }

    @Override
    public boolean cancelOrder(Integer id, String account, String role) {
        RepairOrder order = repairOrderMapper.selectById(id);
        if(order == null){
            throw new RuntimeException("报修单不存在。");
        }

        if("student".equals(role) && !order.getAccount().equals(account)){
            throw new RuntimeException("不能取消他人的报修单。");
        }

        if("已取消".equals(order.getStatus()) || "已完成".equals(order.getStatus())){
            throw new RuntimeException("该报修单已取消。");
        }

        int rows = repairOrderMapper.updateOrder(id,"已取消",LocalDateTime.now());
        return rows > 0;
    }

    @Override
    public boolean updateOrder(Integer id, String status) {
        int rows = repairOrderMapper.updateOrder(id,status,LocalDateTime.now());
        return rows > 0;
    }

    @Override
    public boolean deleteOrder(Integer id) {
        int rows = repairOrderMapper.deleteOrder(id);
        return rows > 0;
    }
}