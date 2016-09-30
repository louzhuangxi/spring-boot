/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * 参考 springside 4 项目，改动并做了扩展.  SearchFilter 类做了简化
 *******************************************************************************/
package org.h819.commons.json;

public class JsonStringLineType {

    /**
     * 禁止无参实例化
     */
    private JsonStringLineType() {
    }


    /**
     * 单行 json string, 是对象集合，还是单个对象，用于 string -> object 转换判断，调用不同的方法
     *
     */
    //反序列化 String -> Object
    //VO vo = JSON.parseObject("...", VO.class);  //单个对象字符串
    //List<VO> vo = JSON.parseArray("...", VO.class);  //多个对象字符串，如 list 有多个对象，序列化为字符串之后，进行反序列化

    public enum LineType {
        ListObject, Object
    }
}

