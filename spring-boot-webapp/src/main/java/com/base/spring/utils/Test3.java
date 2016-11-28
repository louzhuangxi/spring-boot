package com.base.spring.utils;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.base.spring.domain.UserEntity;
import org.h819.commons.json.FastJsonPropertyPreFilter;


/**
 * Description : TODO()
 * User: h819
 * Date: 2016/7/18
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class Test3 {


    public static void main(String[] args) {

        Test3 t = new Test3();

        String s1 = "http://s.capital-std.com/testcgi.exe?<SCRIPT>alert(“Cookie”+document.cookie)</SCRIPT>";

        t.testJsonPrint();
    }

//    public static String toJSONString(Object object, //
//                                      SerializeConfig config, //
//                                      SerializeFilter[] filters, //
//                                      String dateFormat, //
//                                      int defaultFeatures, //
//                                      SerializerFeature... features) {
//        MySerializeWriter out = new MySerializeWriter(null, defaultFeatures, features);
//
//
//        try {
//            JSONSerializer serializer = new JSONSerializer(out, config);
//
//            if (dateFormat != null && dateFormat.length() != 0) {
//                serializer.setDateFormat(dateFormat);
//                serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
//            }
//
//            if (filters != null) {
//                for (SerializeFilter filter : filters) {
//                    serializer.addFilter(filter);
//                }
//            }
//
//            serializer.write(object);
//            new String(serializer.buf, 0, serializer.count);
//            return out.toString();
//        } finally {
//            out.close();
//        }
//    }

    private String xss(String value) {
        return "";
    }

    private void testJsonPrint() {

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("中文");
        userEntity.setLoginName("english");
        SerializerFeature[] features = {
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.PrettyFormat};

        SerializeConfig config = null;
//        for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
//            config.config(feature, true);
//        }

        FastJsonPropertyPreFilter[] preFilter = null;



//        try {
//
//            public static final int writeJSONString(OutputStream os, //
//                    Charset charset, //
//                    Object object, //
//                    SerializeConfig config, //
//                    SerializeFilter[] filters, //
//                    String dateFormat, //
//            int defaultFeatures, //
//            SerializerFeature... features)
//
//            JSON.writeJSONString(System.out,
//                    Charset.forName("utf-8"),
//                    userEntity,
//                    SerializeConfig.getGlobalInstance(),
//                   preFilter,0,"");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}

