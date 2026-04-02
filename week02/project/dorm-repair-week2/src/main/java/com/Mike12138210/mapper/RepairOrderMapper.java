package com.Mike12138210.mapper;

import com.Mike12138210.pojo.RepairOrder;

import org.apache.ibatis.annotations.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public interface RepairOrderMapper {
    // 插入新的报修单
    @Insert("INSERT INTO repair_order(id,device_type,description,status,create_time,update_time,account,image_url)"+
    "VALUES(#{id},#{deviceType},#{description},#{status},#{createTime},#{updateTime},#{account},#{imageUrl})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertOrder(RepairOrder order);

    // 根据学生ID查询该学生的所有报修单
    @Select("SELECT id,account,device_type,description,status,create_time,update_time,image_url FROM repair_order WHERE account = #{account} ORDER BY create_time DESC")
    List<RepairOrder> selectByAccount(String account);

    // 根据报修单ID查询单个报修单
    @Select("SELECT id,device_type,description,status,create_time,update_time,account,image_url FROM repair_order WHERE id = #{id}")
    RepairOrder selectById(Integer id);

    // 更新报修单
    @Update("UPDATE repair_order SET status = #{status},update_time = #{updateTime} WHERE id = #{id}")
    int updateOrder(@Param("id") Integer id, @Param("status") String status, @Param("updateTime")LocalDateTime updateTime);

    // 上传图片
    @Update("UPDATE repair_Order SET image_url=#{imageUrl},update_time=NOW() WHERE id = #{id}")
    int uploadImageUrl(@Param("id")Integer id,@Param("imageUrl")String imageUrl);

    // 管理员：根据状态查询报修单列表
    @Select("SELECT id,device_type,description,status,create_time,update_time,account,image_url FROM repair_order WHERE status = #{status} ORDER BY create_time DESC")
    List<RepairOrder> selectByStatus(String status);

    // 管理员：查询所有报修单列表
    @Select("SELECT id,device_type,description,status,create_time,update_time,account,image_url FROM repair_order ORDER BY create_time DESC")
    List<RepairOrder> selectAll();

    // 管理员：删除报修单
    @Delete("DELETE FROM repair_order WHERE id = #{id}")
    int deleteOrder(Integer id);
}