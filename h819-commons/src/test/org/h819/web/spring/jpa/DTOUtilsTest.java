package org.h819.web.spring.jpa;

import org.h819.commons.MyFastJsonUtils;
import org.junit.Test;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/8/5
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class DTOUtilsTest {

    @Test
    public void testCreateDTOcopy() throws Exception {

        DtoUtils utils = new DtoUtils();

        User user = new User("jiang", 40, true);

        // MyJsonUtils.prettyPrint(user);

        MyFastJsonUtils.prettyPrint(utils.createDTOcopy(user,2));

    }


}