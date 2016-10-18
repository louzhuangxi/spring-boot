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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/10/10
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


    public Optional<UserEntity> getUserById(long id) {
        logger.debug("Getting user={}", id);
        return Optional.ofNullable(userRepository.findOne(id));
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        logger.debug("Getting user by email={}", email.replaceFirst("@.*", "@***"));

        return userRepository.findOneByEmail(email);
    }


    public Collection<UserEntity> getAllUsers() {
        logger.debug("Getting all users");
        return userRepository.findAll(new Sort("email"));
    }


    @Transactional(readOnly = false)
    public UserEntity createUser(String loginName, String password, String email) {
        return userRepository.save(new UserEntity(loginName, password, email));
    }


    /**
     * 关联所有 user 到指定的 group
     *
     * @param groupIds
     * @param userId
     */
    @Transactional(readOnly = false)
    public void associateGroups(String[] groupIds, String userId) {
        UserEntity userEntity = userRepository.findOne(Long.valueOf(userId.trim()));

        if (userEntity == null)
            return;

        //清空
        if (groupIds == null || groupIds.length == 0) {
            logger.info("clear groups.");
            userEntity.clearGroups();
            userRepository.save(userEntity);
            return;
        }

        //构造 id 集合
        List<Long> listGroupId = new ArrayList<>(groupIds.length);
        for (String id : groupIds) {
            // System.out.println("id=" + id);
            // roleEntity.addTreeNode();
            listGroupId.add(Long.valueOf(id.trim()));
        }


        /**
         * 重新建立关联，原来的关联关系会被替代
         * */
        // 被选中的节点
        List<GroupEntity> targetGroups = groupRepository.findByIdIn(listGroupId);
        // group 已经关联的 user
        Set<GroupEntity> sourceTreeGroups = userEntity.getGroups();
        // 需要删除的节点
        List<GroupEntity> deleteGroups = new ArrayList<>();


        //找到被取消关联关系的节点
        for (GroupEntity entity : sourceTreeGroups) {
            if (!targetGroups.contains(entity))
                deleteGroups.add(entity);
        }

        //取消关联
        if (!deleteGroups.isEmpty())
            userEntity.removeGroups(deleteGroups);

        //重新建立关联
        userEntity.addGroups(targetGroups);

        userRepository.save(userEntity);

//        FastJsonPropertyPreFilter filter = new FastJsonPropertyPreFilter();
//        filter.addExcludes(UserEntity.class, "roles");
//        filter.addExcludes(GroupEntity.class, "roles", "users");
        //  MyJsonUtils.prettyPrint(userRepository.findByIdIn(listUserId), filter);

    }


    /**
     * 关联所有 user 到指定的 group
     *
     * @param roleIds
     * @param userId
     */
    @Transactional(readOnly = false)
    public void associateRoles(String[] roleIds, String userId) {

        UserEntity userEntity = userRepository.findOne(Long.valueOf(userId.trim()));

        if (userEntity == null)
            return;

        //清空
        if (roleIds == null || roleIds.length == 0) {
            logger.info("clear roles.");
            userEntity.clearRoles();
            userRepository.save(userEntity);
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
        List<RoleEntity> targetRoles = roleRepository.findByIdIn(listRoleId);
        //  logger.info("targetRoles size ={} ", targetRoles.size());

        // user 已经关联的 role
        Set<RoleEntity> sourceRoles = userEntity.getRoles();
        // 需要删除的节点
        List<RoleEntity> deleteRoles = new ArrayList<>();


        //找到被取消关联关系的节点
        for (RoleEntity entity : sourceRoles) {
            if (!targetRoles.contains(entity))
                deleteRoles.add(entity);
        }

        //取消关联
        if (!deleteRoles.isEmpty())
            userEntity.removeRoles(deleteRoles);

        FastJsonPropertyPreFilter filter = new FastJsonPropertyPreFilter();
        filter.addExcludes(RoleEntity.class, "groups", "users", "treeNodes");

        //重新建立关联
        userEntity.addRoles(targetRoles);//不行，会插入两条同样的记录，不知道为什么，只能用 userEntity.setRoles(targetRoles);

        userRepository.save(userEntity);


    }

}
