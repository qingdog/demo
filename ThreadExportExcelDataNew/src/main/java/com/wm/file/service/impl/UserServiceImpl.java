package com.wm.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wm.file.entity.UserEntity;
import com.wm.file.mapper.UserMapper;
import com.wm.file.service.UserService;
import com.wm.file.util.easypoi.JDBCDruidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public List<UserEntity> findLimit(int index, int pageSize) {

        return baseMapper.findLimit(index, pageSize);
    }

    @Override
    public List<UserEntity> findNewPage(String id, int pageSize) {

        return baseMapper.findNewPage(id, pageSize);
    }


    @Autowired
    private DataSource dataSource;

    @Override
    public Map<String, Object> importDBFromExcel10w(List<Map<Integer, String>> dataList) {
        HashMap<String, Object> result = new HashMap<>();
        //结果集中数据为0时,结束方法.进行下一次调用
        if (dataList.size() == 0) {
            result.put("empty", "0000");
            return result;
        }
        //JDBC分批插入+事务操作完成对10w数据的插入
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long startTime = System.currentTimeMillis();
            log.info(dataList.size() + "条,开始导入到数据库时间:" + startTime + "ms");
            conn = dataSource.getConnection();
            //控制事务:默认不提交
            conn.setAutoCommit(false);
            String sql = "insert into usernew (id,name,phone,ceate_by) values (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            //循环结果集:这里循环不支持"烂布袋"表达式
            for (int i = 0; i < dataList.size(); i++) {
                Map<Integer, String> item = dataList.get(i);
                ps.setString(1, item.get(0));
                ps.setString(2, item.get(1));
                ps.setString(3, item.get(2));
                ps.setString(4, item.get(3));

                //将一组参数添加到此 PreparedStatement 对象的批处理命令中。
                ps.addBatch();
            }
            //执行批处理
            ps.executeBatch();
            //手动提交事务
            conn.commit();
            long endTime = System.currentTimeMillis();
            log.info(dataList.size() + "条,结束导入到数据库时间:" + endTime + "ms");
            log.info(dataList.size() + "条,导入用时:" + (endTime - startTime) + "ms");
            result.put("success", "1111");
        } catch (Exception e) {
            result.put("exception", "0000");
            e.printStackTrace();
        } finally {
            //关连接
            JDBCDruidUtils.close(conn, ps);
        }
        return result;
    }


}
