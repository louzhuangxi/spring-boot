package org.h819.commons.net.html.parser.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Description : TODO()
 * User: h819
 * Date: 2016/11/10
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */

// select(String s) 语法包装类
// https://jsoup.org/apidocs/org/jsoup/select/Selector.html
// 使用
//  Elements elements = new ElementSelector(document).removeTagsDefault().byClass("box1").byTag("li").select();

public class ElementSelector {


    private Elements currentElements;


    private ElementSelector() {
    }

    public ElementSelector(Element element) {
        this.currentElements = element.getAllElements();
    }


    public ElementSelector(Elements elements) {
        this.currentElements = elements;
    }


    public Elements select() {
        if (this.currentElements.isEmpty())
            return new Elements(0);
        else
            return this.currentElements;
    }

    /**
     * 按tag名过虑当前所有节点
     * -
     * select 语法 ：tag
     * example : select("div")
     *
     * @param tagName
     * @return
     */
    public ElementSelector byTag(String tagName) {
        this.currentElements = this.currentElements.select(tagName);
        return this;
    }

    /**
     * 构造  element.select(name).select(name1);
     *
     * @param tag
     * @return
     */
    public ElementSelector byTagsNested(String... tag) {
        Elements temp = this.currentElements;
        for (String name : tag)
            temp = temp.select(name);
        this.currentElements = temp;
        return this;
    }


    /**
     * 去掉常用的，对内容分析没有帮助的标签
     * Remove all script and style elements and those of class "hidden".
     */
    public ElementSelector removeTagsDefault() {
        return removeTags("head", "style", "script");
    }


    /**
     * 去掉 指定的 tag 及内容
     * -
     * select 语法 ：tag1,tag2 ...
     * example :
     * select("script").remove().("style").remove().("head").remove()
     * or
     * select("script, style, .hidden").remove()
     * -
     * 此时 currentElements 会包含所有的标签，逐层递减，会有多个，而不是去掉标签之后的结果，只有第一个是
     *
     * @param tags
     * @return
     */
    public ElementSelector removeTags(String... tags) {
        Elements temp = new Elements();
        for (String s : tags)   // 大小写不敏感，都能去掉
            this.currentElements.select(s).remove();   // currentElements 已经改变
        //此时 currentElements 会包含所有的标签，逐层递减，会有多个，而不是去掉标签之后的结果，只有第一个是
        if (tags.length != 0) { // 避免无参数时，改变 currentElements
            temp.add(this.currentElements.first());
            this.currentElements = temp;
        }
        return this;
    }

    /**
     * 按属性过虑
     * -
     * select 语法 ：[attr]
     * example : select("[width]")
     *
     * @param attributeName 属性名字
     * @return
     */
    public ElementSelector byAttribute(String attributeName) {
        StringBuilder builder = new StringBuilder(3);
        builder.append("[").append(attributeName).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }


    /**
     * 按属性、属性值过虑
     * -
     * select 语法 ：[attr=val]
     * example : select("[width=500]")
     *
     * @param attributeName  属性名字
     * @param attributeValue 属性值
     * @return
     */
    public ElementSelector byAttribute(String attributeName, String attributeValue) {
        StringBuilder builder = new StringBuilder(5);
        builder.append("[").append(attributeName).append("=").append(attributeValue).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按tag、属性和属性值过滤
     * -
     * select 语法 ：tag[attr=val]
     * example : select("img[width=500]")
     *
     * @param tag
     * @param attribute
     * @param attrValue
     * @return
     */
    public ElementSelector byTagAndAttribute(String tag, String attribute, String attrValue) {
        StringBuilder builder = new StringBuilder(6);
        builder.append(tag).append("[").append(attribute).append("=").append(attrValue).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }


    /**
     * 按样式 class 过滤
     * select 语法 ：.class
     * example : select(".left")
     *
     * @param className
     * @return
     */
    public ElementSelector byClass(String className) {
        StringBuilder builder = new StringBuilder(2);
        builder.append(".").append(className);
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按tag、样式 class 过滤
     * select 语法 ：tag.class
     * example : select("div.left")
     *
     * @param tag
     * @param className
     * @return
     */
    public ElementSelector byTagAndClass(String tag, String className) {
        StringBuilder builder = new StringBuilder(3);
        builder.append(tag).append(".").append(className);
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按id 过滤
     * -
     * select 语法 ：#id
     * example : select("#wrap")
     *
     * @param idName
     * @return
     */
    public ElementSelector byId(String idName) {
        StringBuilder builder = new StringBuilder(2);
        builder.append("#").append(idName);
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按tag、id 过滤
     * -
     * select 语法 ：div#id
     * example : select("div#wrap")
     *
     * @param tag
     * @param idName
     * @return
     */
    public ElementSelector byTagAndId(String tag, String idName) {
        StringBuilder builder = new StringBuilder(3);
        builder.append(tag).append("#").append(idName);
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按属性值开始特征部分过虑
     * 按 按属性值开始 过滤
     * -
     * select 语法 ：[attr^=valPrefix]
     * example : select("href^=http:")
     *
     * @param attributeName
     * @param valuePrefix
     * @return
     */
    public ElementSelector byAttrValueStartsWith(String attributeName, String valuePrefix) {
        StringBuilder builder = new StringBuilder(5);
        builder.append("[").append(attributeName).append("^=").append(valuePrefix).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按 tag 、属性值开始 过滤
     * -
     * select 语法 ：tag[attr^=valPrefix]
     * example : select("a[href^=http:]")
     *
     * @param tag
     * @param attributeName
     * @param valuePrefix
     * @return
     */
    public ElementSelector byTagAndAttrValueStartsWith(String tag, String attributeName, String valuePrefix) {
        StringBuilder builder = new StringBuilder(6);
        builder.append(tag).append("[").append(attributeName).append("^=").append(valuePrefix).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按 属性值结束 过滤
     * -
     * select 语法 ：[attr$=valSuffix]
     * example : select("[src$=.png]")
     *
     * @param attributeName
     * @param valSuffix
     * @return
     */
    public ElementSelector byAttrValueEndsWith(String attributeName, String valSuffix) {
        StringBuilder builder = new StringBuilder(5);
        builder.append("[").append(attributeName).append("$=").append(valSuffix).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按 tag 属性值结束 过滤
     * -
     * select 语法 ：tag[attr$=valSuffix]
     * example : select("img[src$=.png]")
     *
     * @param tag
     * @param attributeName
     * @param valSuffix
     * @return
     */
    public ElementSelector byTagAndAttrValueEndsWith(String tag, String attributeName, String valSuffix) {
        StringBuilder builder = new StringBuilder(6);
        builder.append(tag).append("[").append(attributeName).append("$=").append(valSuffix).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按 属性值包含 过滤
     * -
     * select 语法 ：[attr*=valContaining]
     * example : select("[href*=/search/]")
     *
     * @param attributeName
     * @param valContaining
     * @return
     */
    public ElementSelector byAttrValueContais(String attributeName, String valContaining) {
        StringBuilder builder = new StringBuilder(5);
        builder.append("[").append(attributeName).append("*=").append(valContaining).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按 tag 属性值包含 过滤
     * -
     * select 语法 ：tag[attr*=valContaining]
     * example : select("a[href*=/search/]")
     *
     * @param tag
     * @param attributeName
     * @param valContaining
     * @return
     */
    public ElementSelector byTagAndAttrValueContais(String tag, String attributeName, String valContaining) {
        StringBuilder builder = new StringBuilder(6);
        builder.append(tag).append("[").append(attributeName).append("*=").append(valContaining).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }


    /**
     * 按 tag 属性值匹配正则表达式进行 过滤
     * -
     * select 语法 ：[attr~=regex]
     * example : select("[src~=(?i)\\.(png|jpe?g)]")
     *
     * @param attributeName
     * @param valRegex
     * @return
     */
    public ElementSelector byAttrValueRegexMatching(String attributeName, String valRegex) {
        StringBuilder builder = new StringBuilder(5);
        builder.append("[").append(attributeName).append("~=").append(valRegex).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按 tag 属性值匹配正则表达式进行 过滤
     * -
     * select 语法 ：tag[attr~=regex]
     * example : select("img[src~=(?i)\\.(png|jpe?g)]")
     *
     * @param tag
     * @param attributeName
     * @param valRegex
     * @return
     */
    public ElementSelector byTagAndAttrValueRegexMatching(String tag, String attributeName, String valRegex) {
        StringBuilder builder = new StringBuilder(6);
        builder.append(tag).append("[").append(attributeName).append("~=").append(valRegex).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }


    public Elements getAllLinks() {
        return this.currentElements.select("a[href]");

    }

    public Elements getAllMedias() {
        return this.currentElements.select("[src]");

    }

    public Elements getAllImportss() {
        return this.currentElements.select("link[href]");

    }

    @Override
    public String toString() {
        return this.currentElements.toString();
    }
}