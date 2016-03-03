package org.h819.web.html;

/**
 * Description : TODO(构造 html select option 标签用到的 java bean，供制造 json 数据用)
 * <p/>
 * <p/>
 * User: h819
 * Date: 14-2-17
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */
public class SelectOptionBean {


    // <option value="sf" selected="selected">sf</option>
    //value
    private String value;
    //显示名称
    private String showValue;
    private boolean selected;

    public SelectOptionBean() {
    }

    public SelectOptionBean(String value, String showValue, boolean selected) {

        this.value = value;
        this.showValue = showValue;
        this.selected = selected;

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getShowValue() {
        return showValue;
    }

    public void setShowValue(String showValue) {
        this.showValue = showValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {

        if (this.selected)
            return "<option value=\"" + this.value + "\"" + " selected=\"selected\">" + this.showValue + "</option>";
        else
            return "<option value=\"" + this.value + "\"" + ">" + this.showValue + "</option>";
    }
}
