package com.base.spring.service;

import com.base.spring.domain.RoleEntity;
import com.base.spring.domain.TreeEntity;
import com.base.spring.repository.RoleRepository;
import com.base.spring.repository.TreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/10/10
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class RoleService {

    //private static final log log = LoggerFactory.getLogger(RoleService.class);


    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TreeRepository treeRepository;

    /**
     * 关联所有树节点到指定的 role
     *
     * @param treeNodeIds
     * @param roleId
     */
    @Transactional(readOnly = false)
    public void associate(String treeNodeIds, String roleId) {

        RoleEntity roleEntity = roleRepository.getOne(Long.valueOf(roleId.trim()));
        //清空
        if (treeNodeIds.isEmpty()) {
            log.info("clear.");
            roleEntity.clearTreeNodes();
            roleRepository.save(roleEntity);
            return;
        }

        //构造 id 集合
        String[] idArray = StringUtils.removeEnd(treeNodeIds, ",").split(",");
        List<Long> listTreeId = new ArrayList<>(idArray.length);
        for (String id : idArray) {
            // System.out.println("id=" + id);
            // roleEntity.addTreeNode();
            listTreeId.add(Long.valueOf(id.trim()));
        }

        /**
         * 重新建立关联，原来的关联关系会被替代
         * */
        // 被选中的节点
        List<TreeEntity> targetTreeNodes = treeRepository.findByIdIn(listTreeId);
        // role 已经关联的节点
        Set<TreeEntity> sourceTreeNodes = roleEntity.getTreeNodes();
        // 需要删除的节点
        List<TreeEntity> deleteTreeNodes = new ArrayList<>();


        //找到被取消关联关系的节点
        for (TreeEntity entity : sourceTreeNodes) {
            if (!targetTreeNodes.contains(entity))
                deleteTreeNodes.add(entity);
        }

        //取消关联
        if (!deleteTreeNodes.isEmpty())
            roleEntity.removeTreeNodes(deleteTreeNodes);

        //重新建立关联
        roleEntity.addTreeNodes(targetTreeNodes);

        roleRepository.save(roleEntity);

        //FastJsonPropertyPreFilter filter = new FastJsonPropertyPreFilter();
//        filter.addExcludes(TreeNodeEntity.class, "parent", "roles", "children");
//
//        MyJsonUtils.prettyPrint(treeNodeRepository.findByIdIn(listId), filter);

    }

}
