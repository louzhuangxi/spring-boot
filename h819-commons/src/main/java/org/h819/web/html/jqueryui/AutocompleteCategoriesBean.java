package org.h819.web.html.jqueryui;

/**
 * Description : TODO(构造 jQuery UI Autocomplete - Categories 用到的 java bean，供制造 json 数据用)
 * <p/>
 * <p/>
 * User: h819
 * Date: 14-2-17
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */
public class AutocompleteCategoriesBean {

    /**
     * 构造  jQuery UI Autocomplete - Categories 的数据结构
     */
//    [
//    { label: "anders", category: "" },
//    { label: "andreas", category: "" },
//    { label: "antal", category: "" },
//    { label: "annhhx10", category: "Products" },
//    { label: "annk K12", category: "Products" },
//    { label: "annttop C13", category: "Products" },
//    { label: "anders andersson", category: "People" },
//    { label: "andreas andersson", category: "People" },
//    { label: "andreas johnson", category: "People" }
//    ]
    private String label;
    //显示名称
    private String category;

    public AutocompleteCategoriesBean() {
    }

    public AutocompleteCategoriesBean(String label, String category) {

        this.label = label;
        this.category = category;

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
