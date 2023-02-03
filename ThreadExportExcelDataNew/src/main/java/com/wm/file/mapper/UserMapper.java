package com.wm.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wm.file.entity.UserEntity;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    @Select("select * from user")
    List<UserEntity> selectAll();

    List<UserEntity> findLimit(@Param("index") int index, @Param("pageSize") int pageSize);

    List<UserEntity> findNewPage(@Param("id") String id, @Param("pageSize") int pageSize);

}
