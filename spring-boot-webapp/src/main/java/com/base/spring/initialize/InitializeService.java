package com.base.spring.initialize;

import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.domain.TreeNodeType;
import com.base.spring.repository.TreeNodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/1/18
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional(readOnly = true)
public class InitializeService {
    private static final Logger logger = LoggerFactory.getLogger(InitializeService.class);


    @Autowired
    TreeNodeRepository treeNodeRepository;

    /**
     * 初始化一个根节点，所有节点均为该节点的子节点
     */
    @Transactional(readOnly = false)
    public void initRootNode() {


        for (TreeNodeType type : TreeNodeType.values()) {
            if (treeNodeRepository.getRoot(type) == null) {
                logger.info("init {} tree root node", type);
                TreeNodeEntity root = new TreeNodeEntity(type, "root_" + type, 0, 0, true, null);
                treeNodeRepository.save(root);
            }
        }


    }
}
