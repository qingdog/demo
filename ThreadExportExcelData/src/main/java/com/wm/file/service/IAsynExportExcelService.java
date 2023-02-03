package com.wm.file.service;

import com.wm.file.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName: IAsynExportExcelService
 * @Description:
 * @Author: xiaomage
 * @Date: 2021-08-06 20:05
 **/
public interface IAsynExportExcelService {

    /**
     * 分批次异步导出数据
     *
     * @param countDownLatch
     */
    void excuteAsyncTask(Map<String, Object> map, CountDownLatch countDownLatch);

    void excuteAsyncTaskDatabase(Map<String, Object> poll, CountDownLatch cdl);
}
