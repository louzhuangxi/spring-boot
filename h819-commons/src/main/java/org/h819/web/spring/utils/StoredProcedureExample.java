package org.h819.web.spring.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description : TODO(spring jdbc 存储过程调用例子)
 * User: h819
 * Date: 14-4-3
 * Time: 上午9:19
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class StoredProcedureExample {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getSubscribeMobileList(Map<String, Object> map) {
        final List<String> mobileList = (List<String>) map.get("mobiles");
        final JdbcTemplate jdbcTemplateMehtod = this.jdbcTemplate;

        List resultList = (List) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                        String procSql = "{Call proc_query_subsceibe_mobiles(?,?)}";// 调用的sql
                        CallableStatement proc = conn.prepareCall(procSql);

                        //获取 oracle 的自定义数组类型 mobileArray
                        Connection oracleConn = conn.getMetaData().getConnection();
                        //该句中的 MOBILEARRAY 一定要为大写，否则会报错误；该值就是在 oracle 中自定义的数组类型 mobileArray

                        oracle.sql.ArrayDescriptor mobileArrayDes =
                                oracle.sql.ArrayDescriptor.createDescriptor("MOBILEARRAY", oracleConn);
                        // java 数组类型转换为 oracle 自定义数组类型
                        oracle.sql.ARRAY mobileArray = new oracle.sql.ARRAY(mobileArrayDes, oracleConn, mobileList.toArray());
                        //测试转换是否正确
                        // String[] array = (String[]) mobileArray.getArray();
                        proc.setArray(1, mobileArray);
                        //oracle.jdbc.OracleTypes.CURSOR：java获得oracle的游标。此类在 oracle 驱动中
                        proc.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);//输出参数
                        return proc;
                    }
                },
                new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<String> mobilesList = new ArrayList<String>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            String mobile = rs.getString("MOBILE");
                            if (!StringUtils.isEmpty(mobile) && !mobilesList.contains(mobile)) {
                                mobilesList.add(mobile);
                            }

                        }
                        rs.close();
                        return mobilesList;
                    }
                }
        );

        return resultList;
    }

    /**
     * 演示 Mysql 的存储过程创建，及调用. oracle 可能不同过程不同，未测试
     */
    public void mysqlProcedureExample() {

        //创建存储过程，有输入变量，无输出变量
        //mysql 中 调用   CALL search_nam(@乳);
        /**
         DROP PROCEDURE IF EXISTS `search_nam`;
         CREATE PROCEDURE search_nam(
         IN p_nam VARCHAR(20)
         )
         BEGIN
         SELECT * FROM 14880_food_class  where food_name like '%' || p_nam || '%';
         END

         **/

        //创建存储过程，有输入、输出变量，编译通过，但不知道在 java 怎么调用
        //mysql 中 调用   CALL search_nam(@乳,@out_food_code,@out_food_name);
        /**
         DROP PROCEDURE IF EXISTS `search_nam2`;
         CREATE PROCEDURE search_nam2(
         IN p_nam VARCHAR(20),
         OUT out_food_code VARCHAR(20),
         OUT out_food_name VARCHAR(20)
         )

         BEGIN

         IF p_nam IS NULL OR p_nam='' THEN
         SELECT * FROM 14880_food_class;

         ELSE
         SELECT food_code ,food_name INTO out_food_code,out_food_name FROM 14880_food_class WHERE food_name like '%' || p_nam || '%';

         END IF;

         END

         */

        /**
         * 测试  search_nam
         */
        //存储过程名字
        String procedureName = "search_nam";
        //获取  SimpleJdbcCall
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(procedureName);
        /**
         *  声明入参，传递变量，可有有多个
         *
         *  存储过程的入参名字为 p_nam ，赋值为 “乳”
         * */
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("p_nam", "乳");
        //编译
        Map<String, Object> out = simpleJdbcCall.execute(in);

        /**
         *  声明出参，并获取输出
         * */

        /**
         * 测试语句，返回如下结果
         * */

        //此语句不对，返回值是两个特定的 map
        System.out.println(out.get("food_code") + "," + out.get("food_name"));
        //测试返回结果
        for (Map.Entry entry : out.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        //输出结果为
        // Key = #result-set-1, Value = [{id=1, food_code=01.0, food_name=乳及乳制品（13.0特殊膳食用食品涉及品种除外）, food_remark=null, record_source=null, record_updatetime=null}]
        // Key = #update-count-1, Value = 0

        /**
         * 测试语句，完成
         */

        //获取查询结果，可以再此基础上进行处理
        List<Map<String, Object>> list = (List) out.get("#result-set-1");

        for (Map<String, Object> s : list) {
            for (Map.Entry<String, Object> entry : s.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
            System.out.println(StringUtils.center("break", 80, "="));
        }
    }
}
