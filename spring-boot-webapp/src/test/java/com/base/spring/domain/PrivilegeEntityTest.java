package com.base.spring.domain;

import com.base.SpringBootWebappApplication;
import com.base.spring.repository.TreeNodeRepository;
import com.base.spring.repository.PrivilegeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description : TODO()
 * User: h819
 * Date: 2015/12/28
 * Time: 13:48
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootWebappApplication.class)
@Rollback(false)
@Transactional
@WebAppConfiguration
public class PrivilegeEntityTest {

    @Autowired
    PrivilegeRepository privilegeRepository;
    @Autowired
    TreeNodeRepository menuRepository;


    @Test
    public void testSave() {

//        PrivilegeEntity entity = new PrivilegeEntity();
//        entity.setName("privilege");
//        entity.setType(PrivilegeType.Menu);
//        TreeNodeEntity menu = menuRepository.getRoot(TreeNodeType.Menu);;
//        entity.setMenu(menu);
//        privilegeRepository.save(entity);

    }


    @Test
    public void testFine() {

      //  System.out.println("menu : " + privilegeRepository.findByName("privilege").get().getMenu().getName());

    }

    @Test
    public void testDel() {

         privilegeRepository.deleteByName("privilege");
       // privilegeRepository.delete(1l);

    }

}