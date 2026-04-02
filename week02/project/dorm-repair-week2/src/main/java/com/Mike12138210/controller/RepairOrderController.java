package com.Mike12138210.controller;

import com.Mike12138210.pojo.Result;
import com.Mike12138210.service.RepairOrderService;
import com.Mike12138210.service.UserService;
import com.Mike12138210.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class RepairOrderController {
    @Autowired
    private RepairOrderService repairOrderService;
    private UserService userService;

    @PutMapping("{id}/image")
    public Result uploadImage(@RequestHeader(name = "Authorization") String token,
                              @PathVariable Integer id,
                              @RequestParam ("image")MultipartFile file) throws IOException {
        Map<String, Object> claims = JwtUtil.parseToken(token);
        String account = (String)claims.get("account");
        String role = (String)claims.get("role");

        String imageUrl = repairOrderService.uploadImage(id,account,role,file);
        return Result.success(imageUrl);
    }

    @PostMapping("/uploadTemp")
    public Result<String>uploadTempImage(@RequestParam("image")MultipartFile file,
                                         @RequestHeader("Authorization")String token){
        Map<String,Object>map = JwtUtil.parseToken(token);
        String account = (String)map.get("account");
        try{
            String imageUrl = repairOrderService.saveImage(file, account);
            return Result.success(imageUrl);
        }catch(IOException e){
            return Result.error("图片上传失败");
        }
    }

    @PutMapping("/{id}/cancel")
    public Result cancelOrder(@PathVariable Integer id,
                              @RequestHeader("Authorization")String token){
        Map<String, Object>map = JwtUtil.parseToken(token);
        String account = (String)map.get("account");
        String role = (String)map.get("role");

        boolean  success = repairOrderService.cancelOrder(id,account,role);
        return success ? Result.success() : Result.error("取消失败");
    }
}