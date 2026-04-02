package com.Mike12138210.controller;

import com.Mike12138210.pojo.RepairOrder;
import com.Mike12138210.pojo.Result;
import com.Mike12138210.service.RepairOrderService;
import com.Mike12138210.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private RepairOrderService repairOrderService;

    private void checkAdmin(){
        Map<String, Object>map = ThreadLocalUtil.get();
        String role = (String)map.get("role");
        if(!"admin".equals(role)){
            throw new RuntimeException("无权限，仅管理员可操作");
        }
    }

    @GetMapping("/repairs")
    public Result<List<RepairOrder>>selectAllRepairs(@RequestParam(required = false) String status){
        checkAdmin();
        List<RepairOrder> list = repairOrderService.selectByStatus(status);
        return Result.success(list);
    }

    @GetMapping("/repair/{id}")
    public Result<RepairOrder> getRepairDetail(@PathVariable Integer id){
        checkAdmin();
        RepairOrder order = repairOrderService.selectById(id);
        if(order == null){
            return Result.error("报修单不存在");
        }
        return Result.success(order);
    }

    @PutMapping("/repair/{id}/status")
    public Result updateStatus(@PathVariable Integer id,@RequestBody Map<String, String> body){
        checkAdmin();
        String status = body.get("status");
        if (status == null || status.isEmpty()){
            return Result.error("状态不能为空。");
        }

        boolean success = repairOrderService.updateOrder(id,status);
        return success ? Result.success() : Result.error("更新失败，请稍后重试。");
    }

    @DeleteMapping("/repair/{id}")
    public Result deleteOrder(@PathVariable Integer id){
        checkAdmin();
        boolean success = repairOrderService.deleteOrder(id);
        return success ? Result.success() : Result.error("删除失败，请稍后重试。");
    }
}