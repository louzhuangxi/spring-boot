package com.base.spring.repository;

import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.domain.TreeNodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/18
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
public interface TreeNodeRepository extends BaseRepository<TreeNodeEntity, Long> {


    /**
     * 返回单个对象时，如果没有值，会返回 null ，此时用 Optional 包装
     *
     * 返回 List 就不用 Optional 包装了，如果没有值，会返回空集合
     */

    /**
     * @param name
     * @return
     */
    @Query("select e from TreeNodeEntity e where e.name=?1")
    List<TreeNodeEntity> findByName(String name);

    @Query("select e from TreeNodeEntity e where e.name like %?1%")
    List<TreeNodeEntity> findByContainsName(String name);

    /**
     * 获 type 类型的根节点
     *
     * @param menuType 节点类型
     * @return
     */
    @Query("select e from TreeNodeEntity e where e.parent is null and e.type =?1 order by  e.index")
    Optional<TreeNodeEntity> getRoot(TreeNodeType menuType);

}