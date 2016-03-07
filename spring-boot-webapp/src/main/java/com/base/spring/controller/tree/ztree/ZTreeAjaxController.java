package com.base.spring.controller.tree.ztree;

import com.base.spring.domain.TreeNodeEntity;
import com.base.spring.domain.TreeNodeType;
import com.base.spring.repository.TreeNodeRepository;
import com.base.spring.service.ZTreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 树操作 : add, copy ,delete  ajax request
 */

@Controller
@RequestMapping("/tree/ztree/ajax")
public class ZTreeAjaxController {

    private static Logger logger = LoggerFactory.getLogger(ZTreeAjaxController.class);

    @Autowired
    TreeNodeRepository treeNodeRepository;
    @Autowired
    ZTreeService treeNodeService;

    /**
     * 异步加载，获取数据。
     * 点击关闭的父节点，传过来该父节点的 id，会激发本函数
     *
     * @param id       首次加载页面，没有点击某个关闭的父节点，此时 id=null
     * @param menuType 第一次加载树，决定了是哪种类型
     * @return
     */
    @RequestMapping(value = "/async.html", produces = "application/json")
    @ResponseBody
    public String async(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "menu_type", required = true) TreeNodeType menuType) {

        logger.info("id={} , menuType={}", id, menuType);
        return treeNodeService.async(id, menuType);

    }

    /**
     * add
     *
     * @return
     */
    @RequestMapping(value = "/add.html", method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestParam(value = "name", required = true) String name,
                      @RequestParam(value = "level", required = true) Integer level,
                      @RequestParam(value = "index", required = true) Integer index,
                      @RequestParam(value = "pId", required = true) Long pId,
                      @RequestParam(value = "isParent", required = true) boolean isParent, @RequestParam(value = "menu_type", required = false) TreeNodeType menuType) {
        logger.info("add new treeNode : name={},level={},index={},pId={},isParent={} , menuType={}", name, level, index, pId, isParent, menuType);

        treeNodeService.add(name, level, index, isParent, pId, menuType);
        return "add succeed.";
    }

    /**
     * edit
     *
     * @return
     */
    @RequestMapping(value = "/edit.html", method = RequestMethod.POST)
    @ResponseBody
    public String edit(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "name", required = true) String name) {
        logger.info("edit treeNode : id={} , name={}", id, name);
        //treeNodeService.clearChildren(id);
        TreeNodeEntity treeNode = treeNodeRepository.findOne(id);
        treeNode.setName(name);
        treeNodeRepository.save(treeNode);
        return "edit succeed.";
    }

    /**
     * del
     *
     * @return
     */
    @RequestMapping(value = "/del.html", method = RequestMethod.POST)
    @ResponseBody
    public String remove(@RequestParam(value = "id", required = true) Long id) {
        logger.info("del treeNode : id={} ", id);
        treeNodeRepository.delete(id);

        return "del succeed.";
    }

    /**
     * clear
     *
     * @return
     */
    @RequestMapping(value = "/clear.html", method = RequestMethod.POST)
    @ResponseBody
    public String clearChildren(@RequestParam(value = "id", required = true) Long id) {
        logger.info("clear treeNode : id={} ", id);
        treeNodeService.clearChildren(id);
        return "clear succeed.";
    }


    /**
     * paste
     * 无论是那种类型的粘帖，ztree 默认添加到子节点的尾部，后台也默认添加到尾部。
     *
     * @return
     */
    @RequestMapping(value = {"/paste.html"}, method = RequestMethod.POST)
    @ResponseBody
    public String paste(@RequestParam(value = "id", required = true) Long id,
                        // @RequestParam(value = "index", required = true) Integer index,
                        @RequestParam(value = "pId", required = true) Long pId,
                        @RequestParam(value = "curType", required = true) String curType) {

        logger.info("paste treeNode : id={},pId={},curType={}", id, pId, curType);

        treeNodeService.paste(id, pId, curType);

        return "paste succeed.";
    }

    /**
     * move
     * 移动节点到指定位置
     *
     * @return
     */
    @RequestMapping(value = {"/move.html"}, method = RequestMethod.POST)
    @ResponseBody
    public String move(@RequestParam(value = "id", required = true) Long id,
                       @RequestParam(value = "index", required = true) Integer index,
                       @RequestParam(value = "pId", required = true) Long pId) {

        logger.info("move treeNode : id={},pId={},index={}", id, pId, index);

        treeNodeService.move(id, pId, index);
        return "move succeed.";
    }

    /**
     节点 CSS 修改
     *
     * @return
     */
    @RequestMapping(value = {"/node/edit/css.html"}, method = RequestMethod.POST)
    @ResponseBody
    public String editCss(@RequestParam(value = "id", required = true) Long id,
                       @RequestParam(value = "css", required = true) String css) {

        logger.info("move treeNode : id={},css={}", id, css.trim());
        treeNodeService.editCss(id, css.trim());
        return "css edit succeed.";
    }

    /**
     * 节点 URL 修改
     *
     * @return
     */
    @RequestMapping(value = {"/node/edit/url.html"}, method = RequestMethod.POST)
    @ResponseBody
    public String editUrl(@RequestParam(value = "id", required = true) Long id,
                          @RequestParam(value = "url", required = true) String url) {

        logger.info("move treeNode : id={},url={}", id, url.trim());
        treeNodeService.editUrl(id, url.trim());
        return "css edit succeed.";
    }

    /**
     * 节点关联标准，可以一次关联多个
     *
     * @return
     */
    @RequestMapping(value = {"/node/link/standard.html"}, method = RequestMethod.POST)
    @ResponseBody
    public String linkStandard(@RequestParam(value = "id", required = true) Long id,
                          @RequestParam(value = "standard", required = true) String standard) {

        logger.info("move treeNode : id={},standard={}", id, standard.trim());
        return "css edit succeed.";
    }

}