package com.xiaoma.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoma.item.service.IItemStockService;
import com.xiaoma.item.mapper.ItemStockMapper;
import com.xiaoma.item.pojo.ItemStock;
import org.springframework.stereotype.Service;

@Service
public class ItemStockService extends ServiceImpl<ItemStockMapper, ItemStock> implements IItemStockService {
}
