package com.Mike12138210;

import com.Mike12138210.pojo.User;
import java.util.Scanner;
import com.Mike12138210.util.MyBatisUtil;
import com.Mike12138210.dao.UserMapper;
import org.apache.ibatis.session.SqlSession;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args){
        while(true){
            if(currentUser == null){
                ShowLoginMenu();
            }else {
                if("student".equals(currentUser.getRole())){
                    ShowStudentMenu();
                }else if("admin".equals(currentUser.getRole())){
                    ShowAdminMenu();
                }
            }
        }
    }

    private static void ShowLoginMenu(){
        System.out.println("===========================");
        System.out.println("\uD83C\uDFE0 宿舍报修管理系统");
        System.out.println("===========================");
        System.out.println("1.登陆");
        System.out.println("2.注册");
        System.out.println("3.退出");

        while(true){
            System.out.print("请选择操作（输入 1-3）：");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1:
                    login();
                    return;
                case 2:
                    register();
                    return;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("输入错误，请输入1,2或3");
            }
        }
    }

    private static void login(){
        System.out.println("===== 用户登录 =====");

        System.out.print("请输入账号：");
        String account = sc.nextLine();

        System.out.print("请输入密码：");
        String password = sc.nextLine();

        try(SqlSession session = MyBatisUtil.getSqlSession()){
            UserMapper mapper = session.getMapper(UserMapper.class);

            User user = mapper.selectByAccount(account);

            if(user == null){
                System.out.println("账号不存在，请重新登陆。");
                return;
            }
            if(!user.getPassword().equals(password)){
                System.out.println("密码错误，请重新登陆。");
                return;
            }

            currentUser = user;
            System.out.println("登陆成功！角色：" + (user.getRole().equals("student") ? "学生" : "管理员"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void register(){
        System.out.println("===== 用户注册 =====");
        System.out.print("请选择角色（1-学生，2-维修人员）：");
        int roleChoice = sc.nextInt();
        sc.nextLine();
        String role = (roleChoice == 1) ? "student" : "admin";

        String account = null;
        if(role.equals("student")){
            System.out.print("请输入学号：");
            account = sc.nextLine();
            if (!account.startsWith("3125") && !account.startsWith("3225")) {
                System.out.println("学生账号必须以3125或3225开头，注册失败");
                return;
            }
        }else{
            System.out.print("请输入工号：");
            account = sc.nextLine();

            if(!account.startsWith("0025")){
                System.out.println("管理员账号必须以0025开头，注册失败");
                return;
            }
        }

        System.out.print("请输入密码：");
        String password = sc.nextLine();
        System.out.print("请确认密码：");
        String confirm = sc.nextLine();

        if(!password.equals(confirm)){
            System.out.println("两次密码不一致，注册失败");
            return;
        }

        try(SqlSession session = MyBatisUtil.getSqlSession()){
            UserMapper mapper = session.getMapper(UserMapper.class);

            User user = new User();
            user.setAccount(account);
            user.setPassword(password);
            user.setRole(role);

            int rows = mapper.insertUser(user);
            if(rows > 0){
                System.out.println("注册成功！请返回主界面登陆。");
            }else{
                System.out.println("注册失败，账号可能已存在。");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void ShowStudentMenu(){
        if(currentUser.getBuilding() == null || currentUser.getRoom() == null){
            System.out.println("首次登录，请先绑定宿舍");
            bindDorm();
        }

        while(true){
            System.out.println("===== 学生菜单 =====");
            System.out.println("1.绑定/修改宿舍");
            System.out.println("2.创建报修单");
            System.out.println("3.查看我的报修记录");
            System.out.println("4.取消报修单");
            System.out.println("5.修改密码");
            System.out.println("6.退出");

            System.out.print("请选择操作（输入 1-6）：");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1:
                    bindDorm();
                    return;
                case 2:
                    createOrder();
                    return;
                case 3:
                    ;
                    return;
                case 4:
                    ;
                    return;
                case 5:
                    ;
                    return;
                case 6:
                    currentUser = null;
                    return;
                default:
                    System.out.println("输入错误，请输入1,2,3,4,5或6");
            }
        }
    }



    private static void bindDorm(){
        System.out.println("===== 绑定/修改宿舍 =====");
        System.out.print("请输入宿舍楼（如西4）：");
        String building = sc.nextLine();
        System.out.print("请输入房间号（如101）：");
        String room = sc.nextLine();

        currentUser.setBuilding(building);
        currentUser.setRoom(room);

        try(SqlSession session = MyBatisUtil.getSqlSession()){
            UserMapper mapper = session.getMapper(UserMapper.class);

            int rows = mapper.updateUser(currentUser);
            if(rows > 0){
                System.out.println("宿舍信息更新成功！");
            }else{
                System.out.println("更新失败，请稍后重试。");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createOrder() {
    }

    private static void ShowAdminMenu(){

    }
}
