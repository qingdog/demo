package com.xiaoma.item.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

@Component
public class NoAutoTransactionalUtil {
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    /**
     * 开启事务
     *
     * @return
     */
    public TransactionStatus begin() {
        TransactionStatus transaction = dataSourceTransactionManager.getTransaction(new DefaultTransactionAttribute());
        return transaction;
    }

    /**
     * 提交事务
     *
     * @return
     */
    public void commit(TransactionStatus transactionStatus) {
        dataSourceTransactionManager.commit(transactionStatus);
    }

    /**
     * 回滚事务
     *
     * @return
     */
    public void rollback(TransactionStatus transactionStatus) {
        dataSourceTransactionManager.rollback(transactionStatus);
    }
}
