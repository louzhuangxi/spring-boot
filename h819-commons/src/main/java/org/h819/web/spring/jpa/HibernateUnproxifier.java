package org.h819.web.spring.jpa;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

/**
 * https://gist.github.com/thinkbigthings/5141813
 * Description : TODO(How to get the entity behind a Hibernate proxy，获取被 hibernate 代理对象)
 * User: h819
 * Date: 2015/5/18
 * Time: 10:27
 * To change this template use File | Settings | File Templates.
 */
public class HibernateUnproxifier<T> {

    @SuppressWarnings("unchecked")
    public T unproxy(T entity) {

        if (entity == null) {
            throw new NullPointerException("Entity passed for initialization is null");
        }

        Hibernate.initialize(entity);
        if (entity instanceof HibernateProxy) {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
        }

        return entity;
    }

}