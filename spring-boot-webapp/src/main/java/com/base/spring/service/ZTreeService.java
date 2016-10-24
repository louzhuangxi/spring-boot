package com.base.spring.service;

import com.alibaba.fastjson.JSON;
import com.base.spring.domain.RoleEntity;
import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.domain.TreeNodeType;
import com.base.spring.repository.TreeNodeRepository;
import com.base.spring.utils.ZTreeUtils;
import org.h819.web.spring.jpa.DtoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true) //在事务中(带有 @Transactional)的 fetch = FetchType.LAZY 才可以自动加载
public class ZTreeService {

    private static final Logger logger = LoggerFactory.getLogger(ZTreeService.class);

    @Autowired
    private TreeNodeRepository treeNodeRepository;

    /**
     * 异步加载原始树
     *
     * @param id
     * @param menuType
     * @return
     */
    public String asyncTree(Long id, TreeNodeType menuType) {
        return async(id, menuType, 1, null);
    }


    /**
     * 根据 role ，异步加载树
     * 需要 check nodes , 展开所有层级, 对于 getCheckedNodes 时有用，方便选择
     * 层级设置为 100 (100 足够大)。
     *
     * @param id
     * @param menuType
     * @return
     */
    public String asyncRoleTree(Long id, TreeNodeType menuType, RoleEntity roleEntity) {
        return async(id, menuType, 100, roleEntity);
    }


    /**
     * ztree 异步模式加载数据
     * 两种情况返回值不一样。
     * id==null ，返回一个节点
     * id!=null , 返回一个集合
     * 所以返回值用 String , 不用 ZTreeJsonNode 对象
     *
     * @param id
     * @param menuType
     * @param show_Level 页面显示树状结构到第 n 级
     * @return
     */
    private String async(Long id, TreeNodeType menuType, int show_Level, RoleEntity roleEntity) {

        //List<TreeNodeEntity> treeNodeEntity = null;
        DtoUtils dtoUtils = new DtoUtils();
        if (roleEntity == null)
            dtoUtils.addExcludes(TreeNodeEntity.class, "parent", "roles");
        else
            dtoUtils.addExcludes(TreeNodeEntity.class, "parent");

        if (id == null) {  // 第一次打开页面时，异步加载，不是点击了关闭的父节点，所以此时没有 id 参数， id=null . 返回节点本身
            logger.info("initialize ztree first from db by id={} , menuType={}", id, menuType);
            Optional<TreeNodeEntity> rootNode = treeNodeRepository.getRoot(menuType);

            if (!rootNode.isPresent()) {
                logger.info("not exist any tree node !");
                return "";
            }

            TreeNodeEntity dtoRootNode = dtoUtils.createDTOcopy(rootNode.get(), show_Level); // 通过 DTOUtils 开控制返回的层级

            return JSON.toJSONString(ZTreeUtils.getJsonData(dtoRootNode, roleEntity));

        } else {  // 点击了某个节点，展开该节点的子节点。 此时有父节点了，已经知道就指定菜单类型了，不必再传入
            logger.info("initialize ztree asyncByTreeType from db by id={}", id);
            TreeNodeEntity rootNode = treeNodeRepository.findOne(id);
            TreeNodeEntity dtoNode = dtoUtils.createDTOcopy(rootNode, show_Level);
            return JSON.toJSONString(ZTreeUtils.getJsonDataChildren(dtoNode, roleEntity)); //返回节点的子节点
        }
    }


    /**
     * 创建菜单
     *
     * @param name
     * @param level
     * @param index
     * @param isParent
     * @param pId      被选择的父节点，在该节点下创建子节点。
     * @param menuType 菜单类型
     */
    @Transactional(readOnly = false)
    public void add(String name, int level, int index, boolean isParent, long pId, TreeNodeType menuType) {

        logger.info("Getting name={} ,   pId={}", name, pId);

        TreeNodeEntity parent = treeNodeRepository.findOne(pId);
        TreeNodeEntity child = new TreeNodeEntity(menuType, name, index, isParent, parent);
        parent.addChildToLastIndex(child);

//        for(TreeNodeEntity entity :parent.getChildren())
//            System.out.println(String.format("%s,%d,%s",entity.getName(),entity.getIndex(),entity.getParent().getName()));
        parent.setParentNode(true); //如果原来为叶节点，需要设置为父节点
        treeNodeRepository.save(parent);

    }

    /**
     * 情况子节点
     *
     * @param id
     */
    @Transactional(readOnly = false)
    public void clearChildren(long id) {

        logger.info("Getting id={}", id);
        TreeNodeEntity parent = treeNodeRepository.findOne(id);
        parent.clearChildren();
        parent.setParentNode(false);//没有叶子节点了，把父节点设置为叶节点，否则前端显示为文件夹
        treeNodeRepository.save(parent);

    }


    /**
     * 粘帖
     *
     * @param id      被保存的节点
     * @param pId     id 的父节点
     * @param curType 粘帖类型
     */
    @Transactional(readOnly = false)
    public void paste(long id, long pId, String curType) {

        TreeNodeEntity selectNode = treeNodeRepository.findOne(id); //被操作的对象
        TreeNodeEntity parentNode = treeNodeRepository.findOne(pId); //参考对象
        // parentNode.setIsParent(true);

        if (curType.equals("copy")) {
            logger.info("copy nodes to a new parent node");
            ZTreeUtils.createCopyNode(parentNode, selectNode); // 复制一份和新生成的对象，加入到 parent 的子中。
        }

        if (curType.equals("cut")) {    //直接移动cut : 直接修改 currentNode 的父类为新的父类，不重新创建新的对象，相当于剪切过来。
            logger.info("paste nodes to a new parent node");
            parentNode.addChildToLastIndex(selectNode);//此方法可以直接修改父类
            parentNode.setParentNode(true); // 修改参考对象为父节点
        }

        treeNodeRepository.save(parentNode);

        if (selectNode.getParent().getChildren().size() == 0) // 移动完成，如果没有子节点了，则标记为叶节点
            selectNode.getParent().setParentNode(false);
        treeNodeRepository.save(selectNode);

    }


    /**
     * 移动节点到指定父节点下
     * 可也是同一父节点，子节点变换位置
     * 也可以是移动到其他的父节点下
     *
     * @param id    被移动节点
     * @param pId   移动到此父节点下
     * @param index 移到到的位置
     */
    @Transactional(readOnly = false)
    public void move(Long id, Long pId, int index) {

        TreeNodeEntity child = treeNodeRepository.findOne(id);
        TreeNodeEntity parent = treeNodeRepository.findOne(pId);

        parent.addChildToIndex(child, index);

        //  moveChildToIndex(parentNode, childNode, index);

        parent.setParentNode(true);

        if (child.getParent().getChildren().size() == 0)
            child.getParent().setParentNode(false);

        treeNodeRepository.save(child);
        treeNodeRepository.save(parent);

    }

    @Transactional(readOnly = false)
    public void editCss(Long id, String css) {

        TreeNodeEntity treeNodeEntity = treeNodeRepository.findOne(id);
        treeNodeEntity.setCss(css);
        treeNodeRepository.save(treeNodeEntity);

    }

    @Transactional(readOnly = false)
    public void editUrl(Long id, String url) {


        TreeNodeEntity treeNodeEntity = treeNodeRepository.findOne(id);
        treeNodeEntity.setUrl(url);
        treeNodeRepository.save(treeNodeEntity);

    }

}