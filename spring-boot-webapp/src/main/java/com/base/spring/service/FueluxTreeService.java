package com.base.spring.service;

import com.alibaba.fastjson.JSON;
import com.base.spring.domain.TreeEntity;
import com.base.spring.domain.TreeType;
import com.base.spring.repository.TreeRepository;
import com.base.spring.utils.FueluxTreeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional(readOnly = true) //在事务中(带有 @Transactional)的 fetch = FetchType.LAZY 才可以自动加载
public class FueluxTreeService {

    private static final Logger logger = LoggerFactory.getLogger(FueluxTreeService.class);

    @Autowired
    private TreeRepository treeNodeRepository;

    /**
     * 参照 ztree 的方法
     * FueluxTree 异步模式加载数据。FueluxTree 每次仅返回被点击节点的子节点，不再深入。
     * 两种情况返回值不一样。
     * id==null ，返回一个节点
     * id!=null , 返回一个集合
     * 所以返回值用 String , 不用 ZTreeJsonNode 对象
     *
     * @param pId
     * @return
     */
    public String async(Long pId, TreeType menuType) {

        if (pId == 0) {  // 打开页面时，第一次异步加载，返回根节点的所有子节点
            logger.info("initialize FueluxTree first from db by pId={} , menuType={}", pId, menuType);
            Optional<TreeEntity> rootNode = treeNodeRepository.findRoot(menuType);
            if (!rootNode.isPresent()) {
                logger.info("not exist any tree node !");
                return JSON.toJSONString(new ArrayList<>(0));
            }

            return FueluxTreeUtils.getFueluxTreeJson(rootNode.get().getChildren()); //打开所有一级节点 (跟节点的子节点)

        } else {  // 点击了某个节点，展开该节点，即返回该节点的子节点。 此时有父节点了，就指定菜单类型了，不必再传入
            logger.info("initialize FueluxTree asyncByTreeType from db by pId={}", pId);
            TreeEntity rootNode = treeNodeRepository.findOne(pId);
            if (rootNode == null) {
                logger.info("not exist any tree node !");
                return JSON.toJSONString(new ArrayList<>(0));
            }
            return FueluxTreeUtils.getFueluxTreeJson(rootNode.getChildren());
        }

    }


}