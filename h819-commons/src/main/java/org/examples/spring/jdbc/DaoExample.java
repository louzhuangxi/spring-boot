package org.examples.spring.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Description : TODO()
 * User: h819
 * Date: 2014-07-28
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DaoExample {

    private static Logger logger = LoggerFactory.getLogger(DaoExample.class);

    @Autowired
    @Resource(name = "jdbcTemplateOracle")
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询所有
     *
     * @return
     */
    public void findAllEntities() {


    }

}
