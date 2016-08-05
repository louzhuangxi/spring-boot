package com.base.spring.utils;

import com.base.spring.domain.TreeNodeEntity;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/2/12
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    private int level = 0;

    public static void main(String[] ags) {

        Test t = new Test();
        //t.testFileName();
        t.testGetLevel();

    }

    public static void copyProperties(Object fromObj, Object toObj) {
        Class<? extends Object> fromClass = fromObj.getClass();
        Class<? extends Object> toClass = toObj.getClass();

        try {
            BeanInfo fromBean = Introspector.getBeanInfo(fromClass);
            BeanInfo toBean = Introspector.getBeanInfo(toClass);

            PropertyDescriptor[] toPd = toBean.getPropertyDescriptors();
            List<PropertyDescriptor> fromPd = Arrays.asList(fromBean.getPropertyDescriptors());

            for (PropertyDescriptor propertyDescriptor : toPd) {

                propertyDescriptor.getDisplayName();
                PropertyDescriptor pd = fromPd.get(fromPd.indexOf(propertyDescriptor));

                if (pd.getDisplayName().equals(propertyDescriptor.getDisplayName()) && !pd.getDisplayName().equals("class")) {

                    if (propertyDescriptor.getWriteMethod() != null)
                        propertyDescriptor.getWriteMethod().invoke(toObj, pd.getReadMethod().invoke(fromObj, null));

                }

            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void testFileName() {

        File f = new File("D:\\01\\00\\src.pdf");

        System.out.println(f.getParent());
        System.out.println(f.getName());
        System.out.println(f.getAbsolutePath());
        System.out.println(FilenameUtils.getPrefix(f.getAbsolutePath()));
        System.out.println(FilenameUtils.getName(f.getAbsolutePath()));
        System.out.println(FilenameUtils.getBaseName(f.getAbsolutePath()));
        System.out.println(f.getParent() + File.separator + FilenameUtils.getBaseName(f.getAbsolutePath()) + "_." + FilenameUtils.getExtension(f.getAbsolutePath()));


    }

    public void copyBeanProperties(final Object source, final Object target, final Iterable<String> properties) {

        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper trg = new BeanWrapperImpl(target);

        for (final String propertyName : properties) {
            trg.setPropertyValue(propertyName, src.getPropertyValue(propertyName));
        }

    }

    public Object copyBeanProperties(final Object source, final Collection<String> includes) {

        final Collection<String> excludes = new ArrayList<String>();
        final PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());

        Object entityBeanVO = BeanUtils.instantiate(source.getClass());

        for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {

            String propName = propertyDescriptor.getName();
            if (!includes.contains(propName)) {
                excludes.add(propName);
            }


        }

        BeanUtils.copyProperties(source, entityBeanVO, excludes.toArray(new String[excludes.size()]));

        return entityBeanVO;
    }

    public void testGetLevel() {
        TreeNodeEntity entity = new TreeNodeEntity();
        TreeNodeEntity entity1 = new TreeNodeEntity();

        TreeNodeEntity entity2 = new TreeNodeEntity();
        TreeNodeEntity entity3 = new TreeNodeEntity();
        entity.addChildToLastIndex(entity1);
        entity1.addChildToLastIndex(entity2);
        entity2.addChildToLastIndex(entity3);

        getLevell(entity3);

        System.out.println();
        System.out.println("level : " + level);

    }

    private void getLevell(TreeNodeEntity entity) {

        if (entity.getParent() == null) {

            System.out.println("null");
            return;
        } else {
            level++;
            System.out.println("level++ : "+level);
            getLevell(entity.getParent());
        }
    }
}
