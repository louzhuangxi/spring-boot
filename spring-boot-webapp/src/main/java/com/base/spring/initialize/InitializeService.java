package com.base.spring.initialize;

import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.domain.TreeNodeType;
import com.base.spring.repository.TreeNodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description : TODO(初始化相关数据)
 * User: h819
 * Date: 2016/1/18
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
@Component
@Transactional(readOnly = true)
public class InitializeService {
    private static final Logger logger = LoggerFactory.getLogger(InitializeService.class);


    @Autowired
    TreeNodeRepository treeNodeRepository;

    /**
     * 初始化 TreeNodeType 中定义的所有类型的根节点，所有节点均为该节点的子节点
     *
     * @PostConstruct // 在 Bean 初始化之前执行的操作
     * @PreDestroy //在 bean 销毁之前执行的操作
     */
    @PostConstruct // 在 InitializeService Bean 初始化之后执行的方法
    @Transactional(readOnly = false)
    public void initRootNode() {
        logger.info("initialize root menu tree.");
        List<TreeNodeEntity> rootTreeList = treeNodeRepository.getRoot();
        List<TreeNodeType> rootTreeNodeType = new ArrayList<>(rootTreeList.size());
        for (TreeNodeEntity tree : rootTreeList) {
            rootTreeNodeType.add(tree.getType());
        }

        List<TreeNodeType> listType = Arrays.asList(TreeNodeType.values());

        for (TreeNodeType type : listType) {
            if (!rootTreeNodeType.contains(type)) {
                logger.info("init {} tree root node", type);
                TreeNodeEntity root = new TreeNodeEntity(type, "root_" + type, 0, true, null);
                treeNodeRepository.save(root);
            }
        }

    }
}
