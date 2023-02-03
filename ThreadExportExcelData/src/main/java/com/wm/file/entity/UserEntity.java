package com.wm.file.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "user")//指定表名
public class UserEntity implements Serializable {

    private static final long serialVersionUID = -5644799954031156649L;

    @TableId(value = "id", type = IdType.AUTO)//指定自增策略
    @ExcelProperty(value = "主键ID" ,index = 0)
    private String id;

    @TableField("name")
    @ExcelProperty(value = "姓名" ,index = 1)
    private String name;

    @TableField("phone")
    @ExcelProperty(value = "手机" ,index = 2)
    private String phone;

    @TableField("ceate_by")
    @ExcelProperty(value = "创建人" ,index = 3)
    private String ceateBy;

    @TableField("remark")
    @ExcelProperty(value = "备注" ,index = 4)
    private String remark;

    /*@TableField("birthday")
    @ExcelProperty(value = "生日" ,index = 5)
    private Date birthday;*/

    @TableField("test1")
    @ExcelProperty(value = "测试数据1" ,index = 6)
    private String test1;

    @TableField("test2")
    @ExcelProperty(value = "测试数据2" ,index = 7)
    private String test2;

    @TableField("test3")
    @ExcelProperty(value = "测试数据3" ,index = 8)
    private String test3;

    @TableField("test4")
    @ExcelProperty(value = "测试数据4" ,index = 9)
    private String test4;

    @TableField("test5")
    @ExcelProperty(value = "测试数据5" ,index = 10)
    private String test5;

    @TableField("test6")
    @ExcelProperty(value = "测试数据6" ,index = 11)
    private String test6;

    @TableField("test7")
    @ExcelProperty(value = "测试数据7" ,index = 12)
    private String test7;

    @TableField("test8")
    @ExcelProperty(value = "测试数据8" ,index = 13)
    private String test8;

    @TableField("test9")
    @ExcelProperty(value = "测试数据9" ,index = 14)
    private String test9;

    @TableField("test10")
    @ExcelProperty(value = "测试数据10" ,index = 15)
    private String test10;

    @TableField("test11")
    @ExcelProperty(value = "测试数据11" ,index = 16)
    private String test11;

    @TableField("test12")
    @ExcelProperty(value = "测试数据12" ,index =17)
    private String test12;

    @TableField("test13")
    @ExcelProperty(value = "测试数据13" ,index = 18)
    private String test13;

    @TableField("test14")
    @ExcelProperty(value = "测试数据14" ,index = 19)
    private String test14;

    @TableField("test15")
    @ExcelProperty(value = "测试数据15" ,index = 20)
    private String test15;

    @TableField("test16")
    @ExcelProperty(value = "测试数据16" ,index = 21)
    private String test16;

    @TableField("test17")
    @ExcelProperty(value = "测试数据17" ,index = 22)
    private String test17;

    @TableField("test18")
    @ExcelProperty(value = "测试数据18" ,index = 23)
    private String test18;

    @TableField("test19")
    @ExcelProperty(value = "测试数据19" ,index = 24)
    private String test19;

    @TableField("test20")
    @ExcelProperty(value = "测试数据20" ,index = 25)
    private String test20;

    @TableField("test21")
    @ExcelProperty(value = "测试数据21" ,index = 26)
    private String test21;
}
