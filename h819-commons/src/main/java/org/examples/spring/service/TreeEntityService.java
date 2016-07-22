package org.examples.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 演示了动态查询的实现方法。通用方法，不同的 SearchFilter ，可以使用
 */

@Service
@Transactional(readOnly = true)
public class TreeEntityService {

    private static Logger logger = LoggerFactory.getLogger(TreeEntityService.class);

    @Transactional(readOnly = false)
   public void deleteOrUpdate(){

    }


}

