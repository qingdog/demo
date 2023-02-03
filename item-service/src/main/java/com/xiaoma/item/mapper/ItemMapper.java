package com.xiaoma.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoma.item.pojo.Item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper extends BaseMapper<Item> {
    int insertUserList(@Param("list") List<Item> list);
}
