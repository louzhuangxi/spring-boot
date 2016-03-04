package org.h819.web.jqgird;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 包装 jqgrid search 参数 filter，可以用此工具类直接调用 filter 中的各参数
 * <p/>
 * A POJO that represents a jQgrid JSON requests {@link String}<br/>
 * A sample filter follows the following format:
 * <pre>
 * {"groupOp":"AND","rules":[{"field":"firstName","op":"eq","data":"John"}]}
 * </pre>
 */
public class JqgridFilter {

    private static Map<String, String> operator;
    // private String source;
    private String groupOp;
    private ArrayList<Rule> rules;


    public JqgridFilter() {
        //  super();
        operator = new HashMap<String, String>();
        //jqgrid 传递过来的查询条件之间的关系为如下字符
        // ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
        /**
         * 创建和  org.h819.spring.examples.dynamicsearch.SearchFilter 类中的操作符关系，为动态查询准备数据
         * 页面也应该就这几项内容
         */

        operator.put("eq", "EQ");  //等于
        operator.put("ne", "NE");  //不等于

        operator.put("cn", "LIKE");    //包含
        operator.put("nc", "NLIKE");     //不包含

        operator.put("gt", "GT");  //大于
        operator.put("ge", "GTE");      //大于等于

        operator.put("lt", "LT");  //小于
        operator.put("le", "LTE");      //小于等于


//        operator.put("eq", " = ");  //等于
//        operator.put("ne", " <> ");  //不等于
//        operator.put("lt", " < ");  //小于
//        operator.put("le", " <= ");      //小于等于
//        operator.put("gt", " > ");  //大于
//        operator.put("ge", " >= ");      //大于等于
//        operator.put("bw", " LIKE ");  //开始于
//        operator.put("bn", " NOT LIKE ");       //不开始于
//        operator.put("in", " IN ");     //属于
//        operator.put("ni", " NOT IN ");     //不属于
//        operator.put("ew", " LIKE ");  //结束于
//        operator.put("en", " NOT LIKE ");     //不结束于
//        operator.put("cn", " LIKE ");    //包含
//        operator.put("nc", " NOT LIKE ");     //不包含
//        operator.put("nn", " NOT LIKE ");     //存在
//        operator.put("nu", " NOT LIKE ");     //不存在
    }

    public String getGroupOp() {
        return groupOp;
    }

//    public JqgridFilterBean(String source) {
//        super();
//        this.source = source;
//    }

//    public String getSource() {
//        return source;
//    }
//
//    public void setSource(String source) {
//        this.source = source;
//    }

    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public void setRules(ArrayList<Rule> rules) {
        this.rules = rules;
    }

    public enum Operator {
        EQ, LIKE, GT, LT, GTE, LTE
    }

    /**
     * Inner class containing field rules
     */
    public static class Rule {
        private String junction;
        private String field;
        private String op;
        private String data;

        public Rule() {
        }

        public Rule(String junction, String field, String op, String data) {
            super();
            this.junction = junction;
            this.field = field;
            this.op = op;
            this.data = data;
        }

        public String getJunction() {
            return junction;
        }

        public void setJunction(String junction) {
            this.junction = junction;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getOp() {
            return operator.get(this.op);
        }

        public void setOp(String op) {
            this.op = op;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }


}

