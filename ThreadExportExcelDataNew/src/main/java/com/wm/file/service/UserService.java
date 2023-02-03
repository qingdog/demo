package com.wm.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wm.file.entity.UserEntity;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface UserService extends IService<UserEntity> {


    List<UserEntity> findLimit(int i, int pageSize);

    List<UserEntity> findNewPage(String o, int pageSize);

    Map<String, Object> importDBFromExcel10w(List<Map<Integer, String>> dataList);
}
