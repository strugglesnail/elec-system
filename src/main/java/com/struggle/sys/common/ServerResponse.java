package com.struggle.sys.common;


import java.io.Serializable;

/**
 * JSON返回工具类
 * @param <T>
 */
public class ServerResponse<T> implements Serializable{
    private String msg;
    private long count;
    private int code;
    private T data; //可以指定泛型里面的内容，也可以不指定泛型里面的内容(String,map,list)

    //构造方法全部私有
    private ServerResponse(int code){
        this.code = code;
    }
    private ServerResponse(int code, T data){
        this.code = code;
        this.data = data;
    }
    private ServerResponse(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
    private ServerResponse(int code, String msg, long count, T data){
        this.code= code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }
    private ServerResponse(int code, T data, String msg){
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    //确认响应是不是正确响应(true:成功)
    //@JsonIgnore //序列化之后不会显示在json里面
    public boolean isSuccess(){
        return this.code == ResponseCode.SUCCESS.getCode(); //如果是0的话表示成功响应
    }


    //对外开放get方法
//    public long getCount() {
//        return count;
//    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }



    //静态方法对外开放,返回一个成功的构造器
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());

    }
    //静态方法对外开放,返回一个成功的构造器,带msg信息
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);

    }
    //静态方法对外开放,返回一个成功的构造器,带data信息
    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);

    }

    //静态方法对外开放,返回一个成功的构造器,带msg信息,data信息
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data,msg);

    }


    //静态方法对外开放,返回一个error的构造器,带描述
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    //静态方法对外开放,返回一个error的构造器,带errMessage
    public static <T> ServerResponse<T> createByErrorMessage(String errMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errMessage);
    }

    //自定义错误返回构造器
    public static <T> ServerResponse<T> createByErrorCodeMessage(int code, String errMessage){
        return new ServerResponse<T>(code,errMessage);
    }

    //静态方法对外开放,返回一个成功的构造器的表格数据,带msg信息,data信息,count信息
    public static <T> ServerResponse<T> createBySuccessForTable(long count, T data, String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, count, data);
    }

    //静态方法对外开放,没有登录，需要登录
    public static <T> ServerResponse<T> createByNeedLoginErrorMessage(String errMessage){
        return new ServerResponse<T>(ResponseCode.NEED_LOGIN.getCode(), errMessage);
    }

    //静态方法对外开放,没有权限，需要权限
    public static <T> ServerResponse<T> createByNeedPermissionErrorMessage(String errMessage){
        return new ServerResponse<T>(ResponseCode.NEED_PERMISSION.getCode(), errMessage);
    }

}