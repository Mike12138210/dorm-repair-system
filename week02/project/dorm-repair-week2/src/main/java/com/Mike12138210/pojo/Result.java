package com.Mike12138210.pojo;

public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result(){}

    public Result(Integer code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public static <E> Result<E> success(E data) {
        return new Result<>(0,"操作成功",data);
    }

    public static Result success(){
        return new Result(0,"操作成功",null);
    }

    public static Result error(String message){
        return new Result(1,message,null);
    }
}