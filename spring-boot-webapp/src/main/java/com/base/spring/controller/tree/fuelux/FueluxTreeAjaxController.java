package com.base.spring.controller.tree.fuelux;

import com.base.spring.domain.TreeNodeType;
import com.base.spring.repository.TreeNodeRepository;
import com.base.spring.service.FueluxTreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * FueluxTree 仅进行展示，不做 tree 编辑
 * 每次点击，仅获取被点击节点的子节点
 */

@Controller
@RequestMapping("/tree/fuelux/ajax")
public class FueluxTreeAjaxController {

    private static Logger logger = LoggerFactory.getLogger(FueluxTreeAjaxController.class);

    @Autowired
    TreeNodeRepository treeNodeRepository;
    @Autowired
    FueluxTreeService treeNodeService;

    /**
     * 异步加载，获取数据。
     * 点击关闭的父节点，传过来该父节点的 id，会激发本函数
     *
     * @param pId 首次加载页面，没有点击某个关闭的父节点，此时 id=null
     * @return
     */
    @RequestMapping(value = "/async.html", produces = "application/json")
    @ResponseBody
    public String async(@RequestParam(value = "pId", required = true) Long pId, @RequestParam(value = "menu_type", required = true) TreeNodeType menuType) {

        logger.info("pId={} , type={}", pId, menuType);
        String fueluxJsonData = treeNodeService.async(pId, menuType);
//        logger.info(fueluxJsonData);
        return fueluxJsonData;
    }

}