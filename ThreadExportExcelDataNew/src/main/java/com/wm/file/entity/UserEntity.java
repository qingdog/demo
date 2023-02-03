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

}
