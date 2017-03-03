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
     * @param tagName
     * @return
     */
    public ElementSelector byTagsNested(String... tagName) {
        Elements temp = this.currentElements;
        for (String name : tagName)
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

        if (tags.length != 0) { // 避免无参数时，改变 currentElements
            temp.add(this.currentElements.first());     //此时 currentElements 会包含所有的标签，逐层递减，会有多个，而不是去掉标签之后的结果，只有第一个是
            this.currentElements = temp;
        }
        return this;
    }

    /**
     * 标签名称为 tag , 标签的包含属性名称为 attrName 的所有节点
     * "<a class="CDcover185" id="albumCover" href="/album/2100226190" title="Hello">"
     * -
     * select 语法 ：tag[attr]
     * example : select("a[href]")
     *
     * @param tagName  tag 名字
     * @param attrName 属性名字
     * @return
     */
    public ElementSelector byTagAndAttr(String tagName, String attrName) {
        StringBuilder builder = new StringBuilder(5);
        builder.append(tagName).append("[").append(attrName).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }


    /**
     * 标签名称为 tag , 标签的包含属性名称为 attrName ，且 attrName 的值为 attrValue 的所有节点
     * -
     * select 语法 ：tag[attr=val]
     * example : select("img[width=500]")
     *
     * @param tagName
     * @param attrName
     * @param attrValue
     * @return
     */
    public ElementSelector byTagAndAttrAndAttrValue(String tagName, String attrName, String attrValue) {
        StringBuilder builder = new StringBuilder(6);
        builder.append(tagName).append("[").append(attrName).append("=").append(attrValue).append("]");
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
     * 标签名称为 tag , 标签的包含 class 名称为 className  的所有节点
     * select 语法 ：tag.class
     * example : select("div.left")
     * -
     * <div class="left" />
     * -
     *
     * @param tagName
     * @param className
     * @return
     */
    public ElementSelector byTagAndClass(String tagName, String className) {
        StringBuilder builder = new StringBuilder(3);
        builder.append(tagName).append(".").append(className);
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
     * -
     * <div id="wrap" ></div>
     *
     * @param tagName
     * @param idName
     * @return
     */
    public ElementSelector byTagAndId(String tagName, String idName) {
        StringBuilder builder = new StringBuilder(3);
        builder.append(tagName).append("#").append(idName);
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 属性名称为 attrName ，属性值以 valuePrefix 开始的所有节点
     * -
     * select 语法 ：[attr^=valPrefix]
     * example : select("href^=http:")
     *
     * @param attrName
     * @param attrValuePrefix
     * @return
     */
    public ElementSelector byAttrAndValueStartsWith(String attrName, String attrValuePrefix) {
        StringBuilder builder = new StringBuilder(5);
        builder.append("[").append(attrName).append("^=").append(attrValuePrefix).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 标签名称为 tag , 标签的包含属性名称为 attrName ，且 attrName 的值为以 attrValuePrefix 开始的所有节点
     * -
     * select 语法 ：tag[attr^=valPrefix]
     * example : select("a[href^=http:]")
     *
     * @param tagName
     * @param attrName
     * @param attrValuePrefix
     * @return
     */
    public ElementSelector byTagAndAttrAndAttrValueStartsWith(String tagName, String attrName, String attrValuePrefix) {
        StringBuilder builder = new StringBuilder(6);
        builder.append(tagName).append("[").append(attrName).append("^=").append(attrValuePrefix).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 属性名称为 attrName ，属性值以 valSuffix 结尾的所有节点
     * -
     * select 语法 ：[attr$=valSuffix]
     * example : select("[src$=.png]")
     *
     * @param attrName
     * @param attrValSuffix
     * @return
     */
    public ElementSelector byAttrAndValueEndsWith(String attrName, String attrValSuffix) {
        StringBuilder builder = new StringBuilder(5);
        builder.append("[").append(attrName).append("$=").append(attrValSuffix).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 标签名称为 tag , 标签的包含属性名称为 attrName ，且 attrName 的值为以 attrValSuffix 结尾的所有节点
     * -
     * select 语法 ：tag[attr$=valSuffix]
     * example : select("img[src$=.png]")
     *
     * @param tagName
     * @param attrName
     * @param attrValSuffix
     * @return
     */
    public ElementSelector byTagAndAttrAndAttrValueEndsWith(String tagName, String attrName, String attrValSuffix) {
        StringBuilder builder = new StringBuilder(6);
        builder.append(tagName).append("[").append(attrName).append("$=").append(attrValSuffix).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }


    /**
     * 属性名称为 attrName 的所有节点
     * -
     * select 语法 ：[attr]
     *
     * @param attrName
     * @return
     */

    public ElementSelector byAttr(String attrName) {
        StringBuilder builder = new StringBuilder(3);
        builder.append("[").append(attrName).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 属性名称为 attrName ，属性值等于 attrVal 的所有节点
     * -
     * select 语法 ：[attr*=valContaining]
     * example : select("[href*=/search/]")
     *
     * @param attrName
     * @param attrVal
     * @return
     */
    public ElementSelector byAttrAndValue(String attrName, String attrVal) {
        StringBuilder builder = new StringBuilder(5);
        builder.append("[").append(attrName).append("=").append(attrVal).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 属性名称为 attrName ，属性值包含 valContaining 的所有节点
     * -
     * select 语法 ：[attr*=valContaining]
     * example : select("[href*=/search/]")
     *
     * @param attrName
     * @param attrValContaining
     * @return
     */
    public ElementSelector byAttrAndValueContais(String attrName, String attrValContaining) {
        StringBuilder builder = new StringBuilder(5);
        builder.append("[").append(attrName).append("*=").append(attrValContaining).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 标签名称为 tag , 标签的包含属性名称为 attrName ，且 attrName 的值包含 attrValContaining 的所有节点
     * -
     * select 语法 ：tag[attr*=valContaining]
     * example : select("a[href*=/search/]")
     *
     * @param tagName
     * @param attrName
     * @param attrValContaining
     * @return
     */
    public ElementSelector byTagAndAttrAndAttrValueContais(String tagName, String attrName, String attrValContaining) {
        StringBuilder builder = new StringBuilder(6);
        builder.append(tagName).append("[").append(attrName).append("*=").append(attrValContaining).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }


    /**
     * 标签名称为 tag , 标签的包含属性名称为 attrName ，且 attrName 的值包含 满足 attrValRegex 正则表达式的 的所有节点
     * -
     * select 语法 ：[attr~=regex]
     * example : select("[src~=(?i)\\.(png|jpe?g)]")
     *
     * @param attrName
     * @param attrValRegex
     * @return
     */
    public ElementSelector byAttrAndValueRegexMatching(String attrName, String attrValRegex) {
        StringBuilder builder = new StringBuilder(5);
        builder.append("[").append(attrName).append("~=").append(attrValRegex).append("]");
        this.currentElements = this.currentElements.select(builder.toString());
        return this;
    }

    /**
     * 按 tag 属性值匹配正则表达式进行 过滤
     * -
     * select 语法 ：tag[attr~=regex]
     * example : select("img[src~=(?i)\\.(png|jpe?g)]")
     *
     * @param tagName
     * @param attrName
     * @param valRegex
     * @return
     */
    public ElementSelector byTagAndAttrAndAttrValueRegexMatching(String tagName, String attrName, String valRegex) {
        StringBuilder builder = new StringBuilder(6);
        builder.append(tagName).append("[").append(attrName).append("~=").append(valRegex).append("]");
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