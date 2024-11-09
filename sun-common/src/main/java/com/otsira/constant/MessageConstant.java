package com.otsira.constant;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 定义消息的静态常量
 * @create: 2024/10/20 16:33
 */
public class MessageConstant {
    public static final String SYSTEM_ERROR = "服务器维护中，请稍后再试";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String ACCOUNT_LOCKED = "账号被禁用";
    public static final String USERNAME_CONFLICT = "该账号名称已被使用";
    public static final String CATEGORY_NAME_CONFLICT = "该菜品分类名称已存在";
    public static final String DISH_NAME_CONFLICT = "该菜品名称已存在";
    public static final String CATEGORY_NOT_FOUND = "该菜品分类不存在";
    public static final String DISH_NOT_FOUND = "菜品不存在";
    public static final String CATEGORY_BE_RELATED_BY_SETMEAL = "当前分类关联了套餐,不能删除";
    public static final String CATEGORY_BE_RELATED_BY_DISH = "当前分类关联了菜品,不能删除";
    public static final String DISH_BE_RELATED_BY_SETMEAL = "当前菜品关联了套餐,不能删除";
    public static final String DISH_ON_SALE = "起售中的菜品不能删除";
    public static final String SETMEAL_ON_SALE = "起售中的套餐不能删除";
    public static final String SETMEAL_ENABLE_FAILED = "套餐内包含未启售菜品，无法启售";
    public static final String UNKNOWN_ERROR = "未知错误";
    public static final String USER_NOT_LOGIN = "用户未登录";
    public static final String SHOPPING_CART_IS_NULL = "购物车数据为空，不能下单";
    public static final String ADDRESS_BOOK_IS_NULL = "用户地址为空，不能下单";
    public static final String LOGIN_FAILED = "登录失败";
    public static final String UPLOAD_FAILED = "文件上传失败";
    public static final String PASSWORD_EDIT_FAILED = "密码修改失败";
    public static final String ORDER_STATUS_ERROR = "订单状态错误";
    public static final String ORDER_NOT_FOUND = "订单不存在";
}
