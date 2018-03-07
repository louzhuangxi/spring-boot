package org.h819.web.jdbc;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Description : TODO(apache dbutils 工具类)
 * User: h819
 * Date: 2018/2/26
 * ---
 * QueryRunner 类中的方法执行过程：
 * 获取 Connection、 Statement
 * 执行 sql
 * 关闭 Connection、 Statement
 * -
 * 所以每次直接用  QueryRunner 中的方法就可以，不必再获取和关闭资源
 * 每次执行 sql 语句，QueryRunner 会自动获取和关闭资源一次
 * 就像使用 spring JdbcTemplate 一样
 * ---
 * 如果用到分页，参考 jdbcTemplate 工具类，改写 getList 方法
 * <p>
 * ---
 */

public class MyDbUtils {


    //=====

    /**
     * 下面几个 private 方法不对外提供，只提供直接的执行 sql 语句的方法
     */
    //=====
    private static QueryRunner getQueryRunnerByDataSource() {
        return new QueryRunner(MyDataSourceFactory.getDataSource());
    }


    /**
     * 开启事务
     */
    private static void beginTransaction(Connection conn) {
        try {
            // 开启事务
            conn.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 回滚事务
     */
    private static void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提交事务
     */
    private static void commit(Connection conn) {
        try {
            conn.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     *ArrayHandler：把结果集中的第一行数据转成对象数组。
     *ArrayListHandler：把结果集中的每一行数据都转成一个数组，再存放到List中。
     *BeanHandler：将结果集中的第一行数据封装到一个对应的JavaBean实例中。
     *BeanListHandler：将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到List里。
     *ColumnListHandler：将结果集中某一列的数据存放到List中。
     *KeyedHandler(name)：将结果集中的每一行数据都封装到一个Map里，再把这些map再存到一个map里，其key为指定的key。
     *MapHandler：将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值。
     *MapListHandler：将结果集中的每一行数据都封装到一个Map里，然后再存放到List
     */

    /**
     * 批量操作，包括批量保存、修改、删除
     *
     * @param sql
     * @param params
     * @return
     */
    public static int[] batch(Connection connection, String sql, Object[][] params) {
        try {
            return new QueryRunner().batch(connection, sql, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 批量操作，包括批量保存、修改、删除
     *
     * @param sql
     * @param params
     * @return
     */
    public static int[] batch(String sql, Object[][] params) {
        try {
            return getQueryRunnerByDataSource().batch(sql, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 删除操作
     *
     * @param sql
     * @param params
     * @return
     */
    public static int delete(String sql, Object... params) {
        try {
            return getQueryRunnerByDataSource().update(sql, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除操作
     *
     * @param sql
     * @param params
     * @return
     */
    public static int delete(Connection connection, String sql, Object... params) {
        try {
            return new QueryRunner().update(connection, sql, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新操作
     *
     * @param sql
     * @param params
     * @return
     */
    public static int update(Connection connection, String sql, Object... params) {
        try {
            new QueryRunner().update(connection, sql, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新操作
     *
     * @param sql
     * @param params
     * @return
     */
    public static int update(String sql, Object... params) {
        try {
            return getQueryRunnerByDataSource().update(sql, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 保存操作
     *
     * @param sql
     * @param params
     * @return
     */
    public static int save(Connection connection, String sql, Object... params) {
        try {
            return new QueryRunner().update(connection, sql, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 保存操作
     *
     * @param sql
     * @param params
     * @return
     */
    public static int save(String sql, Object... params) {
        try {
            return getQueryRunnerByDataSource().update(sql, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据sql查询list对象
     *
     * @param <T>
     * @param sql
     * @param type
     * @param params
     * @return
     */
    public static <T> List<T> getListBean(Connection connection, String sql, Class<T> type, Object... params) {
        try {
            return new QueryRunner().query(connection, sql, new BeanListHandler<T>(type), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据sql查询list对象
     *
     * @param <T>
     * @param sql
     * @param type
     * @param params
     * @return
     */
    public static <T> List<T> getListBean(String sql, Class<T> type, Object... params) {
        try {
            return getQueryRunnerByDataSource().query(sql, new BeanListHandler<T>(type), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据sql查询list对象
     *
     * @param <T>
     * @param sql
     * @param type
     * @return
     */
    public static <T> List<T> getListBean(Connection connection, String sql, Class<T> type) {
        try {
            return new QueryRunner().query(connection, sql, new BeanListHandler<T>(type));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据sql查询list对象
     *
     * @param <T>
     * @param sql
     * @param type
     * @return
     */
    public static <T> List<T> getListBean(String sql, Class<T> type) {
        try {
            // BeanListHandler 将ResultSet转换为List<JavaBean>的ResultSetHandler实现类
            return getQueryRunnerByDataSource().query(sql, new BeanListHandler<T>(type));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据sql和对象，查询结果并以对象形式返回
     *
     * @param <T>
     * @param sql
     * @param type
     * @param params
     * @return
     */
    public static <T> T getBean(Connection connection, String sql, Class<T> type, Object... params) {
        try {
            return new QueryRunner().query(connection, sql, new BeanHandler<T>(type), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据sql和对象，查询结果并以对象形式返回
     *
     * @param <T>
     * @param sql
     * @param type
     * @param params
     * @return
     */
    public static <T> T getBean(String sql, Class<T> type, Object... params) {
        try {
            return getQueryRunnerByDataSource().query(sql, new BeanHandler<T>(type), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据sql和对象，查询结果并以对象形式返回
     *
     * @param <T>
     * @param sql
     * @param type
     * @return
     */
    public static <T> T getBean(Connection connection, String sql, Class<T> type) {
        try {
            return new QueryRunner().query(connection, sql, new BeanHandler<T>(type));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据sql和对象，查询结果并以对象形式返回
     *
     * @param <T>
     * @param sql
     * @param type
     * @return
     */
    public static <T> T getBean(String sql, Class<T> type) {
        try {
            // BeanHandler 将ResultSet行转换为一个JavaBean的ResultSetHandler实现类
            return getQueryRunnerByDataSource().query(sql, new BeanHandler<T>(type));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql查询所有记录，以List Map形式返回
     *
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> getListMap(Connection connection, String sql, Object... params) {
        try {
            return new QueryRunner().query(connection, sql, new MapListHandler(), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql查询所有记录，以List Map形式返回
     *
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> getListMap(String sql, Object... params) {
        try {
            return getQueryRunnerByDataSource().query(sql, new MapListHandler(), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql查询所有记录，以List Map形式返回
     *
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> getListMap(Connection connection, String sql) {
        try {
            return new QueryRunner().query(connection, sql, new MapListHandler());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql查询所有记录，以List Map形式返回
     *
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> getListMap(String sql) {
        try {
            // MapListHandler 将ResultSet转换为List<Map>的ResultSetHandler实现类
            return getQueryRunnerByDataSource().query(sql, new MapListHandler());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql，查询记录，以Map形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
     *
     * @param sql
     * @param params
     * @return
     */
    public static Map<String, Object> getFirstRowMap(Connection connection, String sql, Object... params) {
        try {
            return new QueryRunner().query(connection, sql, new MapHandler(), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql，查询记录，以Map形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
     *
     * @param sql
     * @param params
     * @return
     */
    public static Map<String, Object> getFirstRowMap(String sql, Object... params) {
        try {
            return getQueryRunnerByDataSource().query(sql, new MapHandler(), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql，查询记录，以Map形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
     *
     * @param sql
     * @return
     */
    public static Map<String, Object> getFirstRowMap(Connection connection, String sql) {
        try {
            return new QueryRunner().query(connection, sql, new MapHandler());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql，查询记录，以Map形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
     *
     * @param sql
     * @return
     */
    public static Map<String, Object> getFirstRowMap(String sql) {
        try {
            // MapHandler 将ResultSet的首行转换为一个Map的ResultSetHandler实现类
            return getQueryRunnerByDataSource().query(sql, new MapHandler());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 根据sql查询返回所有记录，以List数组形式返回
     *
     * @param sql
     * @param params
     * @return
     */
    public static List<Object[]> getListArray(Connection connection, String sql, Object... params) {
        try {
            return new QueryRunner().query(connection, sql, new ArrayListHandler(), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据sql查询返回所有记录，以List数组形式返回
     *
     * @param sql
     * @param params
     * @return
     */
    public static List<Object[]> getListArray(String sql, Object... params) {
        try {
            return getQueryRunnerByDataSource().query(sql, new ArrayListHandler(), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据sql查询返回所有记录，以List数组形式返回
     *
     * @param sql
     * @return
     */
    public static List<Object[]> getListArray(Connection connection, String sql) {
        try {
            new QueryRunner().query(connection, sql, new ArrayListHandler());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据sql查询返回所有记录，以List数组形式返回
     *
     * @param sql
     * @return
     */
    public static List<Object[]> getListArray(String sql) {
        try {
            // ArrayListHandler 将ResultSet转换为List<Object[]>的ResultSetHandler实现类
            return getQueryRunnerByDataSource().query(sql, new ArrayListHandler());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql，查询记录，以数组形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
     *
     * @param sql
     * @param params
     * @return
     */
    public static Object[] getFirstRowArray(Connection connection, String sql, Object... params) {
        try {
            return new QueryRunner().query(connection, sql, new ArrayHandler(), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql，查询记录，以数组形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
     *
     * @param sql
     * @param params
     * @return
     */
    public static Object[] getFirstRowArray(String sql, Object... params) {
        try {
            return getQueryRunnerByDataSource().query(sql, new ArrayHandler(), params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql，查询记录，以数组形式返回第一行记录。 注意：如果有多行记录，只会返回第一行，所以适用场景需要注意，可以使用根据主键来查询的场景
     *
     * @param sql
     * @return
     */
    public static Object[] getFirstRowArray(Connection connection, String sql) {
        try {
            return new QueryRunner().query(connection, sql, new ArrayHandler());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据传入的sql，查询记录，以数组形式返回第一行记录。 注意：如果有多行记录，只会返回第一行， 所以适用场景需要注意，可以使用根据主键来查询的场景
     *
     * @param sql
     * @return
     */
    public static Object[] getFirstRowArray(String sql) {
        try {
            return getQueryRunnerByDataSource().query(sql, new ArrayHandler());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 得到查询记录的条数
     *
     * @param sql
     * @param params
     * @return
     */
    public static int getCount(String sql, Object... params) {
        try {
            Object value = getQueryRunnerByDataSource().query(sql, new ScalarHandler(), params);
            return objectToInteger(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到查询记录的条数
     *
     * @param sql
     * @param params
     * @return
     */
    public static int getCount(Connection conn, String sql, Object... params) {
        try {
            // ScalarHandler 将ResultSet的一个列到一个对象
            Object value = new QueryRunner().query(conn, sql, new ScalarHandler(), params);
            return objectToInteger(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到查询记录的条数
     *
     * @param sql
     * @return
     */
    public static int getCount(String sql) {
        try {
            Object value = getQueryRunnerByDataSource().query(sql, new ScalarHandler());
            return objectToInteger(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /*
     * 得到查询记录的条数
     *
     * @param sql
     *
     * @return 查询记录条数
     */
    public static int getCount(Connection connection, String sql) {
        try {
            // ScalarHandler 将ResultSet的一个列到一个对象
            Object value = new QueryRunner().query(connection, sql, new ScalarHandler());
            return objectToInteger(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private static int objectToInteger(Object obj) {
        try {
            if (obj != null && !obj.toString().trim().equals(""))
                return Integer.parseInt(obj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
        return 0;
    }
}
