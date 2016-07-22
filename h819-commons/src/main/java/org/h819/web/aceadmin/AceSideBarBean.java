package org.h819.web.aceadmin;


import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO(用于输出 ace admin side bar)
 * <p/>
 * User: h819
 * Date: 14-2-17
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */
@Deprecated // ace admin ajax 方式，已经不需要此种方式
public class AceSideBarBean {


    //链接 url
    private String href = "";
    //菜单名
    private String text = "";
    //菜单图标
    private String iocn = "";
    private List<AceSideBarBean> submenu = new ArrayList<AceSideBarBean>();

    public AceSideBarBean(String text, String href, String iocn, List<AceSideBarBean> submenu) {
        this.text = text;
        this.href = href;
        this.iocn = iocn;
        this.submenu = submenu;

    }

    public List<AceSideBarBean> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(List<AceSideBarBean> submenu) {
        this.submenu = submenu;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIocn() {
        return iocn;
    }

    public void setIocn(String iocn) {
        this.iocn = iocn;
    }
}

