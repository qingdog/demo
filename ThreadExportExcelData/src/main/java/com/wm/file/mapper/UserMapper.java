package com.wm.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wm.file.entity.UserEntity;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    @Select("select * from user")
    List<UserEntity> selectAll();

}
