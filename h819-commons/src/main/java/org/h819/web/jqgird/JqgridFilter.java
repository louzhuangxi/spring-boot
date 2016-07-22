package org.h819.web.jqgird;

import org.h819.web.spring.jpa.SearchFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包装 jqgrid search 参数 filter，可以用此工具类直接调用 filter 中的各参数
 * <p>
 * A POJO that represents a jQgrid JSON requests {@link String}<br/>
 * A sample filter follows the following format:
 * <pre>
 *     jqgrid 查询参数返回格式如下:
 * {"groupOp":"AND","rules":[{"field":"firstName","op":"eq","data":"John"}]}
 * </pre>
 */
public class JqgridFilter {

    private static Map<String, String> operator;

    static {
        operator = new HashMap<>();
        //jqgrid 传递过来的查询条件之间的关系为如下字符，和 SearchFilter 中定义的不一致，需要重新对应
        // ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
        /**
         * 创建和  org.h819.spring.examples.dynamicsearch.SearchFilter 类中的操作符关系，为动态查询准备数据
         */
        operator.put("eq", SearchFilter.Operator.EQ.name());  //等于
        operator.put("ne", SearchFilter.Operator.NE.name());  //不等于

        operator.put("cn", SearchFilter.Operator.LIKE.name());    //包含
        operator.put("nc", SearchFilter.Operator.NLIKE.name());     //不包含

        operator.put("gt", SearchFilter.Operator.GT.name());  //大于
        operator.put("ge", SearchFilter.Operator.GTE.name());      //大于等于

        operator.put("lt", SearchFilter.Operator.LT.name());  //小于
        operator.put("le", SearchFilter.Operator.LTE.name());      //小于等于


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

    // private String source;
    private String groupOp;
    private List<Rule> rules = new ArrayList<>();


    /**
     * 必须提供无参的构造方法，供 json string -> object 自动转换用
     */
    public JqgridFilter() {

    }

//    /**
//     * @param groupOp 各个条件之间的关系
//     * @param rules
//     */
//    public JqgridFilter(SearchFilter.Relation groupOp, List<Rule> rules) {
//
//        this.groupOp = groupOp.toString();
//        this.rules = rules;
//    }

    public String getGroupOp() {
        return groupOp;
    }


    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }


//    public enum Operator {
//        EQ, LIKE, GT, LT, GTE, LTE
//    }

    /**
     * Inner class containing field rules
     */

    public static class Rule {

        private String junction;  // 这个参数好像无用
        private String field; //
        private String op;
        private String data;

        /**
         * 必须提供无参的构造方法，供 json string -> object 自动转换用
         */
        public Rule() {
        }


//        /**
//         * jqgrid 中传递的 rule 的各项参数名称，需和 jqgrid 对应
//         *
//         * @param field 参数名称
//         * @param op    运算关系
//         * @param data  参数值
//         */
//        public Rule(String field, SearchFilter.Operator op, String data) {
//            super();
//            this.field = field;
//
//            //map :  get value by key
//            //避免改变 op 的 String 类型，使 JSON String -> Object 时出现错误
//            for (Map.Entry<String, String> e : operator.entrySet()) {
//                if (op.name().equals(e.getValue())) {
//                    this.op = e.getKey();
//                    break;
//                }
//            }
//            this.data = data;
//        }


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

