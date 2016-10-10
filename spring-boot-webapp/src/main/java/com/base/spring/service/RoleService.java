package com.base.spring.service;

import com.base.spring.domain.RoleEntity;
import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.repository.RoleRepository;
import com.base.spring.repository.TreeNodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/10/10
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true)
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);


    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TreeNodeRepository treeNodeRepository;

    /**
     * 关联所有树节点到指定的 role
     *
     * @param treeNodeIds
     * @param roleId
     */
    @Transactional(readOnly = false)
    public void associate(String treeNodeIds, String roleId) {

        RoleEntity roleEntity = roleRepository.findOne(Long.valueOf(roleId.trim()));
        //清空
        if (treeNodeIds.isEmpty()) {
            logger.info("clear.");
            roleEntity.clearTreeNodes();
            roleRepository.save(roleEntity);
            return;
        }

        String[] idArray = StringUtils.removeEnd(treeNodeIds, ",").split(",");
        List<Long> listTreeId = new ArrayList<>(idArray.length);

        for (String id : idArray) {
            // System.out.println("id=" + id);
            // roleEntity.addTreeNode();
            listTreeId.add(Long.valueOf(id.trim()));
        }

        //重新建立关联，原来的关联关系会被替代
        List<TreeNodeEntity> targetTreeNodes = treeNodeRepository.findByIdIn(listTreeId);
        List<TreeNodeEntity> sourceTreeNodes = roleEntity.getTreeNodes();
        List<TreeNodeEntity> deleteTreeNodes = new ArrayList<>();


        //找到被取消关联关系的节点
        for (TreeNodeEntity entity : sourceTreeNodes) {
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
