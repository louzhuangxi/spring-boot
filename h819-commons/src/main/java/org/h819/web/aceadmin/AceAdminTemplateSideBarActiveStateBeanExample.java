package org.h819.web.aceadmin;


/**
 * Description : TODO(Ace admin 网页模版，处理侧边栏菜单的激活状态，用于保存菜单的激活状态)
 * <p/>
 * User: h819
 * Date: 14-2-17
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */
@Deprecated // ace admin ajax 方式，已经不需要此种方式
public class AceAdminTemplateSideBarActiveStateBeanExample {

    //首页

    //变量随菜单变化，此处仅作演示，实际应用时，拷贝此类到应用中，根据菜单情况调整
    // AceAdminTemplateSideBarActiveStateParams 不需要拷贝，因为不发生变化
    //2760 食品添加剂平台
    private String foodAddMenuActive = "null";
    private String foodAddAdminSubMenuActive = "null";
    //
    //14880 营养强化剂平台
    private String enhancerMenuActive = "null";
    private String enhancerAdminSubMenuActive = "null";

    //9685  营养强化剂平台
    private String packgeAddMenuActive = "null";
    private String packgeAddAdminSubMenuActive = "null";

    public AceAdminTemplateSideBarActiveStateBeanExample() {

    }

    public String getPackgeAddMenuActive() {
        return packgeAddMenuActive;
    }

    public void setPackgeAddMenuActive(String packgeAddMenuActive) {
        this.packgeAddMenuActive = packgeAddMenuActive;
    }

    public String getPackgeAddAdminSubMenuActive() {
        return packgeAddAdminSubMenuActive;
    }

    public void setPackgeAddAdminSubMenuActive(String packgeAddAdminSubMenuActive) {
        this.packgeAddAdminSubMenuActive = packgeAddAdminSubMenuActive;
    }

    public String getEnhancerMenuActive() {
        return enhancerMenuActive;
    }

    public void setEnhancerMenuActive(String enhancerMenuActive) {
        this.enhancerMenuActive = enhancerMenuActive;
    }

    public String getEnhancerAdminSubMenuActive() {
        return enhancerAdminSubMenuActive;
    }

    public void setEnhancerAdminSubMenuActive(String enhancerAdminSubMenuActive) {
        this.enhancerAdminSubMenuActive = enhancerAdminSubMenuActive;
    }

    public String getFoodAddMenuActive() {
        return foodAddMenuActive;
    }

    public void setFoodAddMenuActive(String foodAddMenuActive) {
        this.foodAddMenuActive = foodAddMenuActive;
    }

    public String getFoodAddAdminSubMenuActive() {
        return foodAddAdminSubMenuActive;
    }

    public void setFoodAddAdminSubMenuActive(String foodAddAdminSubMenuActive) {
        this.foodAddAdminSubMenuActive = foodAddAdminSubMenuActive;
    }

}

