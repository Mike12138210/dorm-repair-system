package com.Mike12138210.controller;

import com.Mike12138210.pojo.RepairOrder;
import com.Mike12138210.pojo.Result;
import com.Mike12138210.pojo.User;
import com.Mike12138210.service.RepairOrderService;
import com.Mike12138210.service.UserService;
import com.Mike12138210.utils.JwtUtil;
import com.Mike12138210.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RepairOrderService repairOrderService;

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        boolean result = userService.register(user);
        if(result) {
            return Result.success();
        }else {
            return Result.error("注册失败");
        }
    }

    @GetMapping("/selectByAccount/{account}")
    public User selectByAccount(@PathVariable String account){
        return userService.selectByAccount(account);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody User user){
        User loginUser = userService.selectByAccount(user.getAccount());

        if(loginUser == null){
            return Result.error("用户名错误");
        }

        if(user.getPassword().equals(loginUser.getPassword())){
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",loginUser.getId());
            claims.put("account",loginUser.getAccount());
            claims.put("role",loginUser.getRole());

            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        }

        return Result.error("密码错误");
    }

    @PutMapping("/dorm")
    public Result bindDorm(@RequestBody User user){
        Map<String, Object> map = ThreadLocalUtil.get();
        String account = (String) map.get("account");

        User currentUser = userService.selectByAccount(account);
        if(currentUser == null){
            return Result.error("用户不存在");
        }

        String newBuilding = user.getBuilding();
        String newRoom = user.getRoom();
        String currentBuilding = currentUser.getBuilding();
        String currentRoom = currentUser.getRoom();

        if((newBuilding == null && currentBuilding == null) || (newBuilding != null && newBuilding.equals(currentBuilding))){
            if((newRoom == null && currentRoom == null) || (newRoom != null && newRoom.equals(currentRoom))){
                return Result.error("输入内容与当前宿舍信息一致，修改失败。");
            }
        }

        boolean success = userService.updateDorm(account,user.getBuilding(),user.getRoom());
        if (success) {
            return Result.success();
        }else{
            return Result.error("修改失败，请稍后重试。");
        }
    }

    @GetMapping("/repairs")
    public Result<List<RepairOrder>> orderInfo(){
        // 根据学号获取id，并由id查询报修单
        Map<String, Object> map = ThreadLocalUtil.get();
        String account = (String) map.get("account");

        if(account == null){
            return Result.error("用户不存在");
        }

        List<RepairOrder> orders = repairOrderService.selectByAccount(account);
        return Result.success(orders);
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params){
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的参数。");
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        String account = (String)map.get("account");
        User loginUser = userService.selectByAccount(account);

        if (loginUser == null) {
            return Result.error("用户不存在");
        }
        if (!loginUser.getPassword().equals(oldPwd)) {
            return Result.error("原密码填写错误。");
        }
        if(!rePwd.equals(newPwd)){
            return Result.error("两次填写的新密码不一致，请重新输入。");
        }

        boolean success = userService.updatePwd(newPwd);
        return success ? Result.success() : Result.error("密码修改失败，请稍后重试。");
    }

    @PostMapping("/repair")
    public Result insertOrder(@RequestBody RepairOrder order,
                              @RequestHeader("Authorization") String token,
                              @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        String account = (String)map.get("account");

        if(image != null && !image.isEmpty()){
            String imageUrl = repairOrderService.saveImage(image,account);
            order.setImageUrl(imageUrl);
        }

        boolean success = repairOrderService.insertOrder(order);
        return success ? Result.success() : Result.error("报修单创建失败");
    }
}