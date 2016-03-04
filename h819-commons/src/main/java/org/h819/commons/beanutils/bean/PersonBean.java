package org.h819.commons.beanutils.bean;

import java.io.Serializable;

/**
 * Description : TODO()
 * User: h819
 * Date: 14-1-13
 * Time: 下午5:47
 * To change this template use File | Settings | File Templates.
 */
public class PersonBean implements Serializable {

    private CityBean address;

    public CityBean getAddress() {
        return address;
    }

    public void setAddress(CityBean address) {
        this.address = address;
    }
}
