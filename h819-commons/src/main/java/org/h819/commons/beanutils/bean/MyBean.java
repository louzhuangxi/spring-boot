package org.h819.commons.beanutils.bean;

import java.io.Serializable;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-1-13
 * Time: 下午5:47
 * To change this template use File | Settings | File Templates.
 */
public class MyBean implements Serializable {

    private PersonBean person;

    public PersonBean getPerson() {
        return person;
    }

    public void setPerson(PersonBean person) {
        this.person = person;
    }
}
