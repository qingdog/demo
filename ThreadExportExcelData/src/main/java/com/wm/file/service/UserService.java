package com.wm.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wm.file.entity.UserEntity;

import java.io.OutputStream;
import java.util.List;

public interface UserService extends IService<UserEntity> {

    List<UserEntity> selectAll(Integer total,Integer limit);

}
