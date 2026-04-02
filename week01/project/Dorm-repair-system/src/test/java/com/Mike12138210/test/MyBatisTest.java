package com.Mike12138210.test;

import com.Mike12138210.dao.UserMapper;
import com.Mike12138210.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisTest {

    public static void main(String[] args) throws IOException {
        // 加载 mybatis-config.xml 配置文件，获取SqlSessionFactory
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);

        // 获取 SqlSession对象，用它来执行sql
        try (SqlSession session = sqlSessionFactory.openSession(true)) { // true 表示自动提交
            UserMapper mapper = session.getMapper(UserMapper.class);
            // 测试查询：假设数据库中有一条 account 为 '3125004123' 的记录
            User user = mapper.selectByAccount("3125004123");
            if (user != null) {
                System.out.println("查询成功：" + user.getAccount());
            } else {
                System.out.println("没有找到用户");
            }
        }
    }

    public static void testCRUD() throws IOException {
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = new User();
            user.setId(1); // 假设要更新 id 为 1 的用户
            user.setPassword("newpassword");
            // 如果需要更新其他字段，也设置上
            mapper.updateUser(user);
            System.out.println("更新成功");
        }
    }
}