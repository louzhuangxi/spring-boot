package org.examples.app.utils;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Description : spirng TransactionTemplate工具类，供 client 调用事务使用
 * User: h819
 * Date: 2014-09-15
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class TransactionTemplateUtils {

    public static TransactionTemplate getTransactionTemplate(
            PlatformTransactionManager txManager,
            int propagationBehavior,
            int isolationLevel) {

        TransactionTemplate transactionTemplate = new TransactionTemplate(txManager);
        transactionTemplate.setPropagationBehavior(propagationBehavior);
        transactionTemplate.setIsolationLevel(isolationLevel);
        return transactionTemplate;
    }

    public static TransactionTemplate getDefaultTransactionTemplate(PlatformTransactionManager txManager) {
        return getTransactionTemplate(
                txManager,
                TransactionDefinition.PROPAGATION_REQUIRED,  //支持当前事务，如果当前没有事务，就新建一个事务
                TransactionDefinition.ISOLATION_READ_COMMITTED);
    }
}
