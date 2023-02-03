package com.wm.file.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@ExcelTarget("userEntityRes")
public class UserEntityRes implements Serializable {

    private static final long serialVersionUID = -5644799954031156649L;
    
    @Excel(name = "主键ID")
    private String id;
    
    @Excel(name = "姓名")
    private String name;
    
    @Excel(name = "手机")
    private String phone;
    
    @Excel(name = "创建人")
    private String ceateBy;
    
    @Excel(name = "备注")
    private String remark;

    /*@TableField("birthday")
    @ExcelProperty(name = "生日" ,index = 5)
    private Date birthday;*/
    
    @Excel(name = "测试数据1")
    private String test1;
    
    @Excel(name = "测试数据2")
    private String test2;
    
    @Excel(name = "测试数据3")
    private String test3;
    
    @Excel(name = "测试数据4")
    private String test4;
    
    @Excel(name = "测试数据5")
    private String test5;
    
    @Excel(name = "测试数据6")
    private String test6;
    
    @Excel(name = "测试数据7")
    private String test7;
    
    @Excel(name = "测试数据8")
    private String test8;
    
    @Excel(name = "测试数据9")
    private String test9;
    
    @Excel(name = "测试数据10")
    private String test10;
    
    @Excel(name = "测试数据11")
    private String test11;
    
    @Excel(name = "测试数据12")
    private String test12;
    
    @Excel(name = "测试数据13")
    private String test13;
    
    @Excel(name = "测试数据14")
    private String test14;
    
    @Excel(name = "测试数据15")
    private String test15;
    
    @Excel(name = "测试数据16")
    private String test16;
    
    @Excel(name = "测试数据17")
    private String test17;
    
    @Excel(name = "测试数据18")
    private String test18;
    
    @Excel(name = "测试数据19")
    private String test19;
    
    @Excel(name = "测试数据20")
    private String test20;
    
    @Excel(name = "测试数据21")
    private String test21;
}
