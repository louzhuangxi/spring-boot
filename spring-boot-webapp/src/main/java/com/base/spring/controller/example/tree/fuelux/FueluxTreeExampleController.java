package com.base.spring.controller.example.tree.fuelux;

import com.base.spring.domain.TreeType;
import com.base.spring.repository.TreeRepository;
import com.base.spring.service.FueluxTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * FueluxTree 仅进行展示，不做 tree 编辑
 * 每次点击，仅获取被点击节点的子节点
 *
 */

@Controller
@RequestMapping("/tree/fuelux/ajax")
@Slf4j
@Deprecated //弃用 . 多选的时候很麻烦，没有全选功能，选择父节点，不能自动选择其子节点
public class FueluxTreeExampleController {

    //private static final log log = LoggerFactory.getLogger(FueluxTreeExampleController.class);

    @Autowired
    TreeRepository treeNodeRepository;
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
    public String async(@RequestParam(value = "pId", required = true) Long pId, @RequestParam(value = "menu_type", required = true) TreeType menuType) {

        log.info("pId={} , type={}", pId, menuType);
        String fueluxJsonData = treeNodeService.async(pId, menuType);
//        log.info(fueluxJsonData);
        return fueluxJsonData;
    }

}