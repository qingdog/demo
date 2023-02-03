package com.xiaoma.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoma.item.pojo.Item;

public interface IItemService extends IService<Item> {
    void saveItem(Item item);
}
