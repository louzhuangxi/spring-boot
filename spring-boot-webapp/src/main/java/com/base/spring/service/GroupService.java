package com.base.spring.service;

import com.base.spring.domain.GroupEntity;
import com.base.spring.domain.RoleEntity;
import com.base.spring.domain.UserEntity;
import com.base.spring.repository.GroupRepository;
import com.base.spring.repository.RoleRepository;
import com.base.spring.repository.UserRepository;
import org.h819.commons.json.FastJsonPropertyPreFilter;
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
public class GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);


    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    /**
     * 关联所有 user 到指定的 group
     *
     * @param userIds
     * @param groupId
     */
    @Transactional(readOnly = false)
    public void associateUsers(String[] userIds, String groupId) {
        GroupEntity groupEntity = groupRepository.findOne(Long.valueOf(groupId.trim()));

        if (groupEntity == null)
            return;

        //清空
        if (userIds == null || userIds.length == 0) {
            logger.info("clear users.");
            groupEntity.clearUsers();
            groupRepository.save(groupEntity);
            return;
        }

//        for (String userId : userIds) {
//            logger.info("userId ={}", userId);
//        }

        //构造 id 集合
        List<Long> listUserId = new ArrayList<>(userIds.length);
        for (String id : userIds) {
            // System.out.println("id=" + id);
            // roleEntity.addTreeNode();
            listUserId.add(Long.valueOf(id.trim()));
        }


        /**
         * 重新建立关联，原来的关联关系会被替代
         * */
        // 被选中的节点
        List<UserEntity> targetTreeNodes = userRepository.findByIdIn(listUserId);
        // group 已经关联的 user
        List<UserEntity> sourceTreeNodes = groupEntity.getUsers();
        // 需要删除的节点
        List<UserEntity> deleteTreeNodes = new ArrayList<>();


        //找到被取消关联关系的节点
        for (UserEntity entity : sourceTreeNodes) {
            if (!targetTreeNodes.contains(entity))
                deleteTreeNodes.add(entity);
        }

        //取消关联
        if (!deleteTreeNodes.isEmpty())
            groupEntity.removeUsers(deleteTreeNodes);

        //重新建立关联
        groupEntity.addUsers(targetTreeNodes);

        groupRepository.save(groupEntity);

        FastJsonPropertyPreFilter filter = new FastJsonPropertyPreFilter();
        filter.addExcludes(UserEntity.class, "roles");
        filter.addExcludes(GroupEntity.class, "roles", "users");
        //  MyJsonUtils.prettyPrint(userRepository.findByIdIn(listUserId), filter);

    }


    /**
     * 关联所有 user 到指定的 group
     *
     * @param roleIds
     * @param groupId
     */
    @Transactional(readOnly = false)
    public void associateRoles(String[] roleIds, String groupId) {

        GroupEntity groupEntity = groupRepository.findOne(Long.valueOf(groupId.trim()));

        if (groupEntity == null)
            return;

        //清空
        if (roleIds == null || roleIds.length == 0) {
            logger.info("clear roles.");
            groupEntity.clearRoles();
            groupRepository.save(groupEntity);
            return;
        }

//        for (String roleId : roleIds) {
//            logger.info("userId ={}", roleId);
//        }

        //构造 id 集合
        List<Long> listRoleId = new ArrayList<>(roleIds.length);
        for (String id : roleIds) {
            // System.out.println("id=" + id);
            // roleEntity.addTreeNode();
            listRoleId.add(Long.valueOf(id.trim()));
        }


        /**
         * 重新建立关联，原来的关联关系会被替代
         * */
        // 被选中的节点
        List<RoleEntity> targetTreeNodes = roleRepository.findByIdIn(listRoleId);
        // group 已经关联的 role
        List<RoleEntity> sourceTreeNodes = groupEntity.getRoles();
        // 需要删除的节点
        List<RoleEntity> deleteTreeNodes = new ArrayList<>();


        //找到被取消关联关系的节点
        for (RoleEntity entity : sourceTreeNodes) {
            if (!targetTreeNodes.contains(entity))
                deleteTreeNodes.add(entity);
        }

        //取消关联
        if (!deleteTreeNodes.isEmpty())
            groupEntity.removeRoles(deleteTreeNodes);

        //重新建立关联
        groupEntity.addRoles(targetTreeNodes);

        groupRepository.save(groupEntity);

//        FastJsonPropertyPreFilter filter = new FastJsonPropertyPreFilter();
//        filter.addExcludes(RoleEntity.class, "groups", "users", "treeNodes");
//        MyJsonUtils.prettyPrint(roleRepository.findByIdIn(listRoleId), filter);

    }

}
