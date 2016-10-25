package com.base.spring.repository;

import com.base.spring.domain.TreeEntity;
import com.base.spring.domain.TreeType;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/18
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public interface TreeRepository extends BaseRepository<TreeEntity, Long> {


    /**
     * 返回单个对象时，如果没有值，会返回 null ，此时用 Optional 包装
     *
     * 返回 List 就不用 Optional 包装了，如果没有值，会返回空集合
     */

    /**
     * name 不唯一
     *
     * @param name
     * @return
     */
    @Query("select e from TreeEntity e where e.name=?1")
    List<TreeEntity> findByName(String name);

    @Query("select e from TreeEntity e where e.name like %?1%")
    List<TreeEntity> findByContainsName(String name);

    /**
     * 获所有的根节点
     *
     * @return
     */
    @Query("select e from TreeEntity e where e.parent is null order by  e.index")
    List<TreeEntity> getRoot();

    /**
     * 获 type 类型的根节点
     *
     * @return
     */
    @Query("select e from TreeEntity e where e.parent is null and e.type =?1 order by  e.index")
    Optional<TreeEntity> getRoot(TreeType menuType);


    @Query("select e.type from TreeEntity e")
    List<TreeType> findTreeTypes();


    @Query("select e from TreeEntity e where e.id in ?1")
    List<TreeEntity> findByIdIn(Collection<Long> ids);

}